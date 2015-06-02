package edu.stanford.bmir.protege.web.server.owlapi.change;

import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.binaryowl.BinaryOWLChangeLogHandler;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.binaryowl.chunk.SkipSetting;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/05/15
 */
public class RevisionStoreImpl implements RevisionStore {

    private ExecutorService changeSerializationExucutor = Executors.newSingleThreadExecutor();


    private ImmutableList<Revision> revisions = ImmutableList.of();

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock readLock = readWriteLock.readLock();

    private Lock writeLock = readWriteLock.writeLock();


    private final ProjectId projectId;

    private final OWLDataFactory dataFactory;

    private final File changeHistoryFile;

    private final WebProtegeLogger logger;

    public RevisionStoreImpl(ProjectId projectId, OWLDataFactory dataFactory, File changeHistoryFile, WebProtegeLogger logger) {
        this.projectId = checkNotNull(projectId);
        this.dataFactory = checkNotNull(dataFactory);
        this.changeHistoryFile = checkNotNull(changeHistoryFile);
        this.logger = checkNotNull(logger);
    }

    @Override
    public Optional<Revision> getRevision(RevisionNumber revisionNumber) {
        if(revisions.isEmpty()) {
            return Optional.absent();
        }
        int index = getRevisionIndexForRevision(revisionNumber);
        if(index == -1) {
            return Optional.absent();
        }
        else {
            return Optional.of(revisions.get(index));
        }
    }

    @Override
    public RevisionNumber getCurrentRevisionNumber() {
        try {
            readLock.lock();
            if (revisions.isEmpty()) {
                return RevisionNumber.getRevisionNumber(0);
            }
            return revisions.get(revisions.size() - 1).getRevisionNumber();
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public ImmutableList<Revision> getRevisions() {
        try {
            readLock.lock();
            return revisions;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void addRevision(Revision revision) {
        checkNotNull(revision);
        try {
            writeLock.lock();
            if(revision.getRevisionNumber().compareTo(getCurrentRevisionNumber()) <= 0) {
                throw new IllegalArgumentException(String.format(
                        "Revision number (%d) must be greater than the current revision number (%d)",
                        revision.getRevisionNumber().getValue(),
                        getCurrentRevisionNumber().getValue()
                ));
            }
            ImmutableList.Builder<Revision> extendedList = ImmutableList.builder();
            extendedList.addAll(revisions);
            extendedList.add(revision);
            revisions = extendedList.build();
            persistChanges(revision);
        } finally {
            writeLock.unlock();
        }

    }

    public void load() {
        try {
            writeLock.lock();
            final ImmutableList.Builder<Revision> revisionsBuilder = ImmutableList.builder();
            try {
                Stopwatch stopwatch = Stopwatch.createStarted();
                BinaryOWLOntologyChangeLog changeLog = new BinaryOWLOntologyChangeLog();
                final BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(changeHistoryFile));
                final Interner<OWLAxiom> axiomInterner = Interners.newStrongInterner();
                final Interner<String> metadataInterner = Interners.newStrongInterner();
                changeLog.readChanges(inputStream, dataFactory, new BinaryOWLChangeLogHandler() {
                    public void handleChangesRead(OntologyChangeRecordList list, SkipSetting skipSetting, long l) {

                        BinaryOWLMetadata metadata = list.getMetadata();
                        String userName = metadataInterner.intern(metadata.getStringAttribute(RevisionSerializationVocabulary.USERNAME_METADATA_ATTRIBUTE.getVocabularyName(), ""));
                        Long revisionNumberValue = metadata.getLongAttribute(RevisionSerializationVocabulary.REVISION_META_DATA_ATTRIBUTE.getVocabularyName(), 0l);
                        RevisionNumber revisionNumber = RevisionNumber.getRevisionNumber(revisionNumberValue);

                        String description = metadataInterner.intern(metadata.getStringAttribute(RevisionSerializationVocabulary.DESCRIPTION_META_DATA_ATTRIBUTE.getVocabularyName(), ""));

//                        RevisionType type = RevisionType.valueOf(metadata.getStringAttribute(RevisionSerializationVocabulary.REVISION_TYPE_META_DATA_ATTRIBUTE.getVocabularyName(), RevisionType.EDIT.name()));

                        final UserId userId = UserId.getUserId(userName);
                        final List<OWLOntologyChangeRecord> changeRecords = internChangeRecords(list, axiomInterner);

                        Revision revision = new Revision(userId, revisionNumber, ImmutableList.copyOf(changeRecords), list.getTimestamp(), description);
                        revisionsBuilder.add(revision);
                    }
                }, SkipSetting.SKIP_NONE);
                inputStream.close();
                stopwatch.stop();
                revisions = revisionsBuilder.build();
                logger.info(projectId, "Change history loading complete.  Loaded %d revisions in %d ms", revisions.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));

            } catch (Exception e) {
                logger.severe(e);
                logger.info(projectId, "Failed to load change history.  Cause: " + e.getMessage());
            }
        } finally {
            writeLock.unlock();
        }


    }


    private int getRevisionIndexForRevision(RevisionNumber revision) {
        try {
            readLock.lock();
            if (revisions.isEmpty()) {
                return -1;
            }
            if (revision.isHead()) {
                return revisions.size() - 1;
            }
            Revision firstRevision = revisions.get(0);
            if (revision.compareTo(firstRevision.getRevisionNumber()) < 0) {
                return -1;
            }
            Revision lastRevision = revisions.get(revisions.size() - 1);
            if (lastRevision.getRevisionNumber() == revision) {
                return revisions.size() - 1;
            }
            Revision dummy = Revision.createEmptyRevisionWithRevisionNumber(revision);
            return Collections.binarySearch(revisions, dummy);
        } finally {
            readLock.unlock();
        }
    }


//    /**
//     * Gets an axiom interner that will ensure the same copies of axioms that are used between project ontologies
//     * and the change manager.
//     *
//     * @return The interner.  This should be disposed of as soon as possible.
//     */
//    private Interner<OWLAxiom> getAxiomInterner() {
//        final Interner<OWLAxiom> axiomInterner = Interners.newStrongInterner();
//        for (AxiomType<?> axiomType : AxiomType.AXIOM_TYPES) {
//            for (OWLAxiom ax : rootOntology.getAxioms(axiomType)) {
//                axiomInterner.intern(ax);
//            }
//        }
//        return axiomInterner;
//    }

    private List<OWLOntologyChangeRecord> internChangeRecords(OntologyChangeRecordList list, final Interner<OWLAxiom> axiomInterner) {
        List<OWLOntologyChangeRecord> result = new ArrayList<>();
        List<OWLOntologyChangeRecord> records = list.getChangeRecords();

        for (OWLOntologyChangeRecord record : records) {
            OWLOntologyID id = record.getOntologyID();
            OWLOntologyChangeData changeData = record.getData();
            OWLOntologyChangeData internedData = changeData.accept(new OWLOntologyChangeDataVisitor<OWLOntologyChangeData, RuntimeException>() {
                @Override
                public OWLOntologyChangeData visit(AddAxiomData data) throws RuntimeException {
                    final OWLAxiom ax = axiomInterner.intern(data.getAxiom());
                    if (ax != null) {
                        return new AddAxiomData(ax);
                    } else {
                        return data;
                    }
                }

                @Override
                public OWLOntologyChangeData visit(RemoveAxiomData data) throws RuntimeException {
                    return data;
                }

                @Override
                public OWLOntologyChangeData visit(AddOntologyAnnotationData data) throws RuntimeException {
                    return data;
                }

                @Override
                public OWLOntologyChangeData visit(RemoveOntologyAnnotationData data) throws RuntimeException {
                    return data;
                }

                @Override
                public OWLOntologyChangeData visit(SetOntologyIDData data) throws RuntimeException {
                    return data;
                }

                @Override
                public OWLOntologyChangeData visit(AddImportData data) throws RuntimeException {
                    return data;
                }

                @Override
                public OWLOntologyChangeData visit(RemoveImportData data) throws RuntimeException {
                    return data;
                }
            });
            OWLOntologyChangeRecord rec = new OWLOntologyChangeRecord(id, internedData);
            result.add(rec);

        }
        return result;
    }

//    private void logChangesInternal(UserId userId, List<? extends OWLOntologyChange> changes, String desc, RevisionType revisionType, boolean immediately) {
//        writeLock.lock();
//        try {
//            // Requires a read lock -
//            RevisionNumber revisionNumber = getCurrentRevision().getNextRevisionNumber();
//            long timestamp = System.currentTimeMillis();
//            final String highlevelDescription = desc != null ? desc : "";
//            List<OWLOntologyChangeRecord> records = new ArrayList<>(changes.size());
//            for (OWLOntologyChange change : changes) {
//                records.add(change.getChangeRecord());
//            }
//            final Revision revision = new Revision(userId, revisionNumber, new OWLOntologyChangeRecordList(records), timestamp, highlevelDescription);
//            addRevision(revision);
//
//            persistChanges(timestamp, revisionNumber, revisionType, userId, changes, highlevelDescription, immediately);
//        } finally {
//            writeLock.unlock();
//        }
//    }

    private void persistChanges(Revision revision) {
        try {
            writeLock.lock();
            RevisionSerializationTask revisionSerializationTask = new RevisionSerializationTask(changeHistoryFile, revision);
            if (revisions.size() != 1) {
                changeSerializationExucutor.submit(revisionSerializationTask);
            } else {
                // Save immediately
                try {
                    logger.info("Saving first revision of project " + projectId);
                    revisionSerializationTask.call();
                } catch (IOException e) {
                    logger.severe(e);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void handleCorruptChangeLog(BinaryOWLParseException e) {
        // The change log appears to be corrupt.  We somehow need a way of backing up the old log and creating
        // a fresh one.
        logger.severe(new RuntimeException("Corrupt change log", e));
    }

}
