//
// Created by hyc on 2022/3/15.
//

#ifndef SERVER_SESSIONS_H
#define SERVER_SESSIONS_H

#include <unordered_map>

#include "Device.h"

namespace ObjectSpace {

class Sessions {

private:
    // <  sessionId, <Controller, Puppet>  >
    std::unordered_map<uint32_t, std::pair<Device*, Device*>> session_;

public:

    Device* getPuppet(uint32_t session_id) {

        return session_[session_id & 0x01111111].second;
    }

    Device* getController(uint32_t session_id) {

        return session_[session_id & 0x01111111].first;
    }

    bool find(uint32_t session_id) {
        if(session_.find(session_id) == session_.end()) {
            return false;
        }
        return true;
    }
    void add_puppet(uint32_t session_id, Device* puppet) {

        session_.insert({session_id, {nullptr, puppet}});
    }

    void add_controller(uint32_t session_id, Device* controller) {

        session_[session_id].first = controller;
    }
    ~Sessions() {
        for(auto& it : session_) {
            delete it.second.first;
            delete it.second.second;
        }
    }
};

}



#endif //SERVER_SESSIONS_H
