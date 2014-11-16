package cs455.scaling.server.pool;

import java.util.concurrent.CountDownLatch;

/**
 * this is a thread pool worker. These workes starts at the begining and they wait on task queue to receive messages.
 */
public class Worker implements Runnable {

    private TasksQueue tasksQueue;
    private CountDownLatch countDownLatch;

    public Worker(TasksQueue tasksQueue, CountDownLatch countDownLatch) {
        this.tasksQueue = tasksQueue;
        this.countDownLatch = countDownLatch;
    }

    public void run() {
        this.countDownLatch.countDown();
        while (true){
            Task task = this.tasksQueue.getTask();
            task.execute();
        }
    }
}
