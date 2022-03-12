#ifndef SERVER_IOURING_H
#define SERVER_IOURING_H

#include <netinet/in.h>
#include <liburing.h>
#include <memory>
#include <atomic>

#include "thread/ThreadPool.h"

namespace IoUringSpace {

enum class IoType {
    OP_ACCEPT = 0,
    OP_READ = 1,
    OP_WRITE = 2
};

class IoUring {

public:
struct Request {

    int iovec_count{};
    int client_socket{};   // it can nvolve a class object
    iovec *iov;  // iovc*
    IoType event_type;
    std::atomic<bool> is_handled;

    Request() : is_handled(false), iov(nullptr) {}
};

public:
    ~IoUring();
    void            start_working();
    static IoUring& get_instance();

private:
    IoUring();
    void setup_listen();
    void post_recv(int socket);
    void post_send(Request* req);
    void post_accept(int socket);
    void handle_io(int res, Request* req);
    int  get_listen_socket() const;
    static void* wait_request(void* parameter);

private:
    int               listen_socket_;
    int               min_post_accept_count_;
    io_uring          ring_{};
    ThreadPoolSpace::ThreadPool* thread_pool_;
};
}




#endif //SERVER_IOURING_H
