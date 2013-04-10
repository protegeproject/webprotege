package edu.stanford.bmir.protege.web.client.dispatch;


import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.HasProjectId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class AbstractProjectSpecificResult<T extends Serializable> extends AbstractObjectResult<T> implements HasProjectId {

    private ProjectId projectId;

    /**
     * For serialization only
     */
    protected AbstractProjectSpecificResult() {
    }

    public AbstractProjectSpecificResult(ProjectId projectId, T object) {
        super(object);
        this.projectId = projectId;
    }

    /**
     * Get the {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.
     * @return The {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.  Not {@code null}.
     */
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }
}
