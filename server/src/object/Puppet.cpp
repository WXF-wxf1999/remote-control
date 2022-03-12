#include "Puppet.h"

namespace ObjectSpace {

void Puppet::handle_io(ObjectSpace::Request *request) {

    char* recv_data = static_cast<char*>(request->iov[0].iov_base);
    unsigned int recv_len = request->iov[0].iov_len;

//    need parse the object‘s serialzation information
//    Buffer buffer(recv_data, recv_len);
//    auto type = static_cast<Puppet::IoType>(buffer.get_type());
//    switch (type) {
//        case  LOGIN:
//            break;
//        case HEART_BEAT:
//            break;
//    }
}
}

