package edu.stanford.bmir.protege.web.server.owlapi.notes.impl;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.notes.OWLAPINotesManagerNotesAPIImpl;
import edu.stanford.bmir.protege.web.server.owlapi.notes.api.Note;
import edu.stanford.bmir.protege.web.server.owlapi.notes.api.NoteStore;
import org.protege.notesapi.NotesException;
import org.protege.notesapi.NotesManager;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/08/2012
 * <p>
 *     Loads legacy notes
 * </p>
 */
public class NoteStoreNotesAPIImpl {

    public static final String CHANGES_ONTOLOGY_FILE_NAME = "changes.owl";


    private OWLAPIProject project;

    private OWLOntology notesOntology;

    private final NotesManager notesManager;

    private static final String NOTES_ONTOLOGY_DOCUMENT_NAME = "notes-data.binary";

    private File notesOntologyDocument;


    public NoteStoreNotesAPIImpl(OWLAPIProject project) {
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addNote(Note note) {

    }

    public void removeNote(Note note) {
    }

    public Iterator<Note> iterator() {
        return null;
    }
}
