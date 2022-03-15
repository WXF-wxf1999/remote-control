//
// Created by hyc on 2022/3/15.
//

#ifndef SERVER_CONTROLLER_H
#define SERVER_CONTROLLER_H

#include "Device.h"

namespace ObjectSpace{

class Controller : public Device {

public:
    void handle_io(Packet& packet) override;
};

}



#endif //SERVER_CONTROLLER_H
