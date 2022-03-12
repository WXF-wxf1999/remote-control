#ifndef SERVER_PUPPET_H
#define SERVER_PUPPET_H
#include "Device.h"

namespace ObjectSpace {

class Puppet : public Device{

enum IoType {
    LOGIN = 0,
    HEART_BEAT = 1
};

public:
    void handle_io(Request* request) override;
};
}



#endif //SERVER_PUPPET_H
