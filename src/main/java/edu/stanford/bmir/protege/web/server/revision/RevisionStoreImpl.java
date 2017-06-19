package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import edu.stanford.bmir.protege.web.server.inject.project.ChangeHistoryFile;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.binaryowl.chunk.SkipSetting;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/05/15
 */
public class RevisionStoreImpl implements RevisionStore {

    private static final Logger logger = LoggerFactory.getLogger(RevisionStoreImpl.class);

    private final ExecutorService changeSerializationExucutor = Executors.newSingleThreadExecutor();

    private ImmutableList<Revision> revisions = ImmutableList.of();

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();


    private final ProjectId projectId;

    private final OWLDataFactory dataFactory;

    private final File changeHistoryFile;


    @Inject
    public RevisionStoreImpl(@Nonnull ProjectId projectId,
                             @Nonnull @ChangeHistoryFile File changeHistoryFile,
                             @Nonnull OWLDataFactory dataFactory) {
        this.projectId = checkNotNull(projectId);
        this.dataFactory = checkNotNull(dataFactory);
        this.changeHistoryFile = checkNotNull(changeHistoryFile);
    }

    @Nonnull
    @Override
    public Optional<Revision> getRevision(@Nonnull RevisionNumber revisionNumber) {
        if(revisions.isEmpty()) {
            return Optional.empty();
        }
        int index = getRevisionIndexForRevision(revisionNumber);
        if(index == -1) {
            return Optional.empty();
        }
        else {
            return Optional.of(revisions.get(index));
        }
    }

    @Nonnull
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

    @Nonnull
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
    public void addRevision(@Nonnull Revision revision) {
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
            if(!changeHistoryFile.exists()) {
                changeHistoryFile.getParentFile().mkdirs();
                return;
            }
            final ImmutableList.Builder<Revision> revisionsBuilder = ImmutableList.builder();
            try {
                Stopwatch stopwatch = Stopwatch.createStarted();
                BinaryOWLOntologyChangeLog changeLog = new BinaryOWLOntologyChangeLog();
                final BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(changeHistoryFile));
                final Interner<OWLAxiom> axiomInterner = Interners.newStrongInterner();
                final Interner<String> metadataInterner = Interners.newStrongInterner();
                changeLog.readChanges(inputStream, dataFactory, (list, skipSetting, l) -> {

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
                }, SkipSetting.SKIP_NONE);
                inputStream.close();
                stopwatch.stop();
                revisions = revisionsBuilder.build();
                logger.info("{} Change history loading complete.  Loaded {} revisions in {} ms.", projectId, revisions.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));

            } catch (Exception e) {
                logger.error("{} Failed to load change history for project.  Cause: {}", projectId, e.getMessage(), e);
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

    private void persistChanges(Revision revision) {
        try {
            writeLock.lock();
            RevisionSerializationTask revisionSerializationTask = new RevisionSerializationTask(changeHistoryFile, revision);
            if (revisions.size() != 1) {
                changeSerializationExucutor.submit(revisionSerializationTask);
            } else {
                // Save immediately
                try {
                    logger.info("{} Saving first revision of project", projectId);
                    revisionSerializationTask.call();
                } catch (IOException e) {
                    logger.error("{} An error occurred whilst saving the first revision of the project.  Cause: {}.", projectId, e.getMessage(), e);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }
}
