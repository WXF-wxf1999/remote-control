#include <cstring>
#include <unistd.h>

#include "loguru.h"
#include "IoUring.h"
#include "config.h"

namespace IoUringSpace {

std::mutex      g_mutex;

void IoUring::post_accept(int socket) {
    // get request queue
    io_uring_sqe *sqe = io_uring_get_sqe(&ring_);
    if(sqe == nullptr) {
        LOG_F(ERROR,"io_uring_get_sqe() failed");
        return;
    }
    sockaddr_in *address = nullptr;
    socklen_t *address_len = nullptr;
    // post accept,and pass the client_addr, and then, system will fill it?
    io_uring_prep_accept(sqe, socket, (sockaddr*)address,address_len, 0);

    /* alloc a context , which will pass to cqe data
     this content the client_socket,which will be inited after*/
    auto* req = new Request;
    req->event_type = IoType::OP_ACCEPT;
    io_uring_sqe_set_data(sqe, req);
    io_uring_submit(&ring_);
}

void IoUring::post_recv(int socket) {

    io_uring_sqe *sqe = io_uring_get_sqe(&ring_);
    // in read request, we can only specify the size of buffer in advance
    // in the case of the large amount data, it is a problem
    auto* req = new Request;
    req->iov = new iovec;
    req->iov[0].iov_base = new char[READ_SIZE];
    req->iov[0].iov_len = READ_SIZE;
    req->event_type = IoType::OP_READ;

    // record the client socket which be filled by io_uring_accept
    // so we can get the client socket in cqe(completion queue,and continue to send data to client)
    req->client_socket = socket;
    memset(req->iov[0].iov_base, 0, READ_SIZE);

    io_uring_prep_readv(sqe, socket, &req->iov[0], 1, 0);
    io_uring_sqe_set_data(sqe, req);
    io_uring_submit(&ring_);

}

void IoUring::post_send(Request* req) {

    io_uring_sqe *sqe = io_uring_get_sqe(&ring_);
    if(sqe == nullptr) {
        LOG_F(ERROR, "io_uring_get_sqe() failed");
    }
    req->event_type = IoType::OP_WRITE;
    // in the req, maybe we have some quantity of iovs, all of them will be sent?
    // and how the receiver receive these data?
    io_uring_prep_writev(sqe, req->client_socket, req->iov, req->iovec_count, 0);
    io_uring_sqe_set_data(sqe, req);
    io_uring_submit(&ring_);
}

void IoUring::start_working() {

    //now , we just create one listen socket,but if many client requesr arrives, what shoud we do?
    setup_listen();
    // chech is it ok?
    if(listen_socket_ == 0) {
        return;
    }
    io_uring_queue_init(QUEUE_DEPTH, &ring_, 0);
    // create a dynamic thread pool to wait
    for(auto i = 0; i < min_post_accept_count_; i++) {
        /*
         * Whether the listening socket has a maximum queue,
         * whether I can create another listening socket listening on the same port
         */
        post_accept(listen_socket_);
    }

//    wait_pool_ = new ThreadPoolSpace::DynaThreadPool(0,
//                                                     3,
//                                                     THREAD_HIGH_RATIO,
//                                                     THREAD_LOW_RATIO,
//                                                     THREAD_STANDARD_RATIO,
//                                                     wait_request,
//                                                     this);
//    wait_pool_->start_thread_pool();
    thread_pool_ = new ThreadPoolSpace::ThreadPool(10);
    // wait_request(this);
    // should change threads' number dynamically, now submit two thread using to wait io
    thread_pool_->submit([this] { wait_request(this);});
    thread_pool_->submit([this] { wait_request(this);});

}

void IoUring::setup_listen() {

    sockaddr_in address{};

    listen_socket_ = socket(PF_INET, SOCK_STREAM, 0);
    if (listen_socket_ == -1) {
        LOG_F(ERROR, "create listen socket error");
        return;
    }
    int enable = 1;
    /*
     * The most common use of port reuse should be to prevent the previously bound
     * port from being released when the server is restarted, or the program suddenly
     * exits and the system does not release the port. In this case, if port reuse is
     * set, the newly started server process can bind the port directly.
     * If port multiplexing is not set, the binding will fail, indicating that addr
     * is already in use - so we have to wait and try again
     */
    if (setsockopt(listen_socket_, SOL_SOCKET, SO_REUSEADDR, &enable, sizeof(int)) < 0) {
        LOG_F(ERROR, "setsockopt(SO_REUSEADDR)");
    }
    memset(&address, 0, sizeof(address));
    address.sin_family = AF_INET;
    address.sin_port = htons(SERVER_PORT);
    address.sin_addr.s_addr = htonl(INADDR_ANY);
    
    if (bind(listen_socket_, reinterpret_cast<sockaddr*>(&address), sizeof(address)) < 0) {
        LOG_F(ERROR, "bind failed");
    }

    if (listen(listen_socket_, SOMAXCONN) < 0) {
        LOG_F(ERROR, "listen failed");
    }
}

IoUring::IoUring() : listen_socket_(0), min_post_accept_count_(10)
                   , thread_pool_(nullptr) {

}

IoUring::~IoUring() {
    // if there is no reply to aio, calling this will cause cofusion
    //io_uring_queue_exit(&ring_);
    if(listen_socket_ != 0) {
        close(listen_socket_);
    }
    if(thread_pool_) {
        delete thread_pool_;
    }
}

void* IoUring::wait_request(void* parameter) {

    auto* pthis = static_cast<IoUring*>(parameter);

    io_uring_cqe* cqe = nullptr;

    while (true) {

        int ret = io_uring_wait_cqe(&pthis->ring_, &cqe);

        // store the result, because we will call io_uring_cqe_seen
        auto res = cqe->res;
        auto *req = reinterpret_cast<Request*>(cqe->user_data);

        if (ret < 0) {
            LOG_F(INFO, "io_uring_wait_cqe");
            continue;
        }
        if (res < 0) {
            LOG_F(ERROR, "Async request failed: %s for event: %d\n", strerror(res), req->event_type);
            return nullptr;
        }

        // prevent the io from being processed multiple times
        {
            std::unique_lock<std::mutex> lock(g_mutex);
            if(req->is_handled) {
                continue;
            }
            req->is_handled = true;
        }

        // mark that we have tackled this io event
        io_uring_cqe_seen(&pthis->ring_, cqe);

        // handle io
        pthis->thread_pool_->submit([pthis, res, req]() {pthis->handle_io(res, req);});
    }

}

/*
 * why not get memory directly, because, consider that we have some socket to listen maybe
 */
int IoUring::get_listen_socket() const {
    return listen_socket_;
}

IoUring &IoUring::get_instance() {
    static IoUring io_uring;
    return io_uring;
}

void IoUring::handle_io(int res, IoUring::Request* req) {

    switch (req->event_type) {
        case IoType::OP_ACCEPT:
            // An asynchronous accept request has been consumed, so add a new one
            post_accept(get_listen_socket());

            post_recv(res);
            break;
        case IoType::OP_READ:
            if (!res) {
                LOG_F(INFO, "Empty request!");
                break;
            }
            post_recv(res);
            //handle_client_request(req);
            delete static_cast<char*>(req->iov[0].iov_base);
            break;
        case IoType::OP_WRITE:
            /*
             * just now, we malloced a piece of memory and have written the data to send
             * now, the system has sent them,we should free the memory
            */
            for (int i = 0; i < req->iovec_count; i++) {
                delete static_cast<char*>(req->iov[i].iov_base);
            }
            break;
    }
    delete req;
}

}

