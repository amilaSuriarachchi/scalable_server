package cs455.scaling.server;

import cs455.scaling.message.ReceiveBuffer;
import cs455.scaling.message.SendQueue;

/**
 * used to keep the context data for each channel connection. This context is associated with the session key
 * to handle the connections.
 */
public class ServerContex {

    private ReceiveBuffer receiveBuffer;
    private SendQueue sendQueue;

    public ReceiveBuffer getReceiveBuffer() {
        return receiveBuffer;
    }

    public void setReceiveBuffer(ReceiveBuffer receiveBuffer) {
        this.receiveBuffer = receiveBuffer;
    }

    public SendQueue getSendQueue() {
        return sendQueue;
    }

    public void setSendQueue(SendQueue sendQueue) {
        this.sendQueue = sendQueue;
    }
}
