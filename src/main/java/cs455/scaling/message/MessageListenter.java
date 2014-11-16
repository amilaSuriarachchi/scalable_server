package cs455.scaling.message;

/**
 * this is the call back class used to receive full messages from
 */
public interface MessageListenter {

    public void messageReceived(byte[] message);

}
