package edu.stanford.bmir.protege.web.server.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/12/2013
 */
public class WebProtegeLogFormatter extends LayoutBase<ILoggingEvent> {

    private static final int COL_WIDTH = 160;

    public static final String PREFIX = "[WebProtege] ";

    public static final Pattern WS = Pattern.compile("\\n| ");

    @Override
    public String doLayout(ILoggingEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX);
        sb.append(new Date(event.getTimeStamp()));
        sb.append("\n");

        StringBuilder messageBuilder = new StringBuilder();
        if (event.getLevel().isGreaterOrEqual(Level.ERROR)) {
            messageBuilder.append(" ***** ");
            messageBuilder.append(event.getLevel().toString());
            messageBuilder.append(" *****\n");
        }
        messageBuilder.append(event.getMessage());
        Iterable<String> lines = wrap(messageBuilder.toString(), COL_WIDTH);
        for(String line : lines) {
            padWithPrefix(sb);
            sb.append(line);
            sb.append("\n");
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
