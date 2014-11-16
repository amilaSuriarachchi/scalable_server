package cs455.scaling.message;

import cs455.scaling.exception.MessageProcessingException;
import cs455.scaling.server.ServerTask;
import cs455.scaling.server.pool.Task;
import cs455.scaling.server.pool.WorkerPool;
import cs455.scaling.util.Constants;
import cs455.scaling.util.Util;

/**
 * When reading the message from socket channel. There is no gurantte that we read the amount given in the
 * byte buffer. And also having an 8k bytebuffer is very in efficient as well. So we use a small byte buffer and
 * construct the message with 8k here. Then pass the message to upper layer.
 */
public class ReceiveBuffer {

    // this is a 8k/20 buffer to temporaliy hold the recived message parts
    private byte[] currentMessage;
    // current message size
    private int currentMessageLegnth;
    // listener to pass the message to upper layer
    private MessageListenter messageListenter;
    // maximum size of the message for server it is 8k and client 20
    private int maxSize;

    public ReceiveBuffer(MessageListenter messageListenter, int maxSize) {
        this.maxSize = maxSize;
        this.messageListenter = messageListenter;
        this.currentMessage = new byte[this.maxSize];
        this.currentMessageLegnth = 0;
    }

    public void add(byte[] message) {

        int remainingMessageLength = message.length;
        int readMessageLength = 0;
        while (this.currentMessageLegnth + remainingMessageLength >= this.maxSize) {
            int requiredLength = this.maxSize - this.currentMessageLegnth;
            System.arraycopy(message, readMessageLength, this.currentMessage, this.currentMessageLegnth, requiredLength);
            executeMessage();
            //now update the remaining length. we have already required amount.
            remainingMessageLength -= requiredLength;
            readMessageLength += requiredLength;
        }

        // if there are parts remaining we can copy that to current buffer to be used in next message
        if (remainingMessageLength > 0) {
            System.arraycopy(message, readMessageLength, this.currentMessage, this.currentMessageLegnth, message.length - readMessageLength);
            this.currentMessageLegnth = this.currentMessageLegnth + message.length - readMessageLength;
        }

    }

    private void executeMessage() {

        this.messageListenter.messageReceived(this.currentMessage);
        this.currentMessage = new byte[this.maxSize];
        this.currentMessageLegnth = 0;
    }


}
