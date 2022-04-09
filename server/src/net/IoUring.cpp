#include <cstring>
#include <unistd.h>

#include "loguru.h"
#include "IoUring.h"
#include "config.h"
#include "proto/Coder.h"
#include "proto/DataPacket.pb.h"

namespace IoUringSpace {

void IoUring::post_accept(int socket, io_uring* ring) {
    // get request queue
    io_uring_sqe *sqe = io_uring_get_sqe(ring);
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
    io_uring_submit(ring);
}

void IoUring::post_recv(int socket, io_uring* ring) {

    LOG_F(INFO, "POST RECV");
    io_uring_sqe *sqe = io_uring_get_sqe(ring);
    // in read request, we can only specify the size of buffer in advance
    // in the case of the large amount data, it is a problem
    auto* req = new Request;
    req->iov[0].iov_base = new char[READ_SIZE];
    req->iov[0].iov_len = READ_SIZE;
    req->event_type = IoType::OP_READ;

    // record the client socket which be filled by io_uring_accept
    // so we can get the client socket in cqe(completion queue,and continue to send data to client)
    req->client_socket = socket;
    memset(req->iov[0].iov_base, 0, READ_SIZE);

    io_uring_prep_readv(sqe, socket, &req->iov[0], 1, 0);
    io_uring_sqe_set_data(sqe, req);
    io_uring_submit(ring);

}

void IoUring::post_send(Request* req, io_uring* ring) {

    LOG_F(INFO, "SEND");
    io_uring_sqe *sqe = io_uring_get_sqe(ring);
    if(sqe == nullptr) {
        LOG_F(ERROR, "io_uring_get_sqe() failed");
    }
    req->event_type = IoType::OP_WRITE;
    // in the req, maybe we have some quantity of iovs, all of them will be sent?
    // and how the receiver receive these data?
    io_uring_prep_writev(sqe, req->client_socket, req->iov, req->iovec_count, 0);
    io_uring_sqe_set_data(sqe, req);
    io_uring_submit(ring);
}

void IoUring::start_working() {

    //now , we just create one listen socket,but if many client requesr arrives, what shoud we do?
    setup_listen();
    // chech is it ok?
    if(listen_socket_ == 0) {
        return;
    }

    int thread_number = static_cast<int>(sysconf(_SC_NPROCESSORS_ONLN)) * 2;

    thread_pool_ = new ThreadPoolSpace::ThreadPool(thread_number);

    for(int i =0; i<thread_number; i++) {
        thread_pool_->submit([this] { wait_request(this);});
    }

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

    int res = 0;
    res = bind(listen_socket_, reinterpret_cast<sockaddr*>(&address), sizeof(address));
    if ( res < 0) {
        LOG_F(ERROR, "bind failed with error code : %d", res);
    }

    if (listen(listen_socket_, SOMAXCONN) < 0) {
        LOG_F(ERROR, "listen failed");
    }
}

IoUring::IoUring() : listen_socket_(0), min_post_accept_count_(10), session_id_(0), working_(true)
                   , thread_pool_(nullptr), sessions_(new ObjectSpace::Sessions()) {

}

IoUring::~IoUring() {

    working_ = false;
    sleep(1);

    if(listen_socket_ != 0) {
        close(listen_socket_);
    }

    delete thread_pool_;
    delete sessions_;

}

void* IoUring::wait_request(void* parameter) {

    io_uring          ring{};

    auto* pthis = static_cast<IoUring*>(parameter);

    io_uring_queue_init(QUEUE_DEPTH, &ring, 0);

    // create a dynamic thread pool to wait
    for(auto i = 0; i < pthis->min_post_accept_count_; i++) {
        /*
         * Whether the listening socket has a maximum queue,
         * whether I can create another listening socket listening on the same port
         */
        pthis->post_accept(pthis->listen_socket_, &ring);
    }

    io_uring_cqe* cqe = nullptr;

    while (pthis->working_) {

        int ret = io_uring_wait_cqe(&ring, &cqe);

        if (ret < 0) {
            LOG_F(INFO, "io_uring_wait_cqe %d", ret);
            continue;
        }

        // store the result, because we will call io_uring_cqe_seen
        auto res = cqe->res;
        auto *req = reinterpret_cast<Request*>(cqe->user_data);

        if (res < 0) {
            LOG_F(ERROR, "Async request failed: %s for event: %d\n", strerror(res), req->event_type);
            //continue;
            return nullptr;
        }

        // mark that we have tackled this io event
        io_uring_cqe_seen(&ring, cqe);

        LOG_F(INFO, "handle io");
        // handle io
        pthis->handle_io(res, req, &ring);
    }

    io_uring_queue_exit(&ring);
    return nullptr;
}

/*
 * why not get memory directly, because, consider that we have some socket to listen maybe
 */
int IoUring::get_listen_socket() const {
    return listen_socket_;
}

uint32_t receive(int socket, char* buffer, uint32_t size) {

    uint32_t receive_len = 0;
    uint32_t total_len = 0;

    receive_len = recv(socket, buffer, size, 0);
    total_len = *(uint32_t*)buffer + sizeof(int);
    while (receive_len != total_len) {
        receive_len += recv(socket,buffer + receive_len,size - receive_len,0);
    }
    return total_len;
}

void IoUring::handle_io(int res, IoUring::Request* req, io_uring* ring) {

    switch (req->event_type) {
        case IoType::OP_ACCEPT:
            // An asynchronous accept request has been consumed, so add a new one
            post_accept(get_listen_socket(), ring);
            post_recv(res, ring);
            LOG_F(INFO, "ACCEPT");
            delete req;
            break;
        case IoType::OP_READ:
            if (!res) {
                LOG_F(INFO, "Empty request!");
                break;
            }
            LOG_F(INFO, "READ");
            // this step doesn't need delete req,because we use it repeatedly
            handle_request(req, ring);
            break;
        case IoType::OP_WRITE:
            /*
             * just now, we malloced a piece of memory and have written the data to send
             * now, the system has sent them,we should free the memory
            */
            for (int i = 0; i < req->iovec_count; i++) {
                delete static_cast<char*>(req->iov[i].iov_base);
            }
            delete req;
            break;
    }

}

void IoUring::handle_request(IoUring::Request *request, io_uring* ring) {

    Packet packet;
    if(CoderSpace::Coder::decode(request, packet) == -1) {

        LOG_F(INFO, "receive a malformed packet");
        return;
    }

    switch (packet.messagetype()) {
        case ObjectSpace::MessageType::PUPPET_LOGIN:

            puppet_login(request, packet);
            post_send(request, ring);
            break;
        case ObjectSpace::MessageType::CONTROLLER_LOGIN:

            controller_login(request, packet);
            post_recv(request->client_socket, ring);
            post_send(request, ring);
            break;
            // must come from controller, the puppet only send message one time
        default:
            post_recv(request->client_socket, ring);
            forward_data(request, packet, ring);
    }
}

void IoUring::puppet_login(IoUring::Request *request, Packet &packet) {

    // assign a unique session id
    session_id_++;
    // add in sessions
    sessions_->add_puppet(session_id_, new ObjectSpace::Device(request->client_socket));
    // only thing to change
    packet.set_sessionid(session_id_);
    // rewrite request according to packet now it's unnecessary
    delete static_cast<char*>(request->iov[0].iov_base);
    CoderSpace::Coder::encode(request, request->client_socket, packet);

}

void IoUring::controller_login(IoUring::Request *request, Packet &packet) {

    uint32_t session_id = packet.sessionid();

    // convert to right format
    session_id = session_id & 0x01111111;

    // programming is still required
    if (!sessions_->find(session_id)) {
        packet.set_messagetype(ObjectSpace::DEVICE_NOT_ONLINE);
        // there is no corresponding puppet
    }
    else {
        sessions_->add_controller(session_id, new ObjectSpace::Device(request->client_socket));
        packet.set_messagetype(ObjectSpace::CONTROLLER_LOGIN);
    }
     delete static_cast<char*>(request->iov[0].iov_base);
    CoderSpace::Coder::encode(request, request->client_socket, packet);

}

static const uint32_t g_buffer_size = 1024*1024*3;
static char* const g_buffer = new char[g_buffer_size]{0};

void IoUring::forward_data(IoUring::Request *request, Packet &packet,io_uring* ring) {

    // get session id
    uint32_t session_id = packet.sessionid();

    int client_socket = 0;

    // if highest bit is true , it's controller
    if(session_id >> 31) {
        // no need now
        session_id = session_id & 0x01111111;
        client_socket = sessions_->getPuppet(session_id)->get_socket();
    }
    else {

        client_socket = sessions_->getController(session_id)->get_socket();
    }

    size_t send_len = packet.ByteSizeLong() + 4;

    // relay message to puppet
    long return_len = 0;
    send(client_socket, request->iov[0].iov_base, send_len, 0);
    // receive the message from puppet
    return_len = receive(client_socket, g_buffer, g_buffer_size);
    // return the result to controller
    send(request->client_socket, g_buffer, return_len, 0);

    delete static_cast<char*>(request->iov[0].iov_base);
    delete request;

}


}

