package edu.stanford.bmir.protege.web.server.owlapi.change;

import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.shared.watches.EntityFrameWatch;
import edu.stanford.bmir.protege.web.shared.watches.HierarchyBranchWatch;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.owlapi.binaryowl.BinaryOWLChangeLogHandler;
import org.semanticweb.owlapi.binaryowl.BinaryOWLMetadata;
import org.semanticweb.owlapi.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.owlapi.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.owlapi.binaryowl.chunk.SkipSetting;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;

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
            changeLog.readChanges(new BufferedInputStream(new FileInputStream(changeHistoryFile)), project.getDataFactory(), new BinaryOWLChangeLogHandler() {
                public void handleChangesRead(OntologyChangeRecordList list, SkipSetting skipSetting, long l) {
                    BinaryOWLMetadata metadata = list.getMetadata();
                    String userName = metadata.getStringAttribute(USERNAME_METADATA_ATTRIBUTE, "");
                    Long revisionNumberValue = metadata.getLongAttribute(REVISION_META_DATA_ATTRIBUTE, 0l);
                    RevisionNumber revisionNumber = RevisionNumber.getRevisionNumber(revisionNumberValue);

                    String description = metadata.getStringAttribute(DESCRIPTION_META_DATA_ATTRIBUTE, "");

                    RevisionType type = RevisionType.valueOf(metadata.getStringAttribute(REVISION_TYPE_META_DATA_ATTRIBUTE, RevisionType.EDIT.name()));

                    final UserId userId = UserId.getUserId(userName);
                    final List<OWLOntologyChangeRecord> changeRecords = list.getChangeRecords();

                    Revision chgList = new Revision(userId, revisionNumber, changeRecords, list.getTimestamp(), description, type);
                    revisions.add(chgList);
                }
            }, SkipSetting.SKIP_NONE);
            long t1 = System.currentTimeMillis();
            LOGGER.info("Loaded " + revisions.size() + " changes in " + (t1 - t0));
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
            for (AxiomType type : AxiomType.AXIOM_TYPES) {
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

            logChangesInternal(UserId.getGuest(), changes, "Initial import", RevisionType.BASELINE, true);
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
            RevisionNumber revision = getCurrentRevision().getNextRevisionNumber();
            long timestamp = System.currentTimeMillis();
            final String highlevelDescription = desc != null ? desc : "";
            List<OWLOntologyChangeRecord> records = new ArrayList<OWLOntologyChangeRecord>(changes.size());
            for (OWLOntologyChange change : changes) {
                records.add(change.getChangeRecord());
            }
            revisions.add(new Revision(userId, revision, records, timestamp, highlevelDescription, revisionType));
            persistChanges(timestamp, revision, revisionType, userId, changes, highlevelDescription, immediately);
        }
        finally {
            writeLock.unlock();
        }
    }

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

    private List<Revision> getRevisionsSinceVersion(RevisionNumber version) {
        readLock.lock();
        try {
            int index = getRevisionIndexForRevision(version);
            if (index < 0) {
                return Collections.emptyList();
            }

            return new ArrayList<Revision>(revisions.subList(index, revisions.size()));
        }
        finally {
            readLock.unlock();
        }
    }

    public boolean isValidRevision(RevisionNumber revision) {
        return getRevisionIndexForRevision(revision) != -1;
    }
    
    public OWLOntologyManager getOntologyManagerForRevision(RevisionNumber revision) {
        try {
            readLock.lock();
            int revisionIndex = getRevisionIndexForRevision(revision);
            if(revisionIndex == -1) {
                throw new IllegalArgumentException("Unknown revision: " + revision);
            }
            OWLOntologyManager manager = WebProtegeOWLManager.createOWLOntologyManager();
            for(int index = 0; index < revisionIndex + 1; index++) {
                Revision rev = revisions.get(index);
                List<OWLOntologyChangeRecord> changeRecords = rev.getChanges();
                for(OWLOntologyChangeRecord changeRecord : changeRecords) {
                    OWLOntologyID ontologyID = changeRecord.getOntologyID();
                    if(!manager.contains(ontologyID)) {
                        manager.createOntology(ontologyID);
                    }
                    OWLOntologyChange change = changeRecord.createOntologyChange(manager);
                    manager.applyChange(change);
                }
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
            if(revision.isHead()) {
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
        if (timestamp > lastRevision.timestamp) {
            return -revisions.size();
        }
        return Collections.binarySearch(revisions, Revision.createEmptyRevisionWithTimestamp(timestamp), new RevisionTimeStampComparator());
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
        for(Watch<?> watch : watches) {
            if(watch instanceof HierarchyBranchWatch) {
                OWLEntity entity = ((HierarchyBranchWatch) watch).getEntity();
                superEntities.add(entity);
                directWatches.add(entity);
            }
            if(watch instanceof EntityFrameWatch) {
                directWatches.add(((EntityFrameWatch) watch).getEntity());
            }
        }
        List<ChangeData> result = new ArrayList<ChangeData>();
        List<Revision> revisionsCopy = getRevisionsCopy();
        for(Revision revision : revisionsCopy) {
            if (revision.getRevisionType() != RevisionType.BASELINE) {
                if(isWatchedRevision(superEntities, directWatches, revision)) {
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
        for(OWLEntity entity : entities) {
            if(directWatches.contains(entity)) {
                return true;
            }
            boolean watchedByAncestor = isWatchedByAncestor(superEntities, entity);
            if(watchedByAncestor) {
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
                    for(OWLClassExpression ce : types) {
                        if(!ce.isAnonymous()) {
                            if(isWatchedByAncestor(project.getClassHierarchyProvider().getAncestors(ce.asOWLClass()))) {
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
                    for(OWLEntity anc : ancestors) {
                        if(watchedAncestors.contains(anc)) {
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


    public List<ChangeData> getChangeDataInTimestampInterval(long fromTimestamp, long toTimestamp) {
        try {
            readLock.lock();
            List<ChangeData> result = new ArrayList<ChangeData>();
            for (Revision changeList : revisions) {
                long ts = changeList.getTimestamp();
                if (ts >= fromTimestamp && ts <= toTimestamp) {
                    for (OWLEntity entity : changeList.getEntities(project)) {
                        result.add(createChangeDataFromRevision(entity, changeList));
                    }
                }
            }
            return result;
        }
        finally {
            readLock.unlock();
        }
    }

    private ChangeData createChangeDataFromRevision(OWLEntity entity, Revision changeList) {
            EntityData entityData = project.getRenderingManager().getEntityData(entity);
            return new ChangeData(entityData, changeList.getUserId().getUserName(), changeList.getHighLevelDescription(project), new Date(changeList.getTimestamp()));

    }

    public List<ChangeData> getChangeDataForEntitiesInTimeStampInterval(Set<OWLEntity> entites, long fromTimestamp, long toTimestamp) {
        readLock.lock();
        // TODO:
        try {
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
        finally {
            readLock.unlock();
        }
    }

//    public List<OntologyEvent> getOntologyEventsSinceRevisionNumber(RevisionNumber revisionNumber) {
//        readLock.lock();
//        try {
//            List<OntologyEvent> result = new ArrayList<OntologyEvent>();
//            RevisionNumber nextRevisionNumber = revisionNumber.getNextRevisionNumber();
//            List<Revision> revisions = getRevisionsSinceVersion(nextRevisionNumber);
//            for (Revision revision : revisions) {
//                OWLOntologyChangeTranslator translator = new OWLOntologyChangeTranslator(revision.getUserId(), revision.getRevisionNumber());
//                for (OWLOntologyChangeRecord change : revision) {
//                    OntologyEvent event = change.getData().accept(translator);
//                    if (event != null) {
//                        result.add(event);
//                    }
//                }
//                getBrowserTextChanges(result, revision);
//            }
//            return result;
//        }
//        finally {
//            readLock.unlock();
//        }
//    }

//    private void getBrowserTextChanges(List<OntologyEvent> resultToFill, Revision revision) {
//        OWLAPIEntityEditorKit editorKit = project.getOWLEntityEditorKit();
//        List<OWLEntityBrowserTextChangeSet> browserTextChanges = editorKit.getChangedEntities(revision.getChanges());
//        for (OWLEntityBrowserTextChangeSet changeSet : browserTextChanges) {
//            EntityData entityData = project.getRenderingManager().getEntityData(changeSet.getEntity());
//            resultToFill.add(new EntityBrowserTextChangedEvent(changeSet.getEntity(), entityData, revision.getUserId().getUserName(), revision.getRevisionNumber().getValueAsInt()));
//        }
//    }

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
        if(index == -1) {
            throw new RuntimeException("Unknown revision: " + revisionNumber);
        }
        Revision revision = revisions.get(index);
        return getRevisionSummaryFromRevision(revision);
    }

    
    public List<RevisionSummary> getRevisionSummaries() {
        List<RevisionSummary> result = new ArrayList<RevisionSummary>();
        try {
            readLock.lock();
            for(Revision revision : revisions) {
                result.add(getRevisionSummaryFromRevision(revision));
            }
        }
        finally {
            readLock.unlock();
        }
        return result;
    }

    private RevisionSummary getRevisionSummaryFromRevision(Revision revision) {
        return new RevisionSummary(revision.getRevisionNumber(), revision.getUserId(), revision.getTimestamp(), revision.getChanges().size());
    }


    private static class Revision implements Iterable<OWLOntologyChangeRecord>, Comparable<Revision> {

        private UserId userId;

        private RevisionNumber revision;

        private long timestamp;

        private List<OWLOntologyChangeRecord> changes = new ArrayList<OWLOntologyChangeRecord>();

        private String highLevelDescription;

        private RevisionType revisionType;

        private Revision(UserId userId, RevisionNumber revision, List<OWLOntologyChangeRecord> changes, long timestamp, String highLevelDescription, RevisionType revisionType) {
            this.changes.addAll(changes);
            this.userId = userId;
            this.revision = revision;
            this.timestamp = timestamp;
            this.highLevelDescription = highLevelDescription;
            this.revisionType = revisionType;
        }

        private Revision(RevisionNumber revision) {
            this.userId = UserId.getGuest();
            this.revision = revision;
            this.timestamp = 0;
            this.changes = Collections.emptyList();
            this.highLevelDescription = "";
            this.revisionType = RevisionType.EDIT;
        }

        public int getSize() {
            return changes.size();
        }

        public static Revision createEmptyRevisionWithRevisionNumber(RevisionNumber revision) {
            return new Revision(revision);
        }

        public static Revision createEmptyRevisionWithTimestamp(long timestamp) {
            Revision revision = new Revision(RevisionNumber.getRevisionNumber(0));
            revision.timestamp = timestamp;
            return revision;
        }

        public Set<OWLEntity> getEntities(OWLAPIProject project) {
            Set<OWLEntity> result = new HashSet<OWLEntity>();
            for (OWLOntologyChangeRecord change : changes) {
                if (change.getData() instanceof AxiomChangeData) {
                    OWLAxiom ax = ((AxiomChangeData) change.getData()).getAxiom();
                    AxiomSubjectProvider axiomSubjectProvider = new AxiomSubjectProvider();
                    OWLObject object = axiomSubjectProvider.getSubject(ax);
                    if (object instanceof OWLEntity) {
                        result.add((OWLEntity) object);
                    }
                    else if (object instanceof IRI) {
                        result.addAll(project.getRootOntology().getEntitiesInSignature((IRI) object));
                    }
                }
            }
            return result;
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

        public RevisionType getRevisionType() {
            return revisionType;
        }

        public int compareTo(Revision o) {
            return this.revision.compareTo(o.revision);
        }

        public String getHighLevelDescription(final OWLAPIProject project, OWLEntity entity) {
            StringBuilder sb = new StringBuilder();
            if (highLevelDescription != null) {
                sb.append(highLevelDescription);
            }
            sb.append("<div style=\"width: 100%;\">");
            sb.append("<div><b>Details:</b></div>");
            sb.append("<div style=\"margin-left: 20px\">");
            for (final OWLOntologyChangeRecord changeRecord : changes) {
                if (changeRecord.getData() instanceof AxiomChangeData) {
                    AxiomChangeData info = (AxiomChangeData) changeRecord.getData();
                    OWLAxiom axiom = info.getAxiom();
                    if (entity == null || isEntitySubjectOfChange(entity, info)) {
                        sb.append("<div style=\"overflow: hidden;\">");
                        String ren = changeRecord.getData().accept(new OWLOntologyChangeDataVisitor<String, RuntimeException>() {

                            public String visit(AddAxiomData addAxiom) {
                                final RenderingManager rm = project.getRenderingManager();
                                return new StringBuilder().append("<b>Added: </b> ").append(rm.getBrowserText(addAxiom.getAxiom())).toString();
                            }

                            public String visit(RemoveAxiomData removeAxiom) {
                                final RenderingManager rm = project.getRenderingManager();
                                return new StringBuilder().append("<b>Removed: </b> ").append(rm.getBrowserText(removeAxiom.getAxiom())).toString();
                            }

                            public String visit(SetOntologyIDData setOntologyID) {
                                return new StringBuilder().append("Changed ontology id from ").append(changeRecord.getOntologyID()).append(" to ").append(setOntologyID.getNewId()).toString();
                            }

                            public String visit(AddImportData addImport) {
                                return new StringBuilder().append("Added import: ").append(addImport.getDeclaration().getIRI().toQuotedString()).toString();
                            }

                            public String visit(RemoveImportData removeImport) {
                                return new StringBuilder().append("Removed import: ").append(removeImport.getDeclaration().getIRI().toQuotedString()).toString();
                            }

                            public String visit(AddOntologyAnnotationData addOntologyAnnotation) {
                                return new StringBuilder().append("Added annotation to ontology: ").append(project.getRenderingManager().getBrowserText(addOntologyAnnotation.getAnnotation())).toString();
                            }

                            public String visit(RemoveOntologyAnnotationData removeOntologyAnnotation) {
                                return new StringBuilder().append("Removed annotation from ontology: ").append(project.getRenderingManager().getBrowserText(removeOntologyAnnotation.getAnnotation())).toString();
                            }
                        });
                        sb.append(ren);
                        sb.append("</div>");

                    }
                }


            }
            sb.append("</div>");
            sb.append("</div>");
            return sb.toString();
        }

        private boolean isEntitySubjectOfChange(OWLEntity entity, AxiomChangeData change) {
            OWLObject changeSubject = getChangeSubject(change);
            return changeSubject.equals(entity) || (changeSubject != null && changeSubject.equals(entity.getIRI()));
        }

        private OWLObject getChangeSubject(AxiomChangeData change) {
            AxiomSubjectProvider subjectProvider = new AxiomSubjectProvider();

            return subjectProvider.getSubject(change.getAxiom());
        }

        public String getHighLevelDescription(final OWLAPIProject project) {
            return getHighLevelDescription(project, null);
        }

        public List<OWLOntologyChangeRecord> getChanges() {
            return changes;
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


    private class RevisionTimeStampComparator implements Comparator<Revision> {

        public int compare(Revision o1, Revision o2) {
            if (o1.timestamp < o2.timestamp) {
                return -1;
            }
            else if (o1.timestamp == o2.timestamp) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }


//    private class OWLOntologyChangeTranslator implements OWLOntologyChangeDataVisitor<OntologyEvent, RuntimeException> {
//
//        private UserId userId;
//
//        private RevisionNumber revisionNumber;
//
//        private OWLOntologyChangeTranslator(UserId userId, RevisionNumber revision) {
//            this.userId = userId;
//            this.revisionNumber = revision;
//        }
//
//        public OntologyEvent visit(AddAxiomData addAxiom) {
//            EntityData sourceEntityData = getAxiomChangeSubject(addAxiom);
//            if (sourceEntityData == null) {
//                return null;
//            }
//            ChangeType changeType = ChangeType.ADDED;
//            return getEventType(addAxiom, changeType, userId, revisionNumber.getValueAsInt());
//
////            return new AbstractOntologyEvent(sourceEntityData, eventType != null ? eventType : -1, userId.getUserName(), (int) revision);
//        }
//
//
//        public OntologyEvent visit(RemoveAxiomData removeAxiom) {
//            EntityData sourceEntityData = getAxiomChangeSubject(removeAxiom);
//            if (sourceEntityData == null) {
//                return null;
//            }
//            ChangeType changeType = ChangeType.REMOVED;
//            return  getEventType(removeAxiom, changeType, userId, revisionNumber.getValueAsInt());
////            return new AbstractOntologyEvent(sourceEntityData, eventType != null ? eventType : -1, userId.getUserName(), (int) revision);
//        }
//
//        public OntologyEvent visit(SetOntologyIDData setOntologyID) {
//            return null;
//        }
//
//        public OntologyEvent visit(AddImportData addImport) {
//            return null;
//        }
//
//        public OntologyEvent visit(RemoveImportData removeImport) {
//            return null;
//        }
//
//        public OntologyEvent visit(AddOntologyAnnotationData addOntologyAnnotation) {
//            return null;
//        }
//
//        public OntologyEvent visit(RemoveOntologyAnnotationData removeOntologyAnnotation) {
//            return null;
//        }
//
//
//        private EntityData getAxiomChangeSubject(AxiomChangeData addAxiom) {
//            AxiomSubjectProvider subjectProvider = new AxiomSubjectProvider();
//            OWLObject object = subjectProvider.getSubject(addAxiom.getAxiom());
//            return object.accept(new OWLObjectVisitorExAdapter<EntityData>() {
//                @Override
//                public EntityData visit(OWLClass desc) {
//                    return project.getRenderingManager().getEntityData(desc);
//                }
//
//                @Override
//                public EntityData visit(OWLObjectProperty property) {
//                    return project.getRenderingManager().getEntityData(property);
//                }
//
//                @Override
//                public EntityData visit(OWLDataProperty property) {
//                    return project.getRenderingManager().getEntityData(property);
//                }
//
//                @Override
//                public EntityData visit(OWLAnnotationProperty property) {
//                    return project.getRenderingManager().getEntityData(property);
//                }
//
//                @Override
//                public EntityData visit(OWLNamedIndividual individual) {
//                    return project.getRenderingManager().getEntityData(individual);
//                }
//
//                @Override
//                public EntityData visit(IRI iri) {
//                    Set<OWLEntity> entities = project.getRootOntology().getEntitiesInSignature(iri);
//                    if (!entities.isEmpty()) {
//                        OWLEntity entity = RenderingManager.selectEntity(entities);
//                        return project.getRenderingManager().getEntityData(entity);
//                    }
//                    else {
//                        return project.getRenderingManager().getEntityData(iri.toString(), EntityType.CLASS);
//                    }
//                }
//            });
//        }
//    }


//    private OntologyEvent getEventType(AxiomChangeData change, ChangeType changeType, UserId userId, int revision) {
//        OntologyEventTranslator translator = new OntologyEventTranslator(changeType, userId, revision);
//        return change.getAxiom().accept(translator);
//    }


    private enum ChangeType {
        ADDED,

        REMOVED
    }

//    private class OntologyEventTranslator implements OWLAxiomVisitorEx<OntologyEvent> {
//
//        private ChangeType changeType;
//
//        private UserId userId;
//
//        private int revision;
//
//        private OntologyEventTranslator(ChangeType changeType, UserId userId, int revision) {
//            this.changeType = changeType;
//            this.userId = userId;
//            this.revision = revision;
//        }
//
//        public OntologyEvent visit(OWLSubClassOfAxiom axiom) {
//            if(axiom.getSubClass().isAnonymous() || axiom.getSuperClass().isAnonymous()) {
//                return null;
//            }
//            OWLClass subCls = axiom.getSubClass().asOWLClass();
//            EntityData subClsData = project.getRenderingManager().getEntityData(subCls);
//            OWLClass superCls = axiom.getSuperClass().asOWLClass();
//            EntityData superClsData = project.getRenderingManager().getEntityData(superCls);
//
//            // Bizarely, this would be fired as an EntityCreateEvent or an EntityDeleteEvent
//            if (changeType == ChangeType.ADDED) {
//                // Seems to be back to front - subs and supers swapped
//                return new EntityCreateEvent(superClsData, EventType.SUBCLASS_ADDED, userId.getUserName(), Arrays.asList(subClsData), revision);
//            }
//            else {
//                // Seems to be back to front - subs and supers swapped
//                return new EntityDeleteEvent(subClsData, EventType.SUBCLASS_REMOVED, userId.getUserName(), Arrays.asList(superClsData), revision);
//            }
//        }
//
//        public OntologyEvent visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLAsymmetricObjectPropertyAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLReflexiveObjectPropertyAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLDisjointClassesAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLDataPropertyDomainAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLObjectPropertyDomainAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLEquivalentObjectPropertiesAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLDifferentIndividualsAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLDisjointDataPropertiesAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLDisjointObjectPropertiesAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLObjectPropertyRangeAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLObjectPropertyAssertionAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLFunctionalObjectPropertyAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLSubObjectPropertyOfAxiom axiom) {
//            if(axiom.getSubProperty().isAnonymous() || axiom.getSuperProperty().isAnonymous()) {
//               return null;
//            }
//            final EntityData subPropData = project.getRenderingManager().getEntityData(axiom.getSubProperty().asOWLObjectProperty());
//            final EntityData superPropData = project.getRenderingManager().getEntityData(axiom.getSuperProperty().asOWLObjectProperty());
//
//            if (changeType == ChangeType.ADDED) {
//                return new EntityCreateEvent(superPropData, EventType.SUBPROPERTY_ADDED, userId.getUserName(), Arrays.<EntityData>asList(subPropData), revision);
//            }
//            else {
//                return new EntityDeleteEvent(superPropData, EventType.SUBPROPERTY_REMOVED, userId.getUserName(), Arrays.<EntityData>asList(subPropData), revision);
//            }
//        }
//
//        public OntologyEvent visit(OWLDisjointUnionAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLDeclarationAxiom axiom) {
//            final EntityData entityData = project.getRenderingManager().getEntityData(axiom.getEntity());
//            if (changeType == ChangeType.ADDED) {
//
//                int type = axiom.getEntity().accept(new OWLEntityVisitorEx<Integer>() {
//                    public Integer visit(OWLClass cls) {
//                        return EventType.CLASS_CREATED;
//                    }
//
//                    public Integer visit(OWLObjectProperty property) {
//                        return EventType.PROPERTY_CREATED;
//                    }
//
//                    public Integer visit(OWLDataProperty property) {
//                        return EventType.PROPERTY_CREATED;
//                    }
//
//                    public Integer visit(OWLNamedIndividual individual) {
//                        return EventType.INDIVIDUAL_CREATED;
//                    }
//
//                    public Integer visit(OWLDatatype datatype) {
//                        return EventType.PROPERTY_CREATED;
//                    }
//
//                    public Integer visit(OWLAnnotationProperty property) {
//                        return EventType.PROPERTY_CREATED;
//                    }
//                });
//                return new EntityCreateEvent(entityData, type, userId.getUserName(), Collections.<EntityData>emptyList(), revision);
//            }
//            else {
//                int eventType = axiom.getEntity().accept(new OWLEntityVisitorEx<Integer>() {
//                    public Integer visit(OWLClass cls) {
//                        return EventType.CLASS_DELETED;
//                    }
//
//                    public Integer visit(OWLObjectProperty property) {
//                        return EventType.PROPERTY_DELETED;
//                    }
//
//                    public Integer visit(OWLDataProperty property) {
//                        return EventType.PROPERTY_DELETED;
//                    }
//
//                    public Integer visit(OWLNamedIndividual individual) {
//                        return EventType.INDIVIDUAL_DELETED;
//                    }
//
//                    public Integer visit(OWLDatatype datatype) {
//                        return EventType.PROPERTY_DELETED;
//                    }
//
//                    public Integer visit(OWLAnnotationProperty property) {
//                        return EventType.PROPERTY_DELETED;
//                    }
//                });
//                return new EntityDeleteEvent(entityData, eventType, userId.getUserName(), Collections.<EntityData>emptyList(), revision);
//            }
//        }
//
//        public OntologyEvent visit(OWLAnnotationAssertionAxiom axiom) {
////            final EntityData entityData = project.getRenderingManager().getEntityData(axiom.getSubject());
////            if (changeType == ChangeType.ADDED) {
////                return new PropertyValueEvent(entityData, , );
////            }
////            else {
////                return EventType.PROPERTY_DELETED;
////            }
//            return null;
//        }
//
//        public OntologyEvent visit(OWLSymmetricObjectPropertyAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLDataPropertyRangeAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLFunctionalDataPropertyAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLEquivalentDataPropertiesAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLClassAssertionAxiom axiom) {
////            if (changeType == ChangeType.ADDED) {
////                return EventType.INDIVIDUAL_ADDED_OR_REMOVED;
////            }
////            else {
////                return EventType.INDIVIDUAL_ADDED_OR_REMOVED;
////            }
//            return null;
//        }
//
//        public OntologyEvent visit(OWLEquivalentClassesAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLDataPropertyAssertionAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLTransitiveObjectPropertyAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLSubDataPropertyOfAxiom axiom) {
////            if (changeType == ChangeType.ADDED) {
////                return EventType.SUBPROPERTY_ADDED;
////            }
////            else {
////                return EventType.SUBPROPERTY_REMOVED;
////            }
//            return null;
//        }
//
//        public OntologyEvent visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLSameIndividualAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLSubPropertyChainOfAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLInverseObjectPropertiesAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLHasKeyAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLDatatypeDefinitionAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(SWRLRule rule) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLSubAnnotationPropertyOfAxiom axiom) {
////            if (changeType == ChangeType.ADDED) {
////                return EventType.SUBPROPERTY_ADDED;
////            }
////            else {
////                return EventType.SUBPROPERTY_REMOVED;
////            }
//            return null;
//        }
//
//        public OntologyEvent visit(OWLAnnotationPropertyDomainAxiom axiom) {
//            return null;
//        }
//
//        public OntologyEvent visit(OWLAnnotationPropertyRangeAxiom axiom) {
//            return null;
//        }
//    }


    private static class OntologyChangeDescriptionGenerator implements OWLAxiomVisitorEx<String> {

        private OWLAPIProject project;

        private OntologyChangeDescriptionGenerator(OWLAPIProject project) {
            this.project = project;
        }

        private String getBrowserText(OWLObject object) {
            return project.getRenderingManager().getBrowserText(object);
        }

        public String visit(OWLSubClassOfAxiom axiom) {
            return getBrowserText(axiom);
        }

        public String visit(OWLNegativeObjectPropertyAssertionAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLAsymmetricObjectPropertyAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLReflexiveObjectPropertyAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLDisjointClassesAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLDataPropertyDomainAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLObjectPropertyDomainAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLEquivalentObjectPropertiesAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLNegativeDataPropertyAssertionAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLDifferentIndividualsAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLDisjointDataPropertiesAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLDisjointObjectPropertiesAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLObjectPropertyRangeAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLObjectPropertyAssertionAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLFunctionalObjectPropertyAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLSubObjectPropertyOfAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLDisjointUnionAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLDeclarationAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLAnnotationAssertionAxiom ax) {
            StringBuilder sb = new StringBuilder();
            sb.append("Added annotation: ");
            sb.append(getBrowserText(ax.getProperty()));
            sb.append("  ");
            sb.append(getBrowserText(ax.getValue()));
            return sb.toString();
        }

        public String visit(OWLSymmetricObjectPropertyAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLDataPropertyRangeAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLFunctionalDataPropertyAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLEquivalentDataPropertiesAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLClassAssertionAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLEquivalentClassesAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLDataPropertyAssertionAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLTransitiveObjectPropertyAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLIrreflexiveObjectPropertyAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLSubDataPropertyOfAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLInverseFunctionalObjectPropertyAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLSameIndividualAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLSubPropertyChainOfAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLInverseObjectPropertiesAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLHasKeyAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLDatatypeDefinitionAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(SWRLRule ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLSubAnnotationPropertyOfAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLAnnotationPropertyDomainAxiom ax) {
            return getBrowserText(ax);
        }

        public String visit(OWLAnnotationPropertyRangeAxiom ax) {
            return getBrowserText(ax);
        }
    }


}
