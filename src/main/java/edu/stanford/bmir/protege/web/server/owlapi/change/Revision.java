package edu.stanford.bmir.protege.web.server.owlapi.change;

import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.change.*;


import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/07/2013
 */
public class Revision implements Iterable<OWLOntologyChangeRecord>, Comparable<Revision> {


    private UserId userId;

    private RevisionNumber revision;

    private long timestamp;

    private OWLOntologyChangeRecordList changes;

    private String highLevelDescription;

    public Revision(UserId userId, RevisionNumber revision, List<OWLOntologyChangeRecord> changes, long timestamp, String highLevelDescription) {
        this.changes = checkNotNull(new OWLOntologyChangeRecordList(changes));
        this.userId = checkNotNull(userId);
        this.revision = checkNotNull(revision);
        this.timestamp = timestamp;
        this.highLevelDescription = checkNotNull(highLevelDescription);
    }

    public Revision(RevisionNumber revision) {
        this.userId = UserId.getGuest();
        this.revision = revision;
        this.timestamp = 0;
        this.changes = new OWLOntologyChangeRecordList();
        this.highLevelDescription = "";
    }

    public int getSize() {
        return changes.size();
    }

    public static Revision createEmptyRevisionWithRevisionNumber(RevisionNumber revision) {
        return new Revision(revision);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UserId getUserId() {
        return userId;
    }

    public RevisionNumber getRevisionNumber() {
        return revision;
    }

    public int compareTo(Revision o) {
        return this.revision.compareTo(o.revision);
    }

    public String getHighLevelDescription() {
        return highLevelDescription != null ? highLevelDescription : "";
    }

    public Iterator<OWLOntologyChangeRecord> iterator() {
        return changes.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(highLevelDescription);
        sb.append("\n");
        for (OWLOntologyChangeRecord change : changes) {
            sb.append(change);
            sb.append("\n");
        }
        return sb.toString();
    }


}
