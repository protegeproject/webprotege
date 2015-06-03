package edu.stanford.bmir.protege.web.server.inject.project;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class NotesOntologyDocumentProvider implements Provider<File> {

    private final File projectDirectory;

    private static final String NOTES_DATA_DIRECTORY_NAME = "notes-data";

    private static final String NOTES_ONTOLOGY_DOCUMENT_NAME = "notes-data.binary";

    @Inject
    public NotesOntologyDocumentProvider(@ProjectDirectory File projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    @Override
    public File get() {
        File notesDataDirectory = new File(projectDirectory, NOTES_DATA_DIRECTORY_NAME);
        return new File(notesDataDirectory, NOTES_ONTOLOGY_DOCUMENT_NAME);
    }
}
