package cs455.scaling.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server IOReactor. This class creates the socket channel and starts the channel reactors. It creates channel reactors
 * according to the number of available processes. The selector used in this class is used to listen to the connections
 * and hand over them to channel reactors.
 *
 */
public class IOReactor implements Runnable{

    Logger logger = Logger.getLogger(IOReactor.class.getName());

    private int port;
    private EventHandler eventHandler;
    private List<ChannelReactor> channelReactors;


    public IOReactor(int port, EventHandler eventHandler) {
        this.port = port;
        this.eventHandler = eventHandler;
        this.channelReactors = new ArrayList<ChannelReactor>();
    }

    public void run() {
        ServerSocketChannel serverSocket = null;
        try {

            serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(this.port));
            serverSocket.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            //create channel reactors according to the number of processors
            int numberOfProcessors = Runtime.getRuntime().availableProcessors();
            for (int i = 0;i<numberOfProcessors;i++){
                this.channelReactors.add(new ChannelReactor(this.eventHandler));
            }
            // start the channel reactors
            for (ChannelReactor channelReactor : this.channelReactors){
                Thread thread = new Thread(channelReactor);
                thread.start();
            }

            int lastChennelSelected = 0;

            while (selector.isOpen()){
                selector.select();
                for (SelectionKey selectionKey : selector.selectedKeys()){
                    if (selectionKey.isAcceptable()){
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        //find a chennel to handover this thread in round robin manner
                        int channelNum = lastChennelSelected % numberOfProcessors;
                        lastChennelSelected++;
                        this.channelReactors.get(channelNum).addNewChannel(socketChannel);
                        logger.log(Level.INFO, "Accepted a connection from " + socketChannel.getRemoteAddress());
                    }
                }
                selector.selectedKeys().clear();
            }
        } catch (IOException e) {
           logger.log(Level.SEVERE, "Can not read from the socket channel");
        }

    }
}
