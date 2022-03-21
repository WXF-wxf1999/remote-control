//
// Created by hyc on 2022/3/15.
//

#ifndef SERVER_CODER_H
#define SERVER_CODER_H

#include "DataPacket.pb.h"
#include "net/IoUring.h"

namespace CoderSpace {

class Coder {

public:
    static int decode(IoUringSpace::IoUring::Request *request, Packet& packet) {

        char* data = static_cast<char*>(request->iov[0].iov_base);

        // len = sizeof(int) + len(protobuf)
        int data_len = 0;

        memcpy(&data_len, data, sizeof(unsigned int));

        if(data_len < 0) {
            return -1;
        }

        // move the pointer to object's location
        data = data + sizeof(int);
        // deserialize packet
        packet.ParseFromArray(data, data_len);
        return 0;
    }

    static int encode(IoUringSpace::IoUring::Request *request, int socket, Packet& packet) {

        int bytes_length = packet.ByteSizeLong();

        // actually when we set the clientId and messageType, the length is 2 bytes
//        if(bytes_length < 8) {
//            // sizeof(clientId) + sizeof(messageType) = 8
//            return -1;
//        }
        int data_len = bytes_length + 4;

        request->client_socket = socket;
        request->iov[0].iov_base = new char[data_len];
        request->iov[0].iov_len = data_len;
        request->event_type = IoUringSpace::IoType::OP_WRITE;
        request->iovec_count = 1;

        *(static_cast<int*>(request->iov[0].iov_base)) = bytes_length;
        unsigned long address = reinterpret_cast<unsigned long>(request->iov[0].iov_base) + sizeof(int);
        // store in iov_base
        packet.SerializeToArray(reinterpret_cast<void*>(address), static_cast<int>(bytes_length));
        return 0;
    }
};
}



#endif //SERVER_CODER_H
