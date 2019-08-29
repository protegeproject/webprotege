package edu.stanford.bmir.protege.web.server.revision;

import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.change.OntologyChangeRecordList;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/05/2012
 */
public class RevisionSerializationTask implements Callable<Integer> {

    private final File file;

    private final Revision revision;

    private Runnable savedHook = () -> {};

    public RevisionSerializationTask(File file, Revision revision) {
        this.file = file;
        this.revision = revision;
    }

    public void setSavedHook(Runnable savedHook) {
        this.savedHook = checkNotNull(savedHook);
    }

    public Integer call() throws IOException {
        BinaryOWLMetadata metadata = new BinaryOWLMetadata();
        metadata.setStringAttribute(RevisionSerializationVocabulary.USERNAME_METADATA_ATTRIBUTE.getVocabularyName(), revision.getUserId().getUserName());
        metadata.setLongAttribute(RevisionSerializationVocabulary.REVISION_META_DATA_ATTRIBUTE.getVocabularyName(), revision.getRevisionNumber().getValue());
        metadata.setStringAttribute(RevisionSerializationVocabulary.DESCRIPTION_META_DATA_ATTRIBUTE.getVocabularyName(), revision.getHighLevelDescription());
        metadata.setStringAttribute(RevisionSerializationVocabulary.REVISION_TYPE_META_DATA_ATTRIBUTE.getVocabularyName(), RevisionType.EDIT.name());
        BinaryOWLOntologyChangeLog changeLog = new BinaryOWLOntologyChangeLog();
        var changeRecords = revision.getChanges()
                .stream()
                .map(OntologyChange::toOwlOntologyChangeRecord)
                .collect(toImmutableList());
        changeLog.appendChanges(new OntologyChangeRecordList(revision.getTimestamp(), metadata, changeRecords), file);
        savedHook.run();
        return 0;
    }
}
