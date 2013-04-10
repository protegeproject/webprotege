package edu.stanford.bmir.protege.web.server.notes.api;

import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class NoteStoreQuery {

    public static final Pattern MATCH_ANY_STRING = Pattern.compile(".*");

    private NoteHeaderQuery headerQuery;

    private NoteContentQuery contentQuery;


    public NoteStoreQuery(NoteHeaderQuery headerQuery, NoteContentQuery contentQuery) {
        this.headerQuery = headerQuery;
        this.contentQuery = contentQuery;
    }

    public NoteHeaderQuery getHeaderQuery() {
        return headerQuery;
    }

    public NoteContentQuery getContentQuery() {
        return contentQuery;
    }
}
