package cs455.scaling.client;

import cs455.scaling.message.ReceiveBuffer;
import cs455.scaling.message.SendQueue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * NIO event handler class for client. IOReactor invoke the readReady and writeReady method to indicate the
 * readiness of the selector. Client handler handles these events accordingly.
 */
public class ClientEvenHandler implements EventHander {

    private Logger logger = Logger.getLogger(IOReactor.class.getName());

    private ReceiveBuffer receiveBuffer;
    private SendQueue sendQueue;

    public ClientEvenHandler(SendQueue sendQueue, ReceiveBuffer receiveBuffer) {
        this.sendQueue = sendQueue;
        this.receiveBuffer = receiveBuffer;
    }

    public void connected(SelectionKey selectionKey) {
        this.logger.log(Level.INFO, "Connect to the server");
    }

    public void readReady(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        try {
            int readLength = socketChannel.read(readBuffer);
            readBuffer.flip();
            byte[] readBytes = Arrays.copyOf(readBuffer.array(), readLength);
            this.receiveBuffer.add(readBytes);
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
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = this.sendQueue.getMessage();
        if (byteBuffer != null) {
            try {
                socketChannel.write(byteBuffer);
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
