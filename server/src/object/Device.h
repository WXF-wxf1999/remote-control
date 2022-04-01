#ifndef SERVER_DEVICE_H
#define SERVER_DEVICE_H

#include "proto/DataPacket.pb.h"

namespace ObjectSpace {

enum MessageType {
    PUPPET_LOGIN = 0,
    CONTROLLER_LOGIN = 1,
    DEVICE_NOT_ONLINE = 2
};

class Device {

private:
    int socket_;

public:
    explicit Device(int socket) : socket_(socket) {}

    int get_socket() const {
        return socket_;
    }

};

}
#endif //SERVER_DEVICE_H
