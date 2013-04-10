package edu.stanford.bmir.protege.web.server.notes.api;

import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class NoteHeaderQuery {

    private static final NoteHeaderQuery MATCH_ANY = new NoteHeaderQuery();

    private long minTimestamp = 0;

    private long maxTimestamp = Long.MAX_VALUE;

    private Pattern authorNamePattern = Pattern.compile(".*");

    private Pattern subjectPattern = Pattern.compile(".*");

    private NoteHeaderQuery() {
    }

    private NoteHeaderQuery(long minTimestamp, long maxTimestamp, Pattern authorNamePattern, Pattern subjectPattern) {
        this.minTimestamp = minTimestamp;
        this.maxTimestamp = maxTimestamp;
        this.authorNamePattern = authorNamePattern;
        this.subjectPattern = subjectPattern;
    }

    public long getMinTimestamp() {
        return minTimestamp;
    }

    public long getMaxTimestamp() {
        return maxTimestamp;
    }

    public Pattern getAuthorNamePattern() {
        return authorNamePattern;
    }

    public Pattern getSubjectPattern() {
        return subjectPattern;
    }

    public static NoteHeaderQuery getMatchAny() {
        return MATCH_ANY;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {

        private long minTimestamp = 0;

        private long maxTimestamp = Long.MAX_VALUE;

        private Pattern authorNamePattern = Pattern.compile(".*");

        private Pattern subjectPattern = Pattern.compile(".*");

        private Builder() {
        }

        public Builder setMinTimestamp(long minTimestamp) {
            this.minTimestamp = minTimestamp;
            return this;
        }

        public Builder setMaxTimestamp(long maxTimestamp) {
            this.maxTimestamp = maxTimestamp;
            return this;
        }

        public Builder setAuthorNamePattern(Pattern authorNamePattern) {
            this.authorNamePattern = authorNamePattern;
            return this;
        }

        public Builder setSubjectPattern(Pattern subjectPattern) {
            this.subjectPattern = subjectPattern;
            return this;
        }

        public NoteHeaderQuery build() {
            return new NoteHeaderQuery(this.minTimestamp, this.maxTimestamp, this.authorNamePattern, this.subjectPattern);
        }
    }

}
