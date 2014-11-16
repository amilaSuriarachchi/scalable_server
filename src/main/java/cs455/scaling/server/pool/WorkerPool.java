package cs455.scaling.server.pool;

import java.util.concurrent.CountDownLatch;

/**
 * Worker pool to which client programs submit their tasks. When the worker pools starts it start the thread pool.
 * when starting the thread pool we uses a count down latch just to wait until all threads has started.
 */
public class WorkerPool {

    private int numberOfThreads;
    private TasksQueue tasksQueue;
    private CountDownLatch countDownLatch;

    public WorkerPool(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.tasksQueue = new TasksQueue();
        this.countDownLatch = new CountDownLatch(numberOfThreads);
    }

    public void start() {

        for (int i = 0; i < this.numberOfThreads; i++) {
            Thread thread = new Thread(new Worker(this.tasksQueue, this.countDownLatch));
            thread.start();
        }
        try {
            //wait until all threads starts
            this.countDownLatch.await();
        } catch (InterruptedException e) {
            //TODO: handle this properly if required.
        }
    }

    public void execute(Task task){
        this.tasksQueue.addTask(task);
    }
}
