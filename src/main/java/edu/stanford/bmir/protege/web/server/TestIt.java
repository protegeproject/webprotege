package edu.stanford.bmir.protege.web.server;

import com.google.common.base.Splitter;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Aug 2017
 */
public class TestIt {

    public static void main(String[] args) {
        Map<String, String> map = new TreeMap<>();
        List<String> list = Arrays.asList("A12", "A1", "A0", "120", "12", "25", "1.2", "1.4", "0.2", "Another", "AAA3", Long.toString(Long.MAX_VALUE));
        list.forEach(item -> {
            map.put(generateSortKey(item), item);
        });
        map.forEach((s, s2) -> {
            System.out.println(s2 + "     " + s);
        });
        // 9223372036854775794
    }

    public static String generateSortKey(String s) {
        StringBuilder sb = new StringBuilder();
        StringBuilder buffer = new StringBuilder();
        boolean inDigit = false;
        for(int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if(Character.isDigit(ch)) {
                inDigit = true;
                buffer.append(ch);
            }
            else {
                if(inDigit) {
                    sb.append(encode(Long.parseLong(buffer.toString())));
                }
                else {
                    sb.append(ch);
                }
                inDigit = false;

            }
        }
        if(inDigit) {
            sb.append(encode(Long.parseLong(buffer.toString())));
        }
        System.out.println(s + " --> " + sb.toString());

        return sb.toString();
    }

    static String encode(long v) {
        return String.format("%018d", v);
//        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
//        byteBuffer.putLong(v);
//        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }
}
