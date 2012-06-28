package edu.stanford.bmir.protege.web.server.owlapi.change;

import edu.stanford.bmir.protege.web.client.model.event.*;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.server.owlapi.*;
import org.semanticweb.owlapi.binaryowl.*;
import org.semanticweb.owlapi.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.owlapi.binaryowl.chunk.SkipSetting;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;

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
            BinaryOWLOntologyChangeLog changeLog = new BinaryOWLOntologyChangeLog();
            changeLog.readChanges(new BufferedInputStream(new FileInputStream(changeHistoryFile)), project.getDataFactory(), new BinaryOWLChangeLogHandler() {
                public void handleChangesRead(OntologyChangeRecordList list, SkipSetting skipSetting, long l) {
                    BinaryOWLMetadata metadata = list.getMetadata();
                    String userName = metadata.getStringAttribute(USERNAME_METADATA_ATTRIBUTE, "");
                    Long revision = metadata.getLongAttribute(REVISION_META_DATA_ATTRIBUTE, 0l);

                    String description = metadata.getStringAttribute(DESCRIPTION_META_DATA_ATTRIBUTE, "");

                    RevisionType type = RevisionType.valueOf(metadata.getStringAttribute(REVISION_TYPE_META_DATA_ATTRIBUTE, RevisionType.EDIT.name()));

                    final UserId userId = UserId.getUserId(userName);
                    final List<OWLOntologyChangeRecord> changeRecords = list.getChangeRecords();

                    Revision chgList = new Revision(userId, revision, changeRecords, list.getTimestamp(), description, type);
                    revisions.add(chgList);
                }
            }, SkipSetting.SKIP_NONE);
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

            logChangesInternal(UserId.getNull(), changes, "Initial import", RevisionType.BASELINE, true);
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
            long revision = getCurrentRevision() + 1;
            long timestamp = System.currentTimeMillis();
            final String highlevelDescription = desc != null ? desc : "";
            List<OWLOntologyChangeRecord> records = new ArrayList<OWLOntologyChangeRecord>(changes.size());
            for (OWLOntologyChange change : changes) {
                records.add(new OWLOntologyChangeRecord(change));
            }
            revisions.add(new Revision(userId, revision, records, timestamp, highlevelDescription, revisionType));
            persistChanges(timestamp, revision, revisionType, userId, changes, highlevelDescription, immediately);
        }
        finally {
            writeLock.unlock();
        }
    }

    private void persistChanges(long timestamp, long revision, RevisionType type, UserId userId, List<? extends OWLOntologyChange> changes, String highlevelDescription, boolean immediately) {
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
        OWLAPIProjectDocumentStoreImpl documentStore = OWLAPIProjectDocumentStoreImpl.getProjectDocumentStore(project.getProjectId());
        File file = documentStore.getChangeDataFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }


    private void handleCorruptChangeLog(BinaryOWLParseException e) {
        // The change log appears to be corrupt.  We somehow need a way of backing up the old log and creating
        // a fresh one.
        System.err.println("Corrupt change log: " + e.getMessage());
        e.printStackTrace();
    }

    public long getCurrentRevision() {
        readLock.lock();
        try {
            if (revisions.isEmpty()) {
                return 0;
            }
            Revision lastRevisionChangeList = revisions.get(revisions.size() - 1);
            return lastRevisionChangeList.getRevision();
        }
        finally {
            readLock.unlock();
        }
    }

    private List<Revision> getRevisionsSinceVersion(long version) {
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

    private int getRevisionIndexForRevision(long revision) {
        if (revisions.isEmpty()) {
            return -1;
        }
        Revision firstRevision = revisions.get(0);
        if (revision < firstRevision.getRevision()) {
            return -1;
        }
        Revision lastRevision = revisions.get(revisions.size() - 1);
        if (lastRevision.getRevision() == revision) {
            return revisions.size() - 1;
        }
        Revision dummy = Revision.createEmptyRevisionWithRevisionNumber(revision);
        return Collections.binarySearch(revisions, dummy);
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


    public List<ChangeData> getChangeDataInTimestampInterval(long fromTimestamp, long toTimestamp) {
        try {
            readLock.lock();
            List<ChangeData> result = new ArrayList<ChangeData>();
            for (Revision changeList : revisions) {
                long ts = changeList.getTimestamp();
                if (ts >= fromTimestamp && ts <= toTimestamp) {
                    result.add(new ChangeData(changeList.getUserId().getUserName(), changeList.getHighLevelDescription(project), new Date(changeList.getTimestamp())));
                }
            }
            return result;
        }
        finally {
            readLock.unlock();
        }
    }

    public List<ChangeData> getChangeDataForEntitiesInTimeStampInterval(Set<OWLEntity> entites, long fromTimestamp, long toTimestamp) {
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

    public List<OntologyEvent> getOntologyEventsSinceVersion(long version) {
        readLock.lock();
        try {
            List<OntologyEvent> result = new ArrayList<OntologyEvent>();
            long nextRevisionNumber = version + 1;
            List<Revision> revisions = getRevisionsSinceVersion(nextRevisionNumber);
            for (Revision revision : revisions) {
                OWLOntologyChangeTranslator translator = new OWLOntologyChangeTranslator(revision.getUserId(), revision.getRevision());
                for (OWLOntologyChangeRecord change : revision) {
                    OntologyEvent event = change.getInfo().accept(translator);
                    if (event != null) {
                        result.add(event);
                    }
                }
                getBrowserTextChanges(result, revision);
            }
            return result;
        }
        finally {
            readLock.unlock();
        }
    }

    private void getBrowserTextChanges(List<OntologyEvent> resultToFill, Revision revision) {
        OWLAPIEntityEditorKit editorKit = project.getOWLEntityEditorKit();
        List<OWLEntityBrowserTextChangeSet> browserTextChanges = editorKit.getChangedEntities(revision.getChanges());
        for (OWLEntityBrowserTextChangeSet changeSet : browserTextChanges) {
            EntityData entityData = project.getRenderingManager().getEntityData(changeSet.getEntity());
            resultToFill.add(new EntityBrowserTextChangedEvent(entityData, revision.getUserId().getUserName(), (int) revision.getRevision()));
        }
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


    private static class Revision implements Iterable<OWLOntologyChangeRecord>, Comparable<Revision> {

        private UserId userId;

        private long revision;

        private long timestamp;

        private List<OWLOntologyChangeRecord> changes = new ArrayList<OWLOntologyChangeRecord>();

        private String highLevelDescription;

        private RevisionType revisionType;

        private Revision(UserId userId, long revision, List<OWLOntologyChangeRecord> changes, long timestamp, String highLevelDescription, RevisionType revisionType) {
            this.changes.addAll(changes);
            this.userId = userId;
            this.revision = revision;
            this.timestamp = timestamp;
            this.highLevelDescription = highLevelDescription;
            this.revisionType = revisionType;
        }

        private Revision(long revision) {
            this.userId = UserId.getNull();
            this.revision = revision;
            this.timestamp = 0;
            this.changes = Collections.emptyList();
            this.highLevelDescription = "";
            this.revisionType = RevisionType.EDIT;
        }

        public static Revision createEmptyRevisionWithRevisionNumber(long revision) {
            return new Revision(revision);
        }

        public static Revision createEmptyRevisionWithTimestamp(long timestamp) {
            Revision revision = new Revision(0);
            revision.timestamp = timestamp;
            return revision;
        }

        public Set<OWLEntity> getEntities(OWLAPIProject project) {
            Set<OWLEntity> result = new HashSet<OWLEntity>();
            for (OWLOntologyChangeRecord change : changes) {
                if (change.getInfo() instanceof AxiomChangeRecordInfo) {
                    OWLAxiom ax = ((AxiomChangeRecordInfo) change.getInfo()).getAxiom();
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

        public long getRevision() {
            return revision;
        }

        public RevisionType getRevisionType() {
            return revisionType;
        }

        public int compareTo(Revision o) {
            if (revision < o.revision) {
                return -1;
            }
            else if (revision == o.revision) {
                return 0;
            }
            else {
                return 1;
            }
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
                if (changeRecord.getInfo() instanceof AxiomChangeRecordInfo) {
                    AxiomChangeRecordInfo info = (AxiomChangeRecordInfo) changeRecord.getInfo();
                    OWLAxiom axiom = info.getAxiom();
                    if (entity == null || isEntitySubjectOfChange(entity, info)) {
                        sb.append("<div style=\"overflow: hidden;\">");
                        String ren = changeRecord.getInfo().accept(new OWLOntologyChangeRecordInfoVisitor<String, RuntimeException>() {

                            public String visit(AddAxiomChangeRecordInfo addAxiom) {
                                return new StringBuilder().append("<b>Added: </b> ").append(project.getRenderingManager().getBrowserText(addAxiom.getAxiom())).toString();
                            }

                            public String visit(RemoveAxiomChangeRecordInfo removeAxiom) {
                                return new StringBuilder().append("<b>Removed: </b> ").append(project.getRenderingManager().getBrowserText(removeAxiom.getAxiom())).toString();
                            }

                            public String visit(SetOntologyIDChangeRecordInfo setOntologyID) {
                                return new StringBuilder().append("Changed ontology id from ").append(changeRecord.getOntologyID()).append(" to ").append(setOntologyID.getNewId()).toString();
                            }

                            public String visit(AddImportChangeRecordInfo addImport) {
                                return new StringBuilder().append("Added import: ").append(addImport.getDeclaration().getIRI().toQuotedString()).toString();
                            }

                            public String visit(RemoveImportChangeRecordInfo removeImport) {
                                return new StringBuilder().append("Removed import: ").append(removeImport.getDeclaration().getIRI().toQuotedString()).toString();
                            }

                            public String visit(AddOntologyAnnotationChangeRecordInfo addOntologyAnnotation) {
                                return new StringBuilder().append("Added annotation to ontology: ").append(project.getRenderingManager().getBrowserText(addOntologyAnnotation.getAnnotation())).toString();
                            }

                            public String visit(RemoveOntologyAnnotationChangeRecordInfo removeOntologyAnnotation) {
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

        private boolean isEntitySubjectOfChange(OWLEntity entity, AxiomChangeRecordInfo change) {
            OWLObject changeSubject = getChangeSubject(change);
            return changeSubject.equals(entity) || (changeSubject != null && changeSubject.equals(entity.getIRI()));
        }

        private OWLObject getChangeSubject(AxiomChangeRecordInfo change) {
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


    private class OWLOntologyChangeTranslator implements OWLOntologyChangeRecordInfoVisitor<OntologyEvent, RuntimeException> {

        private UserId userId;

        private long revision;

        private OWLOntologyChangeTranslator(UserId userId, long revision) {
            this.userId = userId;
            this.revision = revision;
        }

        public OntologyEvent visit(AddAxiomChangeRecordInfo addAxiom) {
            EntityData sourceEntityData = getAxiomChangeSubject(addAxiom);
            if (sourceEntityData == null) {
                return null;
            }
            ChangeType changeType = ChangeType.ADDED;
            Integer eventType = getEventType(addAxiom, changeType);
            return new AbstractOntologyEvent(sourceEntityData, eventType != null ? eventType : -1, userId.getUserName(), (int) revision);
        }


        public OntologyEvent visit(RemoveAxiomChangeRecordInfo removeAxiom) {
            EntityData sourceEntityData = getAxiomChangeSubject(removeAxiom);
            if (sourceEntityData == null) {
                return null;
            }
            ChangeType changeType = ChangeType.REMOVED;
            Integer eventType = getEventType(removeAxiom, changeType);
            return new AbstractOntologyEvent(sourceEntityData, eventType != null ? eventType : -1, userId.getUserName(), (int) revision);
        }

        public OntologyEvent visit(SetOntologyIDChangeRecordInfo setOntologyID) {
            return null;
        }

        public OntologyEvent visit(AddImportChangeRecordInfo addImport) {
            return null;
        }

        public OntologyEvent visit(RemoveImportChangeRecordInfo removeImport) {
            return null;
        }

        public OntologyEvent visit(AddOntologyAnnotationChangeRecordInfo addOntologyAnnotation) {
            return null;
        }

        public OntologyEvent visit(RemoveOntologyAnnotationChangeRecordInfo removeOntologyAnnotation) {
            return null;
        }


        private EntityData getAxiomChangeSubject(AxiomChangeRecordInfo addAxiom) {
            AxiomSubjectProvider subjectProvider = new AxiomSubjectProvider();
            OWLObject object = subjectProvider.getSubject(addAxiom.getAxiom());
            return object.accept(new OWLObjectVisitorExAdapter<EntityData>() {
                @Override
                public EntityData visit(OWLClass desc) {
                    return project.getRenderingManager().getEntityData(desc);
                }

                @Override
                public EntityData visit(OWLObjectProperty property) {
                    return project.getRenderingManager().getEntityData(property);
                }

                @Override
                public EntityData visit(OWLDataProperty property) {
                    return project.getRenderingManager().getEntityData(property);
                }

                @Override
                public EntityData visit(OWLAnnotationProperty property) {
                    return project.getRenderingManager().getEntityData(property);
                }

                @Override
                public EntityData visit(OWLNamedIndividual individual) {
                    return project.getRenderingManager().getEntityData(individual);
                }

                @Override
                public EntityData visit(IRI iri) {
                    Set<OWLEntity> entities = project.getRootOntology().getEntitiesInSignature(iri);
                    if (!entities.isEmpty()) {
                        OWLEntity entity = RenderingManager.selectEntity(entities);
                        return project.getRenderingManager().getEntityData(entity);
                    }
                    else {
                        return project.getRenderingManager().getEntityData(iri.toString(), EntityType.CLASS);
                    }
                }
            });
        }
    }


    private static Integer getEventType(AxiomChangeRecordInfo change, ChangeType changeType) {
        OntologyEventIdTranslator translator = new OntologyEventIdTranslator(changeType);
        Integer eventType = change.getAxiom().accept(translator);
        if (eventType == null) {
            return -1;
        }
        else {
            return eventType;
        }
    }


    private enum ChangeType {
        ADDED,

        REMOVED
    }

    private static class OntologyEventIdTranslator implements OWLAxiomVisitorEx<Integer> {

        private ChangeType changeType;

        private OntologyEventIdTranslator(ChangeType changeType) {
            this.changeType = changeType;
        }

        public Integer visit(OWLSubClassOfAxiom axiom) {
            if (changeType == ChangeType.ADDED) {
                return EventType.SUBCLASS_ADDED;
            }
            else {
                return EventType.SUBCLASS_REMOVED;
            }
        }

        public Integer visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
            return null;
        }

        public Integer visit(OWLAsymmetricObjectPropertyAxiom axiom) {
            return null;
        }

        public Integer visit(OWLReflexiveObjectPropertyAxiom axiom) {
            return null;
        }

        public Integer visit(OWLDisjointClassesAxiom axiom) {
            return null;
        }

        public Integer visit(OWLDataPropertyDomainAxiom axiom) {
            return null;
        }

        public Integer visit(OWLObjectPropertyDomainAxiom axiom) {
            return null;
        }

        public Integer visit(OWLEquivalentObjectPropertiesAxiom axiom) {
            return null;
        }

        public Integer visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
            return null;
        }

        public Integer visit(OWLDifferentIndividualsAxiom axiom) {
            return null;
        }

        public Integer visit(OWLDisjointDataPropertiesAxiom axiom) {
            return null;
        }

        public Integer visit(OWLDisjointObjectPropertiesAxiom axiom) {
            return null;
        }

        public Integer visit(OWLObjectPropertyRangeAxiom axiom) {
            return null;
        }

        public Integer visit(OWLObjectPropertyAssertionAxiom axiom) {
            return null;
        }

        public Integer visit(OWLFunctionalObjectPropertyAxiom axiom) {
            return null;
        }

        public Integer visit(OWLSubObjectPropertyOfAxiom axiom) {
            if (changeType == ChangeType.ADDED) {
                return EventType.SUBPROPERTY_ADDED;
            }
            else {
                return EventType.SUBPROPERTY_REMOVED;
            }
        }

        public Integer visit(OWLDisjointUnionAxiom axiom) {
            return null;
        }

        public Integer visit(OWLDeclarationAxiom axiom) {
            if (changeType == ChangeType.ADDED) {
                return axiom.getEntity().accept(new OWLEntityVisitorEx<Integer>() {
                    public Integer visit(OWLClass cls) {
                        return EventType.CLASS_CREATED;
                    }

                    public Integer visit(OWLObjectProperty property) {
                        return EventType.PROPERTY_CREATED;
                    }

                    public Integer visit(OWLDataProperty property) {
                        return EventType.PROPERTY_CREATED;
                    }

                    public Integer visit(OWLNamedIndividual individual) {
                        return EventType.INDIVIDUAL_CREATED;
                    }

                    public Integer visit(OWLDatatype datatype) {
                        return EventType.PROPERTY_CREATED;
                    }

                    public Integer visit(OWLAnnotationProperty property) {
                        return EventType.PROPERTY_CREATED;
                    }
                });
            }
            else {
                return axiom.getEntity().accept(new OWLEntityVisitorEx<Integer>() {
                    public Integer visit(OWLClass cls) {
                        return EventType.CLASS_DELETED;
                    }

                    public Integer visit(OWLObjectProperty property) {
                        return EventType.PROPERTY_DELETED;
                    }

                    public Integer visit(OWLDataProperty property) {
                        return EventType.PROPERTY_DELETED;
                    }

                    public Integer visit(OWLNamedIndividual individual) {
                        return EventType.INDIVIDUAL_DELETED;
                    }

                    public Integer visit(OWLDatatype datatype) {
                        return EventType.PROPERTY_DELETED;
                    }

                    public Integer visit(OWLAnnotationProperty property) {
                        return EventType.PROPERTY_DELETED;
                    }
                });
            }
        }

        public Integer visit(OWLAnnotationAssertionAxiom axiom) {
            if (changeType == ChangeType.ADDED) {
                return EventType.PROPERTY_VALUE_ADDED;
            }
            else {
                return EventType.PROPERTY_DELETED;
            }
        }

        public Integer visit(OWLSymmetricObjectPropertyAxiom axiom) {
            return null;
        }

        public Integer visit(OWLDataPropertyRangeAxiom axiom) {
            return null;
        }

        public Integer visit(OWLFunctionalDataPropertyAxiom axiom) {
            return null;
        }

        public Integer visit(OWLEquivalentDataPropertiesAxiom axiom) {
            return null;
        }

        public Integer visit(OWLClassAssertionAxiom axiom) {
            if (changeType == ChangeType.ADDED) {
                return EventType.INDIVIDUAL_ADDED_OR_REMOVED;
            }
            else {
                return EventType.INDIVIDUAL_ADDED_OR_REMOVED;
            }
        }

        public Integer visit(OWLEquivalentClassesAxiom axiom) {
            return null;
        }

        public Integer visit(OWLDataPropertyAssertionAxiom axiom) {
            return null;
        }

        public Integer visit(OWLTransitiveObjectPropertyAxiom axiom) {
            return null;
        }

        public Integer visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
            return null;
        }

        public Integer visit(OWLSubDataPropertyOfAxiom axiom) {
            if (changeType == ChangeType.ADDED) {
                return EventType.SUBPROPERTY_ADDED;
            }
            else {
                return EventType.SUBPROPERTY_REMOVED;
            }
        }

        public Integer visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
            return null;
        }

        public Integer visit(OWLSameIndividualAxiom axiom) {
            return null;
        }

        public Integer visit(OWLSubPropertyChainOfAxiom axiom) {
            return null;
        }

        public Integer visit(OWLInverseObjectPropertiesAxiom axiom) {
            return null;
        }

        public Integer visit(OWLHasKeyAxiom axiom) {
            return null;
        }

        public Integer visit(OWLDatatypeDefinitionAxiom axiom) {
            return null;
        }

        public Integer visit(SWRLRule rule) {
            return null;
        }

        public Integer visit(OWLSubAnnotationPropertyOfAxiom axiom) {
            if (changeType == ChangeType.ADDED) {
                return EventType.SUBPROPERTY_ADDED;
            }
            else {
                return EventType.SUBPROPERTY_REMOVED;
            }
        }

        public Integer visit(OWLAnnotationPropertyDomainAxiom axiom) {
            return null;
        }

        public Integer visit(OWLAnnotationPropertyRangeAxiom axiom) {
            return null;
        }
    }


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
