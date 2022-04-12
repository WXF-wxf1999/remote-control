#ifndef SERVER_IOURING_H
#define SERVER_IOURING_H

#include <netinet/in.h>
#include <liburing.h>
#include <memory>
#include <atomic>

#include "thread/ThreadPool.h"
#include "object/Sessions.h"

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

    Request() : iov(new iovec) {}
    ~Request() { delete iov; }
};

public:
    IoUring();
    ~IoUring();
    void            start_working();
    //static IoUring& get_instance();

private:

    void setup_listen();
    static void post_recv(int socket, io_uring* ring);
    static void post_send(Request* req, io_uring* ring);
    static void post_accept(int socket, io_uring* ring);
    void handle_io(int socket, Request* req, io_uring* ring);
    int  get_listen_socket() const;
    void handle_request(Request* request, io_uring* ring);
    static void* wait_request(void* parameter);
    void forward_data(Request *request, Packet& packet, io_uring* ring);
    void puppet_login(Request *request, Packet& packet);
    void controller_login(Request *request, Packet& packet);
    static uint32_t receive(int socket, char* buffer, uint32_t size);

private:
    int               session_id_;
    int               listen_socket_;
    int               min_post_accept_count_;
    std::atomic<bool> working_;
    ObjectSpace::Sessions*       sessions_;
    ThreadPoolSpace::ThreadPool* thread_pool_;
};
}




#endif //SERVER_IOURING_H
