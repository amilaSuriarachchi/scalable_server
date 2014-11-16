package cs455.scaling.server.pool;

/**
 * Task interface for any program that uses this pool. The client program should create a task which implements this
 * interface and submit to process.
 */
public interface Task {

     public void execute();
}
