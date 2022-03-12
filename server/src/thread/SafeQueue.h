#ifndef SERVER_SAFEQUEUE_H
#define SERVER_SAFEQUEUE_H

#include <mutex>
#include <queue>

namespace ThreadPoolSpace {

template <typename T>
class SafeQueue {
private:
    std::queue<T> queue_;
    std::mutex mutex_;
public:
    SafeQueue() {
        int i = 0;
    };

    SafeQueue(SafeQueue& other) {
        //TODO:
    }

    ~SafeQueue() = default;


    bool empty() {
        std::unique_lock<std::mutex> lock(mutex_);
        return queue_.empty();
    }

    int size() {
        std::unique_lock<std::mutex> lock(mutex_);
        return queue_.size();
    }

    void enqueue(T& t) {
        std::unique_lock<std::mutex> lock(mutex_);
        queue_.push(t);
    }

    bool dequeue(T& t) {
        std::unique_lock<std::mutex> lock(mutex_);

        if (queue_.empty()) {
            return false;
        }
        t = std::move(queue_.front());

        queue_.pop();
        return true;
    }
};
}
#endif //SERVER_SAFEQUEUE_H
