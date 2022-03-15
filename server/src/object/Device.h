#ifndef SERVER_DEVICE_H
#define SERVER_DEVICE_H

#include "proto/DataPacket.pb.h"

namespace ObjectSpace {

enum MessageType {
    LOGIN = 0,
    HEART_BEAT = 1
};

class Device {

public:
    virtual void handle_io(Packet& packet) {};

};

}
#endif //SERVER_DEVICE_H
