package edu.stanford.bmir.protege.web.server.notes;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.notes.converter.CHAO2NotesConverter;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.NotePostedEvent;
import edu.stanford.bmir.protege.web.shared.notes.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.apache.commons.io.FileUtils;
import org.protege.notesapi.NotesException;
import org.protege.notesapi.NotesManager;
import org.protege.notesapi.notes.AnnotatableThing;
import org.protege.notesapi.notes.Annotation;
import org.protege.notesapi.notes.impl.DefaultComment;
import org.protege.notesapi.oc.impl.DefaultOntologyComponent;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
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

    private static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(OWLAPINotesManagerNotesAPIImpl.class);


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
            importLegacyNotesIfNecessary();

            WebProtegeLoggerManager.get(OWLAPINotesManagerNotesAPIImpl.class).info(project.getProjectId(), "Initialized notes manager in %d ms", (t1 - t0));
        }
        catch (OWLOntologyCreationException e) {
            // Can't start - too dangerous to do anything without human intervention
            throw new RuntimeException(e);
        }
        catch (NotesException e) {
            throw new RuntimeException(e);
        }
    }

    private void importLegacyNotesIfNecessary() {
        // Junk to import notes
        OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(project.getProjectId());
        File notesDataDirectory = documentStore.getNotesDataDirectory();
        File legacy = new File(notesDataDirectory, "notes-data.legacy");
        if(legacy.exists()) {
            LOGGER.info(project.getProjectId(), "Importing legacy notes data");
            try {
                final BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(legacy));
                OWLOntology legacyNotesOntology = WebProtegeOWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(inputStream);
                String base = legacyNotesOntology.getOntologyID().getOntologyIRI().toString();
                LOGGER.info(project.getProjectId(), "Using base obtained from legacy notes ontology: " + base);
                CHAO2NotesConverter converter = new CHAO2NotesConverter(project.getRootOntology(), legacyNotesOntology, base);
                converter.convertToNotes(this);
                inputStream.close();
                FileUtils.moveFile(legacy, new File(legacy.getParentFile(), "notes-data.legacy.imported-" + System.currentTimeMillis()));
                LOGGER.info(project.getProjectId(), "Import completed");
            }
            catch (Exception e) {
                LOGGER.severe(e);
            }
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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Note getNoteForAnnotation(Annotation annotation, Optional<NoteId> inReplyTo) {
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
    
    private AnnotatableThing getAnnotatableThingForObjectId(NoteId noteId) {
        OWLNamedIndividual entity = project.getDataFactory().getOWLNamedIndividual(IRI.create(noteId.getLexicalForm()));
        return new DefaultComment(entity, notesOntology);
    }


    @Override
    public void deleteNoteAndReplies(NoteId noteId) {
        Annotation note = notesManager.getNote(noteId.getLexicalForm());
        if (note != null) {
            notesManager.deleteNote(noteId.getLexicalForm());
            project.getEventManager().postEvent(new NoteDeletedEvent(project.getProjectId(), noteId));
        }
    }


    @Override
    public void setNoteStatus(NoteId noteId, NoteStatus noteStatus) {
        Annotation note = notesManager.getNote(noteId.getLexicalForm());
        if(note == null) {
            // Sometimes we fail to find the note.  I'm not sure why.  This has something to do with the weird internals
            // and typing of the notes API.
            LOGGER.info(project.getProjectId(), "Failed to find note by Id when changing the note status.  The noteId was %s", noteId);
            return;
        }
        if(noteStatus == NoteStatus.OPEN) {
            note.setArchived(false);
        }
        else {
            note.setArchived(true);
        }
        project.getEventManager().postEvent(new NoteStatusChangedEvent(project.getProjectId(), noteId, noteStatus));
    }



    public int getDirectNotesCount(OWLEntity entity) {
        return getDiscusssionThread(entity).getRootNotes().size();
    }

    public int getIndirectNotesCount(OWLEntity entity) {
        return getDiscusssionThread(entity).size();
    }

    @Override
    public Note addReplyToNote(NoteId inReplyToId, NoteContent replyContent, UserId author) {
        return addReplyToNote(inReplyToId, replyContent, author, System.currentTimeMillis());
    }

    @Override
    public Note addReplyToNote(NoteId inReplyToId, NoteContent replyContent, UserId author, long timestamp) {
        try {
            AnnotatableThing target = getAnnotatableThingForObjectId(inReplyToId);
            Note note = addNoteToTarget(target, replyContent, author, timestamp);
            project.getEventManager().postEvent(new NotePostedEvent(project.getProjectId(), new NoteDetails(note.getHeader(), replyContent), Optional.of(inReplyToId)));
            return note;
        }
        catch (NotesException e) {
            throw new RuntimeException("Problem creating note: " + e.getMessage());
        }
    }

    @Override
    public Note addNoteToEntity(OWLEntity targetEntity, NoteContent noteContent, UserId author) {
        return addNoteToEntity(targetEntity, noteContent, author, System.currentTimeMillis());
    }

    @Override
    public Note addNoteToEntity(OWLEntity targetEntity, NoteContent noteContent, UserId author, long timestamp) {
        try {
            checkNotNull(targetEntity);
            checkNotNull(noteContent);
            checkNotNull(author);
            AnnotatableThing target = getAnnotatableThing(targetEntity);
            Note note = addNoteToTarget(target, noteContent, author, timestamp);
            OWLEntityData entityData = DataFactory.getOWLEntityData(targetEntity, project.getRenderingManager().getBrowserText(targetEntity));
            final NotePostedEvent evt = new NotePostedEvent(project.getProjectId(), Optional.of(entityData), new NoteDetails(note.getHeader(), note.getContent()));
            project.getEventManager().postEvent(evt);
            return note;
        }
        catch (NotesException e) {
            throw new RuntimeException("Problem creating note: " + e.getMessage());
        }
    }

    private Note addNoteToTarget(AnnotatableThing target, NoteContent noteContent, UserId author, long timestamp) throws NotesException {
        final String subject = noteContent.getSubject().or("");
        final String body = noteContent.getBody().or("");
        final org.protege.notesapi.notes.NoteType noteType = org.protege.notesapi.notes.NoteType.Comment;
        Annotation annotation = notesManager.createSimpleNote(noteType, subject, body, author.getUserName(), target);
        annotation.setCreatedAt(timestamp);
        final NoteId noteId = NoteId.createNoteIdFromLexicalForm(annotation.getId());
        NoteHeader noteHeader = new NoteHeader(noteId, Optional.<NoteId>absent(), author, timestamp);
        return Note.createNote(noteHeader, noteContent);
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
            if (annotation != null) {
                getAllNotesForAnnotation(annotation, Optional.<NoteId>absent(), result);
            }
        }
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
