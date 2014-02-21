package edu.stanford.bmir.protege.web.server.logging;

import com.google.common.base.Splitter;

import java.util.*;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/12/2013
 */
public class WebProtegeLogFormatter extends Formatter {

    private static final int COL_WIDTH = 160;

    public static final String PREFIX = "[WebProtege] ";

    public static final Pattern WS = Pattern.compile("\\n| ");

    @Override
    public String format(LogRecord logRecord) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX);

        StringBuilder messageBuilder = new StringBuilder();
        if (logRecord.getLevel() == Level.SEVERE) {
            messageBuilder.append(" ***** ");
            messageBuilder.append(logRecord.getLevel());
            messageBuilder.append(" *****\n");
        }
        messageBuilder.append(logRecord.getMessage());
        Iterable<String> lines = wrap(messageBuilder.toString(), COL_WIDTH);
        boolean first = true;
        for(String line : lines) {
            if (!first) {
                padWithPrefix(sb);
            }
            sb.append(line);
            if(first) {
                int len = line.length();
                while(len < COL_WIDTH) {
                    sb.append(" ");
                    len++;
                }
                sb.append("        [");
                sb.append(new Date(logRecord.getMillis()));
                sb.append("]");
                sb.append("\n");
            }
            else {
                sb.append("\n");
            }
            first = false;
        }
        return sb.toString();
    }

    private void padWithPrefix(StringBuilder sb) {
        for(int j = 0; j < PREFIX.length(); j++) {
            sb.append(" ");
        }
    }

    private static Iterable<String> wrap(String message, int width) {
        List<String> result = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(message, "\n ", true);
        String last = "";
        while(tokenizer.hasMoreTokens()) {
            StringBuilder line = new StringBuilder(last);
            while(tokenizer.hasMoreTokens()) {
                String chunk = tokenizer.nextToken();
                if (chunk.equals("\n")) {
                    last = "";
                    break;
                }
                else if(line.length() < width) {
                    if(line.length() + chunk.length() < width) {
                        line.append(chunk);
                    }
                    else if(line.length() == 0) {
                        // Longer, but goes on this line
                        line.append(chunk);
                        last = "";
                        break;
                    }
                    else {
                        // Goes on next line
                        last = chunk;
                        break;
                    }
                }
                else {
                    last = chunk;
                    break;
                }
            }
            result.add(line.toString().trim());
            if(!tokenizer.hasMoreTokens() && !last.isEmpty()) {
                result.add(last);
            }
        }
        return result;
    }
}
