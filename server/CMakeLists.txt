cmake_minimum_required(VERSION 3.21)
project(server)

set(CMAKE_CXX_STANDARD 14)

include_directories(include src 3rdparty)

find_package(Protobuf REQUIRED)

add_executable(server src/main.cpp 3rdparty/loguru.cpp 3rdparty/loguru.h
        src/net/IoUring.cpp src/net/IoUring.h include/config.h
        src/object/Device.h src/object/Puppet.cpp src/object/Puppet.h src/thread/ThreadPool.h src/thread/SafeQueue.h
        src/proto/DataPacket.pb.h src/proto/DataPacket.pb.cc src/proto/Coder.h src/object/Controller.cpp src/object/Controller.h src/object/Sessions.h)

include_directories(${PROTOBUF_INCLUDE_DIR})

target_link_libraries(server uring dl pthread ${PROTOBUF_LIBRARY})
