package edu.stanford.bmir.protege.web.server.db.mongodb;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public abstract class ClassDiscussionThreadsCollectionCommand<R, T extends Throwable> extends ProjectDBCollectionCommand<R, T> {

    private static final String CLASS_DISSCUSSION_THREADS_COLLECTION_NAME = "cdiscs";

    public ClassDiscussionThreadsCollectionCommand(ProjectId projectId) {
        super(projectId, CLASS_DISSCUSSION_THREADS_COLLECTION_NAME);
    }

}
