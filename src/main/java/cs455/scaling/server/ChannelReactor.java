package cs455.scaling.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server side uses channel reactors to receive events from different channels. There can be 100s of connections.
 * one channel reactor works with a set of channels. Channel reactors are created to the amount of cores in the system.
 */
public class ChannelReactor implements Runnable {

    private Logger logger = Logger.getLogger(ChannelReactor.class.getName());

    private EventHandler eventHandler;
    private Queue<SocketChannel> connectionQueue;
    private Selector selector;

    public ChannelReactor(EventHandler eventHandler) throws IOException {
        this.eventHandler = eventHandler;
        this.connectionQueue = new ConcurrentLinkedQueue<SocketChannel>();
        this.selector = Selector.open();

    }

    public void run() {

        while (this.selector.isOpen()) {
            try {
                processNewChannels();
                this.selector.select();
                for (SelectionKey selectionKey : this.selector.selectedKeys()) {
                    if (selectionKey.isReadable()) {
                        this.eventHandler.readReady(selectionKey);
                    } else if (selectionKey.isWritable()) {
                        this.eventHandler.writeReady(selectionKey);
                    }
                }
                this.selector.selectedKeys().clear();

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Can not process the channel ", e);
            }
        }

    }

    private void processNewChannels() throws IOException {
        SocketChannel socketChannel;
        while ((socketChannel = this.connectionQueue.poll()) != null) {
            // register this channel with this selector
            socketChannel.configureBlocking(false);
            ServerContex serverContex = new ServerContex();
            SelectionKey selectionKey =
                    socketChannel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, serverContex);
            this.eventHandler.accepted(selectionKey, serverContex);
        }
    }

    public void addNewChannel(SocketChannel socketChannel) {
        this.connectionQueue.add(socketChannel);
        //selector thread may be blocking for new connections. Need to wake up that.
        this.selector.wakeup();
    }
}
