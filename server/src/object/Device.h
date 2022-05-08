#ifndef SERVER_DEVICE_H
#define SERVER_DEVICE_H

#include "proto/DataPacket.pb.h"

namespace ObjectSpace {

enum MessageType {
    PUPPET_LOGIN = 0,
    CONTROLLER_LOGIN = 1,
    DEVICE_NOT_ONLINE = 2,
    DESKTOP_CONTROL = 3,
    CURSOR_CONTROL_LEFT_DOWN = 4,
    CURSOR_CONTROL_RIGHT_DOWN = 5,
    CURSOR_CONTROL_WHEEL = 6,
    KEYBOARD_CONTROL = 7,
    DRIVER_REQUEST = 8,
    FILE_RESEARCH = 9,
    FILE_REQUEST = 10
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
