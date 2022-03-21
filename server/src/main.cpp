#include <semaphore.h>
#include <iostream>

#include "net/IoUring.h"

sem_t g_semaphore;

void sigint_handler(int signo) {

        std::cout<<"ctrl c"<< std::endl;
        sem_post(&g_semaphore);

}

int main() {

    std::unique_ptr<IoUringSpace::IoUring> net(new IoUringSpace::IoUring());

    net->start_working();
    // the main thread must exist,or net object will destory and call ~IoUring
    sem_init(&g_semaphore, 0, 0);
    signal(SIGINT, sigint_handler);
    sem_wait(&g_semaphore);
    sem_destroy(&g_semaphore);
    sem_close(&g_semaphore);
    std::cout<<"over"<<std::endl;
    return 0;
}