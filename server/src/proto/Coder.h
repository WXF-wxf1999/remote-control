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

        // mistake data packet
        if(request->iov[0].iov_len < sizeof(int)) {
            return -1;
        }
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

        size_t bytes_length = packet.ByteSizeLong();

        if(bytes_length < 8) {
            // sizeof(clientId) + sizeof(messageType) = 8
            return -1;
        }
        request = new IoUringSpace::IoUring::Request;
        request->iov = new iovec;
        request->client_socket = socket;
        request->iov[0].iov_base = new char[bytes_length];
        request->iov[0].iov_len = bytes_length;
        request->event_type = IoUringSpace::IoType::OP_WRITE;
        request->iovec_count = 1;

        // store in iov_base
        packet.SerializeToArray(request->iov[0].iov_base, static_cast<int>(bytes_length));
        return 0;
    }
};
}



#endif //SERVER_CODER_H
