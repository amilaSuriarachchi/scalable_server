package cs455.scaling.client;

import cs455.scaling.exception.MessageProcessingException;
import cs455.scaling.message.ReceiveBuffer;
import cs455.scaling.message.SendQueue;
import cs455.scaling.util.Constants;
import cs455.scaling.util.Util;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class starts the client side. First it starts the IOReactor to receive the messages at the
 * client side. Then it uses the client message buffer to store the messages and check.
 */
public class Client {

    private static Logger logger = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {

        String serverHost = args[0];
        int serverPort = Integer.parseInt(args[1]);
        int messageRate = Integer.parseInt(args[2]);

        //create a message receiver and receive buffer.
        ClientMessageBuffer clientMessageBuffer = new ClientMessageBuffer();
        ReceiveBuffer receiveBuffer = new ReceiveBuffer(clientMessageBuffer, Constants.HASH_SIZE);

        SendQueue sendQueue = new SendQueue();
        EventHander eventHander = new ClientEvenHandler(sendQueue, receiveBuffer);
        IOReactor ioReactor = new IOReactor(eventHander, serverHost, serverPort);
        Thread thread = new Thread(ioReactor);
        thread.start();

        MessageGenerator messageGenerator = new MessageGenerator();
        while (true){
            ByteBuffer byteBuffer = ByteBuffer.allocate(Constants.MESSAGE_SIZE);
            byte[] messageToSend = messageGenerator.getRandomMessage();
            try {
                clientMessageBuffer.addMessage(Util.getSHA1Hash(messageToSend));
                byteBuffer.put(messageToSend);
                byteBuffer.flip();
                sendQueue.addMessage(byteBuffer);
            } catch (MessageProcessingException e) {
                logger.log(Level.SEVERE, "Can not get the digest ", e);
            }
            try {
                Thread.sleep(1000/messageRate);
            } catch (InterruptedException e) {

            }
        }
    }
}
