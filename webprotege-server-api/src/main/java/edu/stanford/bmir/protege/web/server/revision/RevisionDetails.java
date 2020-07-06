package edu.stanford.bmir.protege.web.server.revision;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.api.TimestampSerializer;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 */
@JsonPropertyOrder({"rev", "timestamp", "userId", "msg", "subjects", "changes"})
public class RevisionDetails {

    private final long revisionNumber;

    private final long timestamp;

    private final UserId userId;

    private final String msg;

    private final ImmutableCollection<IRI> subjects;

    private final ImmutableList<ChangeDetails> changeDetails;

    public RevisionDetails(int revisionNumber,
                           long timestamp,
                           UserId userId,
                           String msg,
                           ImmutableCollection<IRI> subjects,
                           ImmutableList<ChangeDetails> changeDetails) {
        this.revisionNumber = revisionNumber;
        this.timestamp = timestamp;
        this.userId = userId;
        this.msg = msg;
        this.subjects = subjects;
        this.changeDetails = changeDetails;
    }

    @JsonProperty("rev")
    public long getRevisionNumber() {
        return revisionNumber;
    }

    @JsonSerialize(using = TimestampSerializer.class)
    public long getTimestamp() {
        return timestamp;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getMsg() {
        return msg;
    }

    public ImmutableCollection<IRI> getSubjects() {
        return subjects;
    }

    @JsonProperty("changes")
    public List<ChangeDetails> getChangeDetails() {
        return changeDetails;
    }

    public enum ChangeOperation {
        ADD,
        REMOVE
    }

    public static class ChangeDetails {

        private final ChangeOperation operation;

        private final OWLAxiom axiom;

        public ChangeDetails(ChangeOperation operation, OWLAxiom axiom) {
            this.operation = operation;
            this.axiom = axiom;
        }

        public ChangeOperation getOperation() {
            return operation;
        }

        @JsonSerialize(using = ToStringSerializer.class)
        public OWLAxiom getAxiom() {
            return axiom;
        }
    }
}
