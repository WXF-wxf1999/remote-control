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
    std::unordered_map<int, std::pair<Device*, Device*>> session_;

public:

    Device* getPuppet(int session_id) {

        return session_[session_id].second;
    }

    Device* getController(int session_id) {

        return session_[session_id].first;
    }

    void add_session(int session_id, Device* controller, Device* puppet) {

        session_.insert({session_id,{controller,puppet}});
    }
    ~Sessions() {
        // release objects
        for(auto &item : session_) {
            delete item.second.second;
            delete item.second.second;
        }
    }
};

}



#endif //SERVER_SESSIONS_H
