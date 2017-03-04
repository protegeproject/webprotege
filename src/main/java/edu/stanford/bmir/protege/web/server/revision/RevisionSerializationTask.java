package edu.stanford.bmir.protege.web.server.revision;

import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.change.OntologyChangeRecordList;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/05/2012
 */
public class RevisionSerializationTask implements Callable<Integer> {

    private final File file;

    private final Revision revision;

    public RevisionSerializationTask(File file, Revision revision) {
        this.file = file;
        this.revision = revision;
    }

    public Integer call() throws IOException {
        BinaryOWLMetadata metadata = new BinaryOWLMetadata();
        metadata.setStringAttribute(RevisionSerializationVocabulary.USERNAME_METADATA_ATTRIBUTE.getVocabularyName(), revision.getUserId().getUserName());
        metadata.setLongAttribute(RevisionSerializationVocabulary.REVISION_META_DATA_ATTRIBUTE.getVocabularyName(), revision.getRevisionNumber().getValue());
        metadata.setStringAttribute(RevisionSerializationVocabulary.DESCRIPTION_META_DATA_ATTRIBUTE.getVocabularyName(), revision.getHighLevelDescription());
        metadata.setStringAttribute(RevisionSerializationVocabulary.REVISION_TYPE_META_DATA_ATTRIBUTE.getVocabularyName(), RevisionType.EDIT.name());
        BinaryOWLOntologyChangeLog changeLog = new BinaryOWLOntologyChangeLog();
        changeLog.appendChanges(new OntologyChangeRecordList(revision.getTimestamp(), metadata, revision.getChanges()), file);
        return 0;
    }
}
