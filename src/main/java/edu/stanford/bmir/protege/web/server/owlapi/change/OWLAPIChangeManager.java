package edu.stanford.bmir.protege.web.server.owlapi.change;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.EntityFrameWatch;
import edu.stanford.bmir.protege.web.shared.watches.HierarchyBranchWatch;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.binaryowl.BinaryOWLChangeLogHandler;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.binaryowl.chunk.SkipSetting;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2012
 */
public class OWLAPIChangeManager {

    private static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(OWLAPIChangeManager.class);

    public static final String USERNAME_METADATA_ATTRIBUTE = "username";

    public static final String REVISION_META_DATA_ATTRIBUTE = "revision";

    public static final String DESCRIPTION_META_DATA_ATTRIBUTE = "description";

    public static final String REVISION_TYPE_META_DATA_ATTRIBUTE = "revisionType";


    private final OWLAPIProject project;


    private List<Revision> revisions = new ArrayList<Revision>();


//    private ListMultimap<OWLEntity, Revision> entity2Revisions = ArrayListMultimap.create();


    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private ExecutorService changeSerializationExucutor = Executors.newSingleThreadExecutor();

    public OWLAPIChangeManager(OWLAPIProject project) {
        this.project = project;
        read();
    }

    /**
     * Only called from the constructor of this class
     */
    private void read() {
        revisions.clear();
        try {
            File changeHistoryFile = getChangeHistoryFile();
            if (!changeHistoryFile.exists()) {
                // Create it with the baseline?
                persistBaseline();
            }
            long t0 = System.currentTimeMillis();
            BinaryOWLOntologyChangeLog changeLog = new BinaryOWLOntologyChangeLog();
            final BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(changeHistoryFile));
            final Interner<OWLAxiom> axiomInterner = getAxiomInterner();
            final Interner<String> metadataInterner = Interners.newStrongInterner();
            final Interner<OWLOntologyID> ontologyIDInterner = Interners.newStrongInterner();
            changeLog.readChanges(inputStream, project.getDataFactory(), new BinaryOWLChangeLogHandler() {
                public void handleChangesRead(OntologyChangeRecordList list, SkipSetting skipSetting, long l) {

                    BinaryOWLMetadata metadata = list.getMetadata();
                    String userName = metadataInterner.intern(metadata.getStringAttribute(USERNAME_METADATA_ATTRIBUTE, ""));
                    Long revisionNumberValue = metadata.getLongAttribute(REVISION_META_DATA_ATTRIBUTE, 0l);
                    RevisionNumber revisionNumber = RevisionNumber.getRevisionNumber(revisionNumberValue);

                    String description = metadataInterner.intern(metadata.getStringAttribute(DESCRIPTION_META_DATA_ATTRIBUTE, ""));

                    RevisionType type = RevisionType.valueOf(metadata.getStringAttribute(REVISION_TYPE_META_DATA_ATTRIBUTE, RevisionType.EDIT.name()));

                    final UserId userId = UserId.getUserId(userName);
                    final List<OWLOntologyChangeRecord> changeRecords = internChangeRecords(list, axiomInterner, ontologyIDInterner);

                    Revision revision = new Revision(userId, revisionNumber, changeRecords, list.getTimestamp(), description, type);
                    addRevision(revision);
                }
            }, SkipSetting.SKIP_NONE);
            inputStream.close();
            long t1 = System.currentTimeMillis();
            LOGGER.info(project.getProjectId(), "Change history loading complete.  Loaded %d revisions in %d ms", revisions.size(), (t1 - t0));

        }
        catch (BinaryOWLParseException e) {
            handleCorruptChangeLog(e);
        }
        catch (EOFException e) {
            // Was the last record that we tried to read malformed?
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Gets an axiom interner that will ensure the same copies of axioms that are used between project ontologies
     * and the change manager.
     * @return The interner.  This should be disposed of as soon as possible.
     */
    private Interner<OWLAxiom> getAxiomInterner() {
        final Interner<OWLAxiom> axiomInterner = Interners.newStrongInterner();
        for (AxiomType<?> axiomType : AxiomType.AXIOM_TYPES) {
            for(OWLAxiom ax : project.getRootOntology().getAxioms(axiomType)) {
                axiomInterner.intern(ax);
            }
        }
        return axiomInterner;
    }

    private List<OWLOntologyChangeRecord> internChangeRecords(OntologyChangeRecordList list, final Interner<OWLAxiom> axiomInterner, Interner<OWLOntologyID> ontologyIDInterner) {
        List<OWLOntologyChangeRecord> result = new ArrayList<OWLOntologyChangeRecord>();
        List<OWLOntologyChangeRecord> records = list.getChangeRecords();

        for(OWLOntologyChangeRecord record : records) {
            OWLOntologyID id = record.getOntologyID();
            OWLOntologyChangeData changeData = record.getData();
            OWLOntologyChangeData internedData = changeData.accept(new OWLOntologyChangeDataVisitor<OWLOntologyChangeData, RuntimeException>() {
                @Override
                public OWLOntologyChangeData visit(AddAxiomData data) throws RuntimeException {
                    final OWLAxiom ax = axiomInterner.intern(data.getAxiom());
                    if (ax != null) {
                        return new AddAxiomData(ax);
                    }
                    else {
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


    /**
     * Adds a revision and performs the associated indexing.
     * @param revision The revision to be added.  Not {@code null}.
     */
    private void addRevision(Revision revision) {
        try {
            writeLock.lock();
            revisions.add(revision);
//            indexRevision(revision);
        }
        finally {
            writeLock.unlock();
        }
    }

//    /**
//     * Performs indexing of a revision (against its signature etc.).
//     * @param revision The revision.
//     */
//    private void indexRevision(Revision revision) {
//        try {
//            writeLock.lock();
//            for(OWLEntity entity : revision.getEntities(project)) {
//                entity2Revisions.put(entity, revision);
//            }
//        }
//        finally {
//            writeLock.unlock();
//        }
//    }

    private void persistBaseline() {
        try {
            // Sort the basline axioms in a nice order
            writeLock.lock();
            List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
            Set<AxiomType<?>> axiomTypes = new LinkedHashSet<AxiomType<?>>();
            axiomTypes.add(AxiomType.DECLARATION);
            axiomTypes.add(AxiomType.ANNOTATION_ASSERTION);
            axiomTypes.add(AxiomType.SUBCLASS_OF);
            axiomTypes.add(AxiomType.EQUIVALENT_CLASSES);
            axiomTypes.add(AxiomType.DISJOINT_CLASSES);
            axiomTypes.add(AxiomType.SUB_OBJECT_PROPERTY);
            axiomTypes.add(AxiomType.EQUIVALENT_OBJECT_PROPERTIES);
            axiomTypes.add(AxiomType.INVERSE_OBJECT_PROPERTIES);
            axiomTypes.add(AxiomType.OBJECT_PROPERTY_DOMAIN);
            axiomTypes.add(AxiomType.OBJECT_PROPERTY_RANGE);
            axiomTypes.add(AxiomType.TRANSITIVE_OBJECT_PROPERTY);
            axiomTypes.add(AxiomType.SYMMETRIC_OBJECT_PROPERTY);
            axiomTypes.add(AxiomType.ASYMMETRIC_OBJECT_PROPERTY);
            axiomTypes.add(AxiomType.FUNCTIONAL_OBJECT_PROPERTY);
            axiomTypes.add(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY);
            axiomTypes.add(AxiomType.REFLEXIVE_OBJECT_PROPERTY);
            axiomTypes.add(AxiomType.IRREFLEXIVE_OBJECT_PROPERTY);
            axiomTypes.add(AxiomType.DISJOINT_OBJECT_PROPERTIES);
            axiomTypes.add(AxiomType.SUB_DATA_PROPERTY);
            axiomTypes.add(AxiomType.EQUIVALENT_DATA_PROPERTIES);
            axiomTypes.add(AxiomType.DATA_PROPERTY_DOMAIN);
            axiomTypes.add(AxiomType.DATA_PROPERTY_RANGE);
            axiomTypes.add(AxiomType.FUNCTIONAL_DATA_PROPERTY);
            axiomTypes.add(AxiomType.CLASS_ASSERTION);
            axiomTypes.add(AxiomType.OBJECT_PROPERTY_ASSERTION);
            axiomTypes.add(AxiomType.DATA_PROPERTY_ASSERTION);

            // Mop Up
            for (AxiomType<?> type : AxiomType.AXIOM_TYPES) {
                if (!axiomTypes.contains(type)) {
                    axiomTypes.add(type);
                }
            }

            for (OWLOntology ontology : project.getRootOntology().getImportsClosure()) {
                for (OWLImportsDeclaration declaration : ontology.getImportsDeclarations()) {
                    changes.add(new AddImport(ontology, declaration));
                }
                for (OWLAnnotation annotation : ontology.getAnnotations()) {
                    changes.add(new AddOntologyAnnotation(ontology, annotation));
                }
            }
            for (AxiomType<?> axiomType : axiomTypes) {
                for (OWLOntology ontology : project.getRootOntology().getImportsClosure()) {
                    for (OWLAxiom axiom : ontology.getAxioms(axiomType)) {
                        changes.add(new AddAxiom(ontology, axiom));
                    }
                }
            }
            logChangesInternal(UserId.getUserId("system"), changes, "Initial import", RevisionType.BASELINE, true);
        }
        finally {
            writeLock.unlock();
        }
    }


    public void logChanges(UserId userId, List<? extends OWLOntologyChange> changes, String desc) {
        logChangesInternal(userId, changes, desc, RevisionType.EDIT, false);
    }


    private void logChangesInternal(UserId userId, List<? extends OWLOntologyChange> changes, String desc, RevisionType revisionType, boolean immediately) {
        writeLock.lock();
        try {
            // Requires a read lock -
            RevisionNumber revisionNumber = getCurrentRevision().getNextRevisionNumber();
            long timestamp = System.currentTimeMillis();
            final String highlevelDescription = desc != null ? desc : "";
            List<OWLOntologyChangeRecord> records = new ArrayList<OWLOntologyChangeRecord>(changes.size());
            for (OWLOntologyChange change : changes) {
                records.add(change.getChangeRecord());
            }
            final Revision revision = new Revision(userId, revisionNumber, records, timestamp, highlevelDescription, revisionType);
            addRevision(revision);

            persistChanges(timestamp, revisionNumber, revisionType, userId, changes, highlevelDescription, immediately);
//            fireProjectChangedEvent(userId, changes, revisionNumber, timestamp, revision);

        }
        finally {
            writeLock.unlock();
        }
    }


//    private void fireProjectChangedEvent(UserId userId, List<? extends OWLOntologyChange> changes, RevisionNumber revisionNumber, long timestamp, Revision revision) {
//        Set<OWLEntityData> changedEntitiesData = new HashSet<OWLEntityData>();
//        for (OWLEntity entity : revision.getEntities(project)) {
//            String browserText = project.getRenderingManager().getBrowserText(entity);
//            changedEntitiesData.add(DataFactory.getOWLEntityData(entity, browserText));
//        }
//        RevisionSummary revisionSummary = new RevisionSummary(revisionNumber, userId, timestamp, changes.size());
//        ProjectEvent<?> event = new ProjectChangedEvent(project.getProjectId(), revisionSummary, changedEntitiesData);
//        EventBusManager.getManager().postEvent(event);
//    }

    private void persistChanges(long timestamp, RevisionNumber revision, RevisionType type, UserId userId, List<? extends OWLOntologyChange> changes, String highlevelDescription, boolean immediately) {
        try {
            writeLock.lock();
            ChangeSerializationTask changeSerializationTask = new ChangeSerializationTask(getChangeHistoryFile(), userId, timestamp, revision, type, highlevelDescription, Collections.unmodifiableList(changes));
            if (!immediately) {
                changeSerializationExucutor.submit(changeSerializationTask);
            }
            else {
                try {
                    changeSerializationTask.call();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        finally {
            writeLock.unlock();
        }
    }


    private File getChangeHistoryFile() {
        OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(project.getProjectId());
        File file = documentStore.getChangeDataFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }


    private void handleCorruptChangeLog(BinaryOWLParseException e) {
        // The change log appears to be corrupt.  We somehow need a way of backing up the old log and creating
        // a fresh one.
        WebProtegeLoggerManager.get(OWLAPIChangeManager.class).severe(new RuntimeException("Corrupt change log", e));
    }

    public RevisionNumber getCurrentRevision() {
        readLock.lock();
        try {
            if (revisions.isEmpty()) {
                return RevisionNumber.getRevisionNumber(0);
            }
            Revision lastRevisionChangeList = revisions.get(revisions.size() - 1);
            return lastRevisionChangeList.getRevisionNumber();
        }
        finally {
            readLock.unlock();
        }
    }

    public OWLOntologyManager getOntologyManagerForRevision(RevisionNumber revision) {
        try {
            readLock.lock();
            int revisionIndex = getRevisionIndexForRevision(revision);
            if (revisionIndex == -1) {
                throw new IllegalArgumentException("Unknown revision: " + revision);
            }
            OWLOntologyManager manager = WebProtegeOWLManager.createOWLOntologyManager();
            for (int index = 0; index < revisionIndex + 1; index++) {
                Revision rev = revisions.get(index);
                for (OWLOntologyChangeRecord changeRecord : rev) {
                    OWLOntologyID ontologyID = changeRecord.getOntologyID();
                    if (!manager.contains(ontologyID)) {
                        manager.createOntology(ontologyID);
                    }
                    OWLOntologyChange change = changeRecord.createOntologyChange(manager);
                    manager.applyChange(change);
                }
            }
            if(manager.getOntologies().isEmpty()) {
                // No revisions exported.  Just create an empty ontology
                manager.createOntology(project.getRootOntology().getOntologyID());
            }
            return manager;
        }
        catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Problem creating ontology: " + e);
        }
        finally {
            readLock.unlock();
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
        }
        finally {
            readLock.unlock();
        }
    }

    private int getRevisionIndexForTimestamp(long timestamp) {
        if (revisions.isEmpty()) {
            return -1;
        }
        Revision firstRevision = revisions.get(0);
        if (timestamp < firstRevision.getTimestamp()) {
            return -1;
        }
        Revision lastRevision = revisions.get(revisions.size() - 1);
        if (timestamp > lastRevision.getTimestamp()) {
            return -revisions.size();
        }
        return Collections.binarySearch(revisions, Revision.createEmptyRevisionWithTimestamp(timestamp), new Revision.RevisionTimeStampComparator());
    }

    private int getRevisionFloorIndexForTimestamp(long timestamp) {
        int index = getRevisionIndexForTimestamp(timestamp);
        if (index >= 0) {
            return index;
        }
        int insertionIndex = -index - 1;
        return insertionIndex - 1;
    }

    public List<ChangeData> getChangeDataForWatches(Set<Watch<?>> watches) {
        Set<OWLEntity> superEntities = new HashSet<OWLEntity>();
        Set<OWLEntity> directWatches = new HashSet<OWLEntity>();
        for (Watch<?> watch : watches) {
            if (watch instanceof HierarchyBranchWatch) {
                OWLEntity entity = ((HierarchyBranchWatch) watch).getEntity();
                superEntities.add(entity);
                directWatches.add(entity);
            }
            if (watch instanceof EntityFrameWatch) {
                directWatches.add(((EntityFrameWatch) watch).getEntity());
            }
        }
        if (superEntities.isEmpty() || directWatches.isEmpty()) {
            return Collections.emptyList();
        }

        List<ChangeData> result = new ArrayList<ChangeData>();

        List<Revision> revisionsCopy = getRevisionsCopy();
        for (Revision revision : revisionsCopy) {
            if (revision.getRevisionType() != RevisionType.BASELINE) {
                if (isWatchedRevision(superEntities, directWatches, revision)) {
                    for (OWLEntity entity : revision.getEntities(project)) {
                        ChangeData changeData = createChangeDataFromRevision(entity, revision);
                        result.add(changeData);
                    }
                }
            }
        }
        return result;
    }

    private boolean isWatchedRevision(final Set<OWLEntity> superEntities, Set<OWLEntity> directWatches, Revision revision) {
        Set<OWLEntity> entities = revision.getEntities(project);
        for (OWLEntity entity : entities) {
            if (directWatches.contains(entity)) {
                return true;
            }
            boolean watchedByAncestor = isWatchedByAncestor(superEntities, entity);
            if (watchedByAncestor) {
                return true;
            }
        }
        return false;
    }

    private Boolean isWatchedByAncestor(final Set<OWLEntity> watchedAncestors, OWLEntity entity) {
        return entity.accept(new OWLEntityVisitorEx<Boolean>() {
            @Override
            public Boolean visit(OWLClass cls) {
                final Set<? extends OWLEntity> ancestors = project.getClassHierarchyProvider().getAncestors(cls);
                return isWatchedByAncestor(ancestors);
            }


            @Override
            public Boolean visit(OWLObjectProperty property) {
                return isWatchedByAncestor(project.getObjectPropertyHierarchyProvider().getAncestors(property));
            }

            @Override
            public Boolean visit(OWLDataProperty property) {
                return isWatchedByAncestor(project.getDataPropertyHierarchyProvider().getAncestors(property));
            }

            @Override
            public Boolean visit(OWLNamedIndividual individual) {
                final Set<OWLClassExpression> types = individual.getTypes(project.getRootOntology().getImportsClosure());
                for (OWLClassExpression ce : types) {
                    if (!ce.isAnonymous()) {
                        if (isWatchedByAncestor(project.getClassHierarchyProvider().getAncestors(ce.asOWLClass()))) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public Boolean visit(OWLDatatype datatype) {
                return false;
            }

            @Override
            public Boolean visit(OWLAnnotationProperty property) {
                return isWatchedByAncestor(project.getAnnotationPropertyHierarchyProvider().getAncestors(property));
            }

            private Boolean isWatchedByAncestor(Set<? extends OWLEntity> ancestors) {
                for (OWLEntity anc : ancestors) {
                    if (watchedAncestors.contains(anc)) {
                        return true;
                    }
                }
                return false;
            }

        });
    }

    private List<Revision> getRevisionsCopy() {
        try {
            readLock.lock();
            return new ArrayList<Revision>(revisions);
        }
        finally {
            readLock.unlock();
        }
    }


    public List<ChangeData> getChangeDataInTimestampInterval(long fromTimestamp, long toTimestamp, RevisionType revisionType) {
        List<Revision> revisions = getRevisionsCopy();
        List<ChangeData> result = new ArrayList<ChangeData>();
        for (Revision revision : revisions) {
            if (revision.getRevisionType() == RevisionType.EDIT || revisionType == RevisionType.BASELINE) {
                long ts = revision.getTimestamp();
                if (ts >= fromTimestamp && ts <= toTimestamp) {
                    for (OWLEntity entity : revision.getEntities(project)) {
                        result.add(createChangeDataFromRevision(entity, revision));
                    }
                }
            }
        }
        return result;
    }

    private ChangeData createChangeDataFromRevision(OWLEntity entity, Revision changeList) {
        EntityData entityData = project.getRenderingManager().getEntityData(entity);
        return new ChangeData(entityData, changeList.getUserId().getUserName(), changeList.getHighLevelDescription(project), new Date(changeList.getTimestamp()));

    }

    public List<ChangeData> getChangeDataForEntitiesInTimeStampInterval(Set<OWLEntity> entites, long fromTimestamp, long toTimestamp) {
        List<Revision> revisions = getRevisionsCopy();

        List<ChangeData> result = new ArrayList<ChangeData>();
        for (Revision changeList : revisions) {
            long ts = changeList.getTimestamp();
            if (ts >= fromTimestamp && ts <= toTimestamp) {
                Set<OWLEntity> changeEntities = changeList.getEntities(project);
                for (OWLEntity entity : entites) {
                    if (changeEntities.contains(entity)) {
                        EntityData entityData = project.getRenderingManager().getEntityData(entity);
                        result.add(new ChangeData(entityData, changeList.getUserId().getUserName(), changeList.getHighLevelDescription(project, entity), new Date(changeList.getTimestamp())));
                    }
                }
            }
        }
        return result;
    }

    public int getChangeSetCount(long fromTimestamp, long toTimestamp) {
        try {
            readLock.lock();
            int count = 0;
            for (Revision changeList : revisions) {
                long ts = changeList.getTimestamp();
                if (ts >= fromTimestamp && ts <= toTimestamp) {
                    count++;
                }
            }
            return count;
        }
        finally {
            readLock.unlock();
        }
    }

    public RevisionSummary getRevisionSummary(RevisionNumber revisionNumber) {
        int index = getRevisionIndexForRevision(revisionNumber);
        if (index == -1) {
            throw new RuntimeException("Unknown revision: " + revisionNumber);
        }
        Revision revision = revisions.get(index);
        return getRevisionSummaryFromRevision(revision);
    }


    public List<RevisionSummary> getRevisionSummaries() {
        List<RevisionSummary> result = new ArrayList<RevisionSummary>();
        try {
            readLock.lock();
            for (Revision revision : revisions) {
                result.add(getRevisionSummaryFromRevision(revision));
            }
        }
        finally {
            readLock.unlock();
        }
        return result;
    }

    private RevisionSummary getRevisionSummaryFromRevision(Revision revision) {
        return new RevisionSummary(revision.getRevisionNumber(), revision.getUserId(), revision.getTimestamp(), revision.getSize());
    }

}
