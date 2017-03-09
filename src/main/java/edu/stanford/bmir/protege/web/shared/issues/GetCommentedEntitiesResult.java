package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public class GetCommentedEntitiesResult implements Result {

    private ProjectId projectId;

    private Page<CommentedEntityData> entities;

    @GwtSerializationConstructor
    private GetCommentedEntitiesResult() {
    }

    public GetCommentedEntitiesResult(ProjectId projectId,
                                      Page<CommentedEntityData> entities) {
        this.projectId = projectId;
        this.entities = entities;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public Page<CommentedEntityData> getEntities() {
        return entities;
    }
}
