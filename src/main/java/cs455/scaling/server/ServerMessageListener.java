package cs455.scaling.server;

import cs455.scaling.exception.MessageProcessingException;
import cs455.scaling.message.MessageListenter;
import cs455.scaling.message.SendQueue;
import cs455.scaling.server.pool.Task;
import cs455.scaling.server.pool.WorkerPool;
import cs455.scaling.util.Util;

/**
 * Message listener to receive messages from the Receive buffer. Once receive a message this class create a task
 * and submit that to worker pool.
 */
public class ServerMessageListener implements MessageListenter {

    private WorkerPool workerPool;
    private SendQueue sendQueue;

    public ServerMessageListener(WorkerPool workerPool, SendQueue sendQueue) {
        this.workerPool = workerPool;
        this.sendQueue = sendQueue;
    }

    public void messageReceived(byte[] message) {

        Task task = new ServerTask(message, this.sendQueue);
        this.workerPool.execute(task);
    }
}
