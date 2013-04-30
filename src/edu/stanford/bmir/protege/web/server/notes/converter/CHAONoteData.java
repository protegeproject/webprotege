package edu.stanford.bmir.protege.web.server.notes.converter;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/08/2012
 * <p>
 *     Represents data parsed from a CHAO ontology.
 * </p>
 */
public class CHAONoteData implements Comparable<CHAONoteData> {

    private String author;
    
    private Optional<String> subject;
    
    private String body;
    
    private String type;
    
    private long timestamp;
    
    private List<CHAONoteData> replies = new ArrayList<CHAONoteData>();

    public CHAONoteData(String author, Optional<String> subject, String body, String type, long timestamp, List<CHAONoteData> replies) {
        this.author = author;
        this.subject = subject;
        this.body = body;
        this.type = type;
        this.timestamp = timestamp;
        this.replies.addAll(replies);
    }


    public String getAuthor() {
        return author;
    }

    public Optional<String> getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
    public List<CHAONoteData> getReplies() {
        return new ArrayList<CHAONoteData>(replies);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendNote(sb, 0);
        return sb.toString();
    }
    
    private void appendNote(StringBuilder sb, int depth) {
        StringBuilder prefixBuilder = new StringBuilder();
        for(int i = 0; i < depth; i++) {
            prefixBuilder.append("        |");
        }
        String prefix = prefixBuilder.toString();

        sb.append(prefix);
        sb.append("--------------------------------------------------------\n");
        sb.append(prefix);
        sb.append("AUTHOR   : ");
        sb.append(author);
        sb.append("\n");


        sb.append(prefix);
        sb.append("TYPE     : ");
        sb.append(type);
        sb.append("\n");


        sb.append(prefix);
        sb.append("TIME     : ");
        sb.append(new Date(timestamp));
        sb.append("\n");


        sb.append(prefix);
        sb.append("SUBJECT  : ");
        sb.append(subject.or(""));
        sb.append("\n");


        sb.append(prefix);
        sb.append("BODY     : ");
        sb.append(body);
        sb.append("\n");

        for(CHAONoteData reply : replies) {
            reply.appendNote(sb, depth + 1);
        }
    }


    public int compareTo(CHAONoteData o) {
       return (int) (this.timestamp - o.timestamp);

    }
}
