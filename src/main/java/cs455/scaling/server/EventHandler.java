package cs455.scaling.server;

import java.nio.channels.SelectionKey;

/**
 * Event handler to hand events from the channel reactor.
 */
public interface EventHandler {

    public void accepted(SelectionKey selectionKey, ServerContex serverContex);

    public void readReady(SelectionKey selectionKey);

    public void writeReady(SelectionKey selectionKey);
}
