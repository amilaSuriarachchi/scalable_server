package cs455.scaling.server;

import cs455.scaling.exception.MessageProcessingException;
import cs455.scaling.message.SendQueue;
import cs455.scaling.server.pool.Task;
import cs455.scaling.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tasks class server used to submit the work to the worker pool.This task creates the has and send that to
 * send queue
 */
public class ServerTask implements Task {

    private Logger logger = Logger.getLogger(ServerTask.class.getName());

    private byte[] message;
    private SendQueue sendQueue;

    public ServerTask(byte[] message, SendQueue sendQueue) {
        this.message = message;
        this.sendQueue = sendQueue;
    }

    public void execute() {
        try {
            byte[] hash = Util.getSHA1Hash(message);
            ByteBuffer byteBuffer = ByteBuffer.allocate(hash.length);
            byteBuffer.put(hash);
            byteBuffer.flip();
            this.sendQueue.addMessage(byteBuffer);
        } catch (MessageProcessingException e) {
            logger.log(Level.SEVERE, "Can not get the hash ", e);
        }

    }
}
