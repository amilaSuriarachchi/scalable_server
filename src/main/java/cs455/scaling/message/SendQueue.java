package cs455.scaling.message;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * queue to hold the messages as Byte buffers until send them. when the write channel is ready that will take a message
 * from here and send write the the channel.
 */
public class SendQueue {

    private Queue<ByteBuffer> messages;

    public SendQueue() {
        this.messages =  new ConcurrentLinkedQueue<ByteBuffer>();
    }

    public synchronized void addMessage(ByteBuffer byteBuffer) {
        this.messages.add(byteBuffer);
    }

    public synchronized ByteBuffer getMessage() {

        if (this.messages.size() > 0) {
            if (this.messages.peek().hasRemaining()) {
                return this.messages.peek();
            } else {
                this.messages.remove();
                if (this.messages.size() > 0) {
                    return this.messages.peek();
                }
            }
        }
        // if this list size is zero nothing to do.
        return null;
    }
}
