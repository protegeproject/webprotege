package edu.stanford.bmir.protege.web.server.notes;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.EntityNotesChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.NotePostedEvent;
import edu.stanford.bmir.protege.web.shared.notes.*;
import edu.stanford.bmir.protege.web.shared.notes.NoteType;
import org.protege.notesapi.NotesException;
import org.protege.notesapi.NotesManager;
import org.protege.notesapi.notes.*;
import org.protege.notesapi.oc.impl.DefaultOntologyComponent;
import org.semanticweb.owlapi.binaryowl.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.owlapi.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/04/2012
 */
public class OWLAPINotesManagerNotesAPIImpl implements OWLAPINotesManager {


    public static final String CHANGES_ONTOLOGY_FILE_NAME = "changes.owl";

    public static final IRI CHANGES_ONTOLOGY_IRI = IRI.create("http://protege.stanford.edu/ontologies/ChAO/changes.owl");


    private OWLAPIProject project;
    
    private OWLOntology notesOntology;

    private final NotesManager notesManager;

    private static final String NOTES_ONTOLOGY_DOCUMENT_NAME = "notes-data.binary";
    
    private File notesOntologyDocument;


    public OWLAPINotesManagerNotesAPIImpl(OWLAPIProject project) {
        this.project = project;
        try {
            long t0 = System.currentTimeMillis();
            OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(project.getProjectId());
            File notesDataDirectory = documentStore.getNotesDataDirectory();
            notesOntologyDocument = new File(notesDataDirectory, NOTES_ONTOLOGY_DOCUMENT_NAME);
            if(!notesOntologyDocument.exists()) {
                createEmptyNotesOntology();
            }
            else {
                loadExistingNotesOntology();
            }
            
            notesManager = NotesManager.createNotesManager(notesOntology, getChangeOntologyDocumentIRI().toString());
            notesManager.getOWLOntology().getOWLOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
                public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
                    handleNotesOntologyChanged(Collections.unmodifiableList(changes));
                }
            });
            long t1 = System.currentTimeMillis();
            WebProtegeLoggerManager.get(OWLAPINotesManagerNotesAPIImpl.class).info("Initialized notes manager in " + (t1 - t0) + "ms");
        }
        catch (OWLOntologyCreationException e) {
            // Can't start - too dangerous to do anything without human intervention
            throw new RuntimeException(e);
        }
        catch (NotesException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadExistingNotesOntology() throws OWLOntologyCreationException {
        final OWLOntologyManager man = WebProtegeOWLManager.createOWLOntologyManager();
        man.addIRIMapper(new SimpleIRIMapper(CHANGES_ONTOLOGY_IRI,  getChangeOntologyDocumentIRI()));
        notesOntology = man.loadOntologyFromOntologyDocument(notesOntologyDocument);
    }


    private void createEmptyNotesOntology() {
        try {
            OWLOntologyManager notesOntologyManager = WebProtegeOWLManager.createOWLOntologyManager();
            notesOntology = notesOntologyManager.createOntology();
            final OWLDataFactory df = notesOntologyManager.getOWLDataFactory();
            notesOntologyManager.applyChange(new AddImport(notesOntology, df.getOWLImportsDeclaration(CHANGES_ONTOLOGY_IRI)));
            IRI notesOntologyDocumentIRI = IRI.create(notesOntologyDocument);
            notesOntologyManager.setOntologyDocumentIRI(notesOntology, notesOntologyDocumentIRI);
            notesOntologyDocument.getParentFile().mkdirs();
            BinaryOWLOntologyDocumentFormat notesOntologyDocumentFormat = new BinaryOWLOntologyDocumentFormat();
            notesOntologyManager.saveOntology(notesOntology, notesOntologyDocumentFormat, notesOntologyDocumentIRI);
        }
        catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
        catch (OWLOntologyStorageException e) {
            throw new RuntimeException(e);
        }
    }


    private void handleNotesOntologyChanged(List<OWLOntologyChange> changes) {
        try {
            OWLOntologyManager notesOntologyManager = notesOntology.getOWLOntologyManager();
            if(notesOntologyManager.getOntologyFormat(notesOntology) instanceof BinaryOWLOntologyDocumentFormat) {
                OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(project.getProjectId());
                List<OWLOntologyChangeData> infoList = new ArrayList<OWLOntologyChangeData>();
                for(OWLOntologyChange change : changes) {
                    OWLOntologyChangeRecord rec = change.getChangeRecord();
                    OWLOntologyChangeData info = rec.getData();
                    infoList.add(info);
                }
                BinaryOWLOntologyDocumentSerializer serializer = new BinaryOWLOntologyDocumentSerializer();
                serializer.appendOntologyChanges(notesOntologyDocument, new OntologyChangeDataList(infoList, System.currentTimeMillis()));
            }
            else {
                // Swap it over
                notesOntologyManager.saveOntology(notesOntology, new BinaryOWLOntologyDocumentFormat());
            }

        }
        catch (OWLOntologyStorageException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private IRI getChangeOntologyDocumentIRI() {
        URL changeOntologyURL = OWLAPINotesManagerNotesAPIImpl.class.getResource("/" + CHANGES_ONTOLOGY_FILE_NAME);
        if (changeOntologyURL == null) {
            throw new RuntimeException("Changes ontology not found.  Please make sure the changes ontology document is placed in the class path with a file name of " + CHANGES_ONTOLOGY_FILE_NAME);
        }
        String uriString = changeOntologyURL.toString();
        return IRI.create(uriString);
    }

    private OWLEntity getOWLEntityForObjectId(String objectId) {
        RenderingManager rm = project.getRenderingManager();
        Set<OWLEntity> entities = rm.getEntities(objectId);
        if (!entities.isEmpty()) {
            return RenderingManager.selectEntity(entities);
        }
        else {
            return getIdentifyingIndividualForNoteId(objectId);
        }
    }

    private OWLEntity getOWLEntityForAnnotatableThing(AnnotatableThing annotatableThing) {
        return getOWLEntityForObjectId(annotatableThing.getId());
    }

   
    private List<NotesData> getNotesDataForEntity(OWLEntity entity, NoteRetrievalType noteRetrievalType) {
        AnnotatableThing annotatableThing = getAnnotatableThing(entity);
        return getNotesData(annotatableThing, noteRetrievalType);
    }

    public List<NotesData> getRepliesForObjectId(String objectId) {
        if(objectId == null) {
            return Collections.emptyList();
        }
        OWLEntity entity = getOWLEntityForObjectId(objectId);
        return getNotesDataForEntity(entity, NoteRetrievalType.THREAD);
    }

    public List<NotesData> getDirectRepliesForObjectId(String objectId) {
        if(objectId == null) {
            return Collections.emptyList();
        }
        OWLEntity entity = getOWLEntityForObjectId(objectId);    
        return getNotesDataForEntity(entity, NoteRetrievalType.DIRECT_REPLIES);
    }

    private OWLNamedIndividual getIdentifyingIndividualForNoteId(String noteId) {
        if(noteId.startsWith("<")) {
            throw new RuntimeException("Problem:  Browser text is being used by something.");
        }
        return project.getDataFactory().getOWLNamedIndividual(IRI.create(noteId));
    }

    public List<NotesData> getNotesData(AnnotatableThing annotatableThing, NoteRetrievalType noteRetrievalType) {
        List<NotesData> result = new ArrayList<NotesData>();
        for (Annotation annotation : annotatableThing.getAssociatedAnnotations()) {
            NotesData notesDataForAnnotation = getNotesDataForAnnotation(annotation, annotatableThing, noteRetrievalType);
            result.add(notesDataForAnnotation);
        }
        return result;
    }

    public NotesData getNotesDataForAnnotation(Annotation annotation, AnnotatableThing annotatedObject, NoteRetrievalType noteRetrievalType) {
        RenderingManager rm = project.getRenderingManager();

        String author = annotation.getAuthor();
        String body = annotation.getBody();
        String timestamp = formatTimeStamp(annotation.getCreatedAt());
        String noteType = annotation.getType().name();
        String subject = annotation.getSubject();

        EntityData noteId = rm.getEntityData(annotation.getId(), EntityType.NAMED_INDIVIDUAL);
        EntityData replyToId = rm.getEntityData(getOWLEntityForAnnotatableThing(annotatedObject));
        List<NotesData> replies;
        int numberOfReplies = 0;
        if(noteRetrievalType == NoteRetrievalType.THREAD) {
            replies = getNotesData(annotation, noteRetrievalType);
            numberOfReplies = replies.size();
        }
        else {
            replies = Collections.emptyList();
            numberOfReplies = getNotesData(annotation, NoteRetrievalType.DIRECT_REPLIES).size();
        }
        NotesData result = new NotesData(author, body, timestamp, noteType, subject, noteId, replyToId, replies);
        result.setNumOfReplies(numberOfReplies);
        Boolean archived = annotation.getArchived();
        // Amazingly (!), this can be null.
        if (archived != null) {
            result.setArchived(archived);
        }
        return result;
    }

    public Note getNoteForAnnotation(Annotation annotation, Optional<NoteId> inReplyTo) {
        RenderingManager rm = project.getRenderingManager();

        UserId author = UserId.getUserId(annotation.getAuthor());
        String body = annotation.getBody() == null ? "" : annotation.getBody();
        long timestamp = annotation.getCreatedAt();
        Optional<String> subject = annotation.getSubject() == null ? Optional.<String>absent() : Optional.<String>of(annotation.getSubject());

        NoteId noteId = NoteId.createNoteIdFromLexicalForm(annotation.getId());
        NoteHeader noteHeader = new NoteHeader(noteId, inReplyTo, author, timestamp);
        NoteStatus noteStatus = annotation.getArchived() != null && annotation.getArchived() ? NoteStatus.RESOLVED : NoteStatus.OPEN;
        NoteContent noteContent = NoteContent.builder().setBody(body).setNoteStatus(noteStatus).setNoteType(NoteType.COMMENT).setSubject(subject).build();
        return Note.createNote(noteHeader, noteContent);
    }


    public String formatTimeStamp(long timestamp) {
        Date date = new Date(timestamp);
        DateFormat dateFormat = DateFormat.getDateInstance();
        return dateFormat.format(date);
    }

    /**
     * Converts an OWLEntity to an AnnotatableThing.  The entity MUST be either an OWLClass, OWLObjectProperty,
     * OWLDataProperty, OWLAnnotationProperty or OWLNamedIndividual.  This method does not support the conversion
     * of OWLDatatype objects.
     * @param entity The entity to be converted.
     * @return The AnnotatableThing corresponding to the entity.
     */
    private AnnotatableThing getAnnotatableThing(OWLEntity entity) {
        return new DefaultOntologyComponent(project.getDataFactory().getOWLNamedIndividual(entity.getIRI()), notesOntology);
    }
    
    private AnnotatableThing getAnnotatableThingForObjectId(String objectId) {
        RenderingManager rm = project.getRenderingManager();
        Set<OWLEntity> entities = rm.getEntities(objectId);
        OWLEntity correspondingEntity;
        if(!entities.isEmpty()) {
            correspondingEntity = RenderingManager.selectEntity(entities);
        }
        else {
            correspondingEntity = getIdentifyingIndividualForNoteId(objectId);
        }
        return getAnnotatableThing(correspondingEntity);
    }


    public List<EntityData> getAvailableNoteTypes() {
        List<EntityData> result = new ArrayList<EntityData>();
        for (org.protege.notesapi.notes.NoteType noteType : org.protege.notesapi.notes.NoteType.values()) {
            String name = noteType.name();
            result.add(new EntityData(name));
        }
        return result;
    }

    @Override
    public NotesData addReplyToObjectId(String subject, String author, String body, org.protege.notesapi.notes.NoteType noteType, String annotatedThingId) {
        try {
            AnnotatableThing annotatableThing = getAnnotatableThingForObjectId(annotatedThingId);
            Annotation annotation = notesManager.createSimpleNote(noteType, subject, body, author, annotatableThing);
            final IRI iri = IRI.create(annotatedThingId);
            if(project.getRootOntology().containsClassInSignature(iri)) {
                OWLClass cls = project.getDataFactory().getOWLClass(iri);
                project.getEventManager().postEvent(new EntityNotesChangedEvent(project.getProjectId(), cls, getIndirectNotesCount(cls)));
            }
            final NotesData notesData = getNotesDataForAnnotation(annotation, annotatableThing, NoteRetrievalType.THREAD);
            Optional<OWLEntity> entity = findEntity(annotatedThingId);
            NoteTarget target = NoteTarget.get(entity);
            Optional<OWLEntityData> targetAsEntityData;
            if(entity.isPresent()) {
                OWLEntityData rendering = DataFactory.getOWLEntityData(entity.get(), project.getRenderingManager().getBrowserText(entity.get()));
                targetAsEntityData = Optional.of(rendering);
            }
            else {
                targetAsEntityData = Optional.absent();
            }
            postNotePostedEvent(target, targetAsEntityData, notesData.getNoteId().getName(), subject, author, body, annotatedThingId);
            return notesData;
        }
        catch (NotesException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<OWLEntity> findEntity(String annotatedThingId) {
        Set<OWLEntity> entities = project.getRootOntology().getEntitiesInSignature(IRI.create(annotatedThingId), true);
        if(!entities.isEmpty()) {
            // TODO BAD
            return Optional.of(entities.iterator().next());
        }
        return Optional.absent();
//        final AnnotatableThing annotatableThingForObjectId = getAnnotatableThingForObjectId(annotatedThingId);
//        List<NotesData> notesDatas = getNotesData(annotatableThingForObjectId, NoteRetrievalType.DIRECT_REPLIES);

    }

    private void postNotePostedEvent(NoteTarget target, Optional<OWLEntityData> targetAsEntityData, String noteId, String subject, String author, String body, String annotatedThingId) {
        NoteHeader noteHeader = new NoteHeader(NoteId.createNoteIdFromLexicalForm(noteId), Optional.<NoteId>absent(), UserId.getUserId(author), System.currentTimeMillis());
        NoteContent.Builder builder = NoteContent.builder();
        builder.setBody(body);
        if (subject != null && !subject.isEmpty()) {
            builder.setSubject(subject);
        }
        NoteDetails noteDetails = new NoteDetails(noteHeader, builder.build());
        project.getEventManager().postEvent(new NotePostedEvent(project.getProjectId(), targetAsEntityData, noteDetails));
    }

    @Override
    public NotesData changeNoteContent(String noteId, String subject, String author, String body, org.protege.notesapi.notes.NoteType noteType) {
        Annotation annotation = notesManager.getNote(noteId);
        annotation.setSubject(subject);
        annotation.setAuthor(author);
        annotation.setBody(body);
        Collection<AnnotatableThing> annotates = annotation.getAnnotates();
        if(annotates.isEmpty()) {
            throw new RuntimeException("Note does not annotate anything");
        }
        return getNotesDataForAnnotation(annotation, annotates.iterator().next(), NoteRetrievalType.THREAD);
    }

    @Override
    public void deleteNoteAndReplies(NoteId noteId) {
        notesManager.deleteNote(noteId.getLexicalForm());
        project.getEventManager().postEvent(new NoteDeletedEvent(project.getProjectId(), noteId));
    }

    public List<NotesData> deleteNoteAndRepliesForObjectId(String objectId) {
        if(objectId == null) {
            return Collections.emptyList();
        }
        AnnotatableThing annotatableThing = getAnnotatableThingForObjectId(objectId);
        notesManager.deleteNote(annotatableThing.getId());
        return Collections.emptyList();
    }

    public void setArchivedStatus(String noteId, ArchivesStatus archivesStatus) {
        Annotation note = notesManager.getNote(noteId);
        note.setArchived(archivesStatus == ArchivesStatus.ARCHIVED);
        NoteStatus status = (archivesStatus == ArchivesStatus.ARCHIVED ? NoteStatus.RESOLVED : NoteStatus.OPEN);
        project.getEventManager().postEvent(new NoteStatusChangedEvent(project.getProjectId(), NoteId.createNoteIdFromLexicalForm(noteId), status));
    }

    @Override
    public void setNoteStatus(NoteId noteId, NoteStatus noteStatus) {
        if(noteStatus == NoteStatus.OPEN) {
            setArchivedStatus(noteId.getLexicalForm(), ArchivesStatus.NOT_ARCHIVED);
        }
        else {
            setArchivedStatus(noteId.getLexicalForm(), ArchivesStatus.ARCHIVED);
        }
        project.getEventManager().postEvent(new NoteStatusChangedEvent(project.getProjectId(), noteId, noteStatus));
    }

    public int getDirectNotesCount(OWLEntity entity) {
        return getNotesDataForEntity(entity, NoteRetrievalType.DIRECT_REPLIES).size();
    }

    public int getIndirectNotesCount(OWLEntity entity) {
        final List<NotesData> notesDataForEntity = getNotesDataForEntity(entity, NoteRetrievalType.THREAD);
        int count = 0;
        for(NotesData data : notesDataForEntity) {
            count += getNotesCountInThread(data);
        }
        return count;
    }

    private int getNotesCountInThread(NotesData data) {
        int count = 1;
        for(NotesData reply : data.getReplies()) {
            count += getNotesCountInThread(reply);
        }
        return count;
    }

    @Override
    public Note addReplyToNote(NoteId inReplyToId, NoteContent replyContent, UserId author) {
        try {
            AnnotatableThing target = getAnnotatableThingForObjectId(inReplyToId.getLexicalForm());
            final String subject = replyContent.getSubject().or("");
            final String body = replyContent.getBody().or("");
            final org.protege.notesapi.notes.NoteType noteType = org.protege.notesapi.notes.NoteType.Comment;
            Annotation annotation = notesManager.createSimpleNote(noteType, subject, body, author.getUserName(), target);
            final long timeStamp = System.currentTimeMillis();
            annotation.setCreatedAt(timeStamp);
            final NoteId noteId = NoteId.createNoteIdFromLexicalForm(annotation.getId());
            NoteHeader noteHeader = new NoteHeader(noteId, Optional.of(inReplyToId), author, timeStamp);
            Note note = Note.createNote(noteHeader, replyContent);
            project.getEventManager().postEvent(new NotePostedEvent(project.getProjectId(), new NoteDetails(noteHeader, replyContent), Optional.of(inReplyToId)));
            return note;
        }
        catch (NotesException e) {
            throw new RuntimeException("Problem creating note: " + e.getMessage());
        }
    }

    @Override
    public Note addNoteToEntity(OWLEntity targetEntity, NoteContent noteContent, UserId author) {
        try {
            checkNotNull(targetEntity);
            checkNotNull(noteContent);
            checkNotNull(author);
            AnnotatableThing target = getAnnotatableThingForObjectId(targetEntity.getIRI().toString());
            final String subject = noteContent.getSubject().or("");
            final String body = noteContent.getBody().or("");
            final org.protege.notesapi.notes.NoteType noteType = org.protege.notesapi.notes.NoteType.Comment;
            Annotation annotation = notesManager.createSimpleNote(noteType, subject, body, author.getUserName(), target);
            final long timeStamp = System.currentTimeMillis();
            annotation.setCreatedAt(timeStamp);
            final NoteId noteId = NoteId.createNoteIdFromLexicalForm(annotation.getId());
            NoteHeader noteHeader = new NoteHeader(noteId, Optional.<NoteId>absent(), author, timeStamp);
            Note note = Note.createNote(noteHeader, noteContent);
            OWLEntityData entityData = DataFactory.getOWLEntityData(targetEntity, project.getRenderingManager().getBrowserText(targetEntity));
            project.getEventManager().postEvent(new NotePostedEvent(project.getProjectId(), Optional.of(entityData), new NoteDetails(noteHeader, noteContent)));
            return note;
        }
        catch (NotesException e) {
            throw new RuntimeException("Problem creating note: " + e.getMessage());
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public DiscussionThread getDiscusssionThread(OWLEntity targetEntity) {
        AnnotatableThing annotatableThing = getAnnotatableThing(targetEntity);
        Set<Note> result = new HashSet<Note>();
        for(Annotation annotation : annotatableThing.getAssociatedAnnotations()) {
            getAllNotesForAnnotation(annotation, Optional.<NoteId>absent(), result);
        }
//        Set<Note> notes = new HashSet<Note>();
//        for(int i = 0; i < 5; i++) {
//            final NoteId noteId = NoteId.createNoteIdFromLexicalForm("Note" + i);
//            String body = project.getProjectId() + " Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
//            final Optional<String> subject = Optional.of("Subject " + i + " in project " + project.getProjectId().getDisplayName());
//            Note note = Note.createNote(noteId, Optional.<NoteId>absent(), System.currentTimeMillis(), UserId.getGuest(), edu.stanford.bmir.protege.web.shared.notes.NoteType.getComment(), subject, body);
//            notes.add(note);
//        }
        return new DiscussionThread(result);
    }


    private void getAllNotesForAnnotation(Annotation annotation, Optional<NoteId> inReplyTo, Set<Note> result) {
        final Note noteForAnnotation = getNoteForAnnotation(annotation, inReplyTo);
        result.add(noteForAnnotation);
        for(Annotation anno : annotation.getAssociatedAnnotations()) {
            getAllNotesForAnnotation(anno, Optional.of(noteForAnnotation.getNoteId()), result);
        }
    }

}
