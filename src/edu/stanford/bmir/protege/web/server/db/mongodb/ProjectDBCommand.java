package edu.stanford.bmir.protege.web.server.db.mongodb;

import com.mongodb.DB;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public abstract class ProjectDBCommand<R, T extends Throwable> {

    private ProjectId projectId;

    protected ProjectDBCommand(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public abstract R run(ProjectId projectId, DB db);

}
