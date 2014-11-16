package cs455.scaling.client;

import cs455.scaling.message.MessageListenter;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * this class is used to store the sent message hashes and compare that with the response received. This class simply
 * keep hases in a linked list and compare when a hash response comes.
 */
public class ClientMessageBuffer implements MessageListenter {

    private Logger logger = Logger.getLogger(ClientMessageBuffer.class.getName());

    private List<byte[]> messageHashes = new LinkedList<byte[]>();

    public void messageReceived(byte[] message) {


        //check whether the message is avaialbe and remove if so
        synchronized (this.messageHashes) {
            for (int i = 0; i < this.messageHashes.size(); i++) {
                if (isEqual(message, this.messageHashes.get(i))) {
                    logger.log(Level.INFO, "Matching entry found for " + new BigInteger(1, message).toString(16));
                    this.messageHashes.remove(i);
                    return;
                }
            }
        }
        logger.log(Level.SEVERE, "Did not find the matching entry for " + new BigInteger(1, message).toString(16));

    }

    public void addMessage(byte[] message){
        synchronized (this.messageHashes){
            this.messageHashes.add(message);
        }
    }


    private boolean isEqual(byte[] message1, byte[] message2) {
        if (message1.length != message2.length) {
            return false;
        } else {
            for (int i = 0; i < message1.length; i++) {
                if (message1[i] != message2[i]) {
                    return false;
                }
            }
        }
        return true;
    }
}
