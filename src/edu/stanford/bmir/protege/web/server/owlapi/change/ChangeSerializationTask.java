package edu.stanford.bmir.protege.web.server.owlapi.change;

import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/05/2012
 */
public class ChangeSerializationTask implements Callable<Integer> {

    private File file;

    private UserId userId;

    private long timestamp;

    private RevisionNumber revisionNumber;
    
    private String highlevelDescription;

    private List<OWLOntologyChange> changes;

    private RevisionType type;

    public ChangeSerializationTask(File file, UserId userId, long timestamp, RevisionNumber revisionNumber, RevisionType type, String highlevelDescription, List<OWLOntologyChange> changes) {
        this.file = file;
        this.type = type;
        this.userId = userId;
        this.timestamp = timestamp;
        this.revisionNumber = revisionNumber;
        this.highlevelDescription = highlevelDescription == null ? "" : highlevelDescription;
        this.changes = new ArrayList<OWLOntologyChange>(changes);
    }

    public Integer call() throws IOException {
        BinaryOWLMetadata metadata = new BinaryOWLMetadata();
        metadata.setStringAttribute(OWLAPIChangeManager.USERNAME_METADATA_ATTRIBUTE, userId.getUserName());
        metadata.setLongAttribute(OWLAPIChangeManager.REVISION_META_DATA_ATTRIBUTE, revisionNumber.getValue());
        metadata.setStringAttribute(OWLAPIChangeManager.DESCRIPTION_META_DATA_ATTRIBUTE, highlevelDescription);
        metadata.setStringAttribute(OWLAPIChangeManager.REVISION_TYPE_META_DATA_ATTRIBUTE, type.name());
        BinaryOWLOntologyChangeLog changeLog = new BinaryOWLOntologyChangeLog();
        changeLog.appendChanges(Collections.unmodifiableList(changes), timestamp, metadata, file);
        return 0;
    }
}
