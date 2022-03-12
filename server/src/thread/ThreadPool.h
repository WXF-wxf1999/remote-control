#pragma once

#include <functional>
#include <future>
#include <thread>
#include <utility>
#include <vector>

#include "SafeQueue.h"

namespace ThreadPoolSpace {

// a little weight static thread pool
class ThreadPool {

private:

    bool shutdown_;
    SafeQueue<std::function<void()>> queue_;
    std::vector<std::thread> threads_;
    std::mutex conditional_mutex_;
    std::condition_variable conditional_lock_;

public:
    explicit ThreadPool(const int n_threads)
            : threads_(std::vector<std::thread>(n_threads))
            , shutdown_(false) {

        for (auto & i : threads_) {
            i = std::thread( [this] {
            std::function<void()> func;
            bool dequeued;
            while (!shutdown_) {
                {
                    std::unique_lock<std::mutex> lock(conditional_mutex_);
                    if (queue_.empty()) {
                        conditional_lock_.wait(lock);
                    }
                    dequeued = queue_.dequeue(func);
                }
                if (dequeued) {
                    func();
                }
            }
            });
        }
    }
    ~ThreadPool() {
        shutdown_ = true;
        conditional_lock_.notify_all();

        for (auto & thread : threads_) {
            if(thread.joinable()) {
                thread.join();
            }
        }
    }

    // Submit a function to be executed asynchronously by the pool
    template<typename F, typename...Args>
    auto submit(F&& f, Args&&... args) -> std::future<decltype(f(args...))> {
        // Create a function with bounded parameters ready to execute
        std::function<decltype(f(args...))()> func = std::bind(std::forward<F>(f), std::forward<Args>(args)...);
        // store in packaged_task
        auto task_ptr = std::make_shared<std::packaged_task<decltype(f(args...))()>>(func);

        // Wrap packaged task into void function
        std::function<void()> wrapper_func = [task_ptr]() {
            (*task_ptr)();
        };

        // Enqueue generic wrapper function
        queue_.enqueue(wrapper_func);

        // Wake up one thread if its waiting
        conditional_lock_.notify_one();

        // Return future result asynchronously
        return task_ptr->get_future();
    }
};
}

