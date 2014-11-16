package cs455.scaling.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class creates the sockect channel to the server and keep listening to read and write events.
 *
 */
public class IOReactor implements Runnable {

    Logger logger = Logger.getLogger(IOReactor.class.getName());

    private EventHander eventHander;
    private String ipAddress;
    private int port;

    public IOReactor(EventHander eventHander, String ipAddress, int port) {
        this.eventHander = eventHander;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void run() {

        try {
            Selector selector = Selector.open();
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(ipAddress, port));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while (selector.isOpen()){
                selector.select();
                for(SelectionKey selectionKey : selector.selectedKeys()){
                    if (selectionKey.isConnectable()){
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        if (!channel.finishConnect()){
                            continue;
                        }
                        channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        this.eventHander.connected(selectionKey);
                    } else if (selectionKey.isReadable()){
                        this.eventHander.readReady(selectionKey);
                    } else if (selectionKey.isWritable()){
                        this.eventHander.writeReady(selectionKey);
                    }
                }
                selector.selectedKeys().clear();
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can not open the socket ", e);
        }

    }
}
