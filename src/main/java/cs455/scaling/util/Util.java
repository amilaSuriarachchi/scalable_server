package cs455.scaling.util;

import cs455.scaling.exception.MessageProcessingException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class
 */
public class Util {

    public static String getSHA1String(byte[] message) throws MessageProcessingException {
        byte[] hash = getSHA1Hash(message);
        BigInteger hashInt = new BigInteger(1, hash);
        return hashInt.toString(16);
    }

    public static byte[] getSHA1Hash(byte[] message) throws MessageProcessingException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            return messageDigest.digest(message);
        } catch (NoSuchAlgorithmException e) {
            throw new MessageProcessingException("Can not get the SHA1 factory ", e);
        }
    }
}
