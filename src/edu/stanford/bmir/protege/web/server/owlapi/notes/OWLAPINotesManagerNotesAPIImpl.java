package edu.stanford.bmir.protege.web.server.owlapi.notes;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import org.protege.notesapi.NotesException;
import org.protege.notesapi.NotesManager;
import org.protege.notesapi.notes.AnnotatableThing;
import org.protege.notesapi.notes.Annotation;
import org.protege.notesapi.notes.NoteType;
import org.protege.notesapi.oc.impl.DefaultOntologyComponent;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.binaryowl.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.owlapi.binaryowl.change.OntologyChangeRecordInfoList;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecordInfo;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/04/2012
 */
public class OWLAPINotesManagerNotesAPIImpl implements OWLAPINotesManager {


    public static final String CHANGES_ONTOLOGY_FILE_NAME = "changes.owl";


    private OWLAPIProject project;
    
    private OWLOntology notesOntology;

    private final NotesManager notesManager;

    private static final String NOTES_ONTOLOGY_DOCUMENT_NAME = "notes-data.binary";
    
    private File notesOntologyDocument;


    public OWLAPINotesManagerNotesAPIImpl(OWLAPIProject project) {
        this.project = project;
        try {
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
        notesOntology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(notesOntologyDocument);
    }


    private void createEmptyNotesOntology() {
        try {
            OWLOntologyManager notesOntologyManager = OWLManager.createOWLOntologyManager();
            notesOntology = notesOntologyManager.createOntology();
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
                List<OWLOntologyChangeRecordInfo> infoList = new ArrayList<OWLOntologyChangeRecordInfo>();
                for(OWLOntologyChange change : changes) {
                    OWLOntologyChangeRecord rec = new OWLOntologyChangeRecord(change);
                    OWLOntologyChangeRecordInfo info = rec.getInfo();
                    infoList.add(info);
                }
                BinaryOWLOntologyDocumentSerializer serializer = new BinaryOWLOntologyDocumentSerializer();
                serializer.appendOntologyChanges(notesOntologyDocument, new OntologyChangeRecordInfoList(infoList, System.currentTimeMillis()));
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
        OWLEntity entity = getOWLEntityForObjectId(objectId);
        return getNotesDataForEntity(entity, NoteRetrievalType.THREAD);
    }

    public List<NotesData> getDirectRepliesForObjectId(String objectId) {
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

    public NotesData addReplyToObjectId(String subject, String author, String body, NoteType noteType, String annotatedThingId) {
        try {
            AnnotatableThing annotatableThing = getAnnotatableThingForObjectId(annotatedThingId);
            Annotation annotation = notesManager.createSimpleNote(noteType, subject, body, author, annotatableThing);
            return getNotesDataForAnnotation(annotation, annotatableThing, NoteRetrievalType.THREAD);
        }
        catch (NotesException e) {
            throw new RuntimeException(e);
        }
    }

    public NotesData changeNoteContent(String noteId, String subject, String author, String body, NoteType noteType) {
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

    public List<NotesData> deleteNoteAndRepliesForObjectId(String objectId) {
        AnnotatableThing annotatableThing = getAnnotatableThingForObjectId(objectId);
        notesManager.deleteNote(annotatableThing.getId());
        return Collections.emptyList();
    }

    public void setArchivedStatus(String noteId, ArchivesStatus archivesStatus) {
        Annotation note = notesManager.getNote(noteId);
        note.setArchived(archivesStatus == ArchivesStatus.ARCHIVED);
    }

    public int getDirectNotesCount(OWLClass cls) {
        return 0;
    }

    public int getIndirectNotesCount(OWLClass cls) {
        return 0;
    }
}
