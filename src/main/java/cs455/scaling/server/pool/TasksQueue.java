package cs455.scaling.server.pool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Task queue to hold the tasks. Worker threads the wait on this if there is not tasks.
 * When a task is received that will notify the waiting threads and one worker will pick the task.
 */
public class TasksQueue {

    private Queue<Task> tasksQueue;

    public TasksQueue() {
        this.tasksQueue = new ConcurrentLinkedQueue<Task>();
    }

    public synchronized void addTask(Task task){
        this.tasksQueue.add(task);
        this.notifyAll();
    }

    public synchronized Task getTask(){
         if (this.tasksQueue.isEmpty()){
             try {
                 this.wait();
                 return  getTask();
             } catch (InterruptedException e) {
                 //TODO: handle this properly
             }
         } else {
             return this.tasksQueue.poll();
         }
        // this can not happen unless there is an interpution.
        return null;
    }
}
