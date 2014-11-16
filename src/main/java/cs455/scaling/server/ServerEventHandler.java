package cs455.scaling.server;

import cs455.scaling.message.MessageListenter;
import cs455.scaling.message.ReceiveBuffer;
import cs455.scaling.message.SendQueue;
import cs455.scaling.server.pool.WorkerPool;
import cs455.scaling.util.Constants;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class to receive events.
 */
public class ServerEventHandler implements EventHandler {

    private Logger logger = Logger.getLogger(ServerEventHandler.class.getName());
    private WorkerPool workerPool;

    public ServerEventHandler(WorkerPool workerPool) {
        this.workerPool = workerPool;
    }

    public void accepted(SelectionKey selectionKey, ServerContex serverContex) {
        SendQueue sendQueue = new SendQueue();
        MessageListenter messageListenter = new ServerMessageListener(this.workerPool, sendQueue);
        ReceiveBuffer receiveBuffer = new ReceiveBuffer(messageListenter, Constants.MESSAGE_SIZE);
        serverContex.setReceiveBuffer(receiveBuffer);
        serverContex.setSendQueue(sendQueue);

    }

    public void readReady(SelectionKey selectionKey) {
        ServerContex serverContex = (ServerContex) selectionKey.attachment();
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        try {
            int readLength = socketChannel.read(readBuffer);
            readBuffer.flip();
            byte[] readBytes = Arrays.copyOf(readBuffer.array(), readLength);
            serverContex.getReceiveBuffer().add(readBytes);
            readBuffer.clear();

        } catch (Exception e) {
            selectionKey.cancel();
            try {
                socketChannel.close();
            } catch (Exception e1) {
                logger.log(Level.SEVERE, "Can not close the channel");
            }
            logger.log(Level.SEVERE, "Can not read data from the channel");
        }
    }

    public void writeReady(SelectionKey selectionKey) {
        ServerContex serverContex = (ServerContex) selectionKey.attachment();
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer writeBuffer = serverContex.getSendQueue().getMessage();

        if (writeBuffer != null){
            try {
                logger.log(Level.INFO, "Sending hash "
                        + new BigInteger(1, writeBuffer.array()).toString(16) + " back to client " + socketChannel.getRemoteAddress());
                socketChannel.write(writeBuffer);
            } catch (Exception e) {
                selectionKey.cancel();
                try {
                    socketChannel.close();
                } catch (Exception e1) {
                    logger.log(Level.SEVERE, "Can not close the channel");
                }
                logger.log(Level.SEVERE, "Can not read data from the channel");
            }
        }
    }
}
