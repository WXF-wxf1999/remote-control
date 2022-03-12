cmake_minimum_required(VERSION 3.21)
project(server)

set(CMAKE_CXX_STANDARD 14)

include_directories(include src 3rdparty)

add_executable(server src/main.cpp 3rdparty/loguru.cpp 3rdparty/loguru.h
        src/net/IoUring.cpp src/net/IoUring.h include/config.h
        src/object/Device.h src/object/Puppet.cpp src/object/Puppet.h src/thread/ThreadPool.h src/thread/SafeQueue.h)

target_link_libraries(server uring dl pthread)
