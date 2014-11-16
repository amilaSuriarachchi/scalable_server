package cs455.scaling.client;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Event handler interface that client used to receive events from IOReactor
 */
public interface EventHander {

    public void connected(SelectionKey selectionKey);

    public void readReady(SelectionKey selectionKey);

    public void writeReady(SelectionKey selectionKey);
}
