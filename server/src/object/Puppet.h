#ifndef SERVER_PUPPET_H
#define SERVER_PUPPET_H

#include <netinet/in.h>

#include "Device.h"

namespace ObjectSpace {

class Puppet : public Device{

public:
    void handle_io(Packet& request) override;
    Puppet(int socket) : socket_(socket) {}

private:
    int socket_;

};
}



#endif //SERVER_PUPPET_H
