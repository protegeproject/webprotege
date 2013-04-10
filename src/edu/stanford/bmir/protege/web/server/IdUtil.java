package edu.stanford.bmir.protege.web.server;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.UUID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/01/2013
 */
public class IdUtil {

    public static String getBase62UUID() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        BigInteger bi = new BigInteger(byteBuffer.array());
        return convertToBase62String(bi);
    }


    public static String getBase62RandomInt() {
        int randomValue = (int)(Math.random() * Integer.MAX_VALUE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(randomValue);
        BigInteger bi = new BigInteger(byteBuffer.array());
        return convertToBase62String(bi);
    }



    private static final String[] elements = {

            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",

            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i",

            "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",

            "y", "z", "0", "1", "2", "3", "4",

            "5", "6", "7", "8", "9"

    };

    private static final BigInteger BASE = new BigInteger("62");

    private static String convertToBase62String(BigInteger value) {
        StringBuilder sb = new StringBuilder();
        while(true) {
            BigInteger mod = value.mod(BASE);
            sb.insert(0, elements[(int) mod.intValue()]);
            value = value.divide(BASE);
            if(value.equals(BigInteger.ZERO)) {
                break;
            }
        }
        return sb.toString();
    }


}
