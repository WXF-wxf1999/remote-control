#include "Puppet.h"
#include "loguru.h"

namespace ObjectSpace {

void Puppet::handle_io(Packet& packet) {


    switch (packet.messagetype()) {
        case 0:
            break;
    }

    // create manage object

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

