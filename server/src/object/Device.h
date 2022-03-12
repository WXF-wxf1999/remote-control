#ifndef SERVER_DEVICE_H
#define SERVER_DEVICE_H

#include "net/IoUring.h"

namespace ObjectSpace {

typedef IoUringSpace::IoUring::Request Request;

class Device {

public:
    virtual void handle_io(Request* request) = 0;

};
}
#endif //SERVER_DEVICE_H
