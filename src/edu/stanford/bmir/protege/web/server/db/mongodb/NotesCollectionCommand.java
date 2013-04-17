package edu.stanford.bmir.protege.web.server.db.mongodb;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public abstract class NotesCollectionCommand<R, T extends Throwable> extends ProjectDBCollectionCommand<R, T> {

    private static final String NOTES_COLLECTION_NAME = "notes";

    public NotesCollectionCommand(ProjectId projectId) {
        super(projectId, NOTES_COLLECTION_NAME);
    }
}
