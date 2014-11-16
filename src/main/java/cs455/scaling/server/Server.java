package cs455.scaling.server;

import cs455.scaling.message.ReceiveBuffer;
import cs455.scaling.message.SendQueue;
import cs455.scaling.server.pool.WorkerPool;
import cs455.scaling.util.Constants;

/**
 * this starts the server. First it starts the worker pool and then start the IOReactor.
 */
public class Server {

    public static void main(String[] args) {

        int portNumber = Integer.parseInt(args[0]);
        int threadPoolSize = Integer.parseInt(args[1]);

        //create a Worker pool and start
        WorkerPool workerPool = new WorkerPool(threadPoolSize);
        workerPool.start();

        ServerEventHandler serverEventHandler = new ServerEventHandler(workerPool);
        IOReactor ioReactor = new IOReactor(portNumber, serverEventHandler);
        Thread thread = new Thread(ioReactor);
        thread.start();
    }

}
