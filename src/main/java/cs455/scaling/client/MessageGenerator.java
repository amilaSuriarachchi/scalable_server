package cs455.scaling.client;

import cs455.scaling.util.Constants;


/**
 * generates random messages to the client.
 */
public class MessageGenerator {

    public byte[] getRandomMessage() {
        byte[] message = new byte[Constants.MESSAGE_SIZE];
        // we set a random value to each byte to generate a ramdom message.
        for (int i = 0; i < message.length; i++) {
            message[i] = (byte) Math.floor(Math.random() * 256);
        }
        return message;
    }

}
