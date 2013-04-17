package edu.stanford.bmir.protege.web.server.db.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public abstract class ProjectDBCollectionCommand<R, T extends Throwable> extends ProjectDBCommand<R, T> {

    private String collectionName;

    protected ProjectDBCollectionCommand(ProjectId projectId, String collectionName) {
        super(projectId);
        this.collectionName = collectionName;
    }

    @Override
    final public R run(ProjectId projectId, DB db) {
        DBCollection collection = db.getCollection(collectionName);
        return run(projectId, collection);
    }

    public abstract R run(ProjectId projectId, DBCollection dbCollection);


}
