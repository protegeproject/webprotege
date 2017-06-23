package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class CreateEntityDiscussionThreadAction implements ProjectAction<CreateEntityDiscussionThreadResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private String comment;

    public CreateEntityDiscussionThreadAction(@Nonnull ProjectId projectId,
                                              @Nonnull OWLEntity entity,
                                              @Nonnull String comment) {
        this.entity = checkNotNull(entity);
        this.comment = checkNotNull(comment);
        this.projectId = checkNotNull(projectId);
    }

    public static CreateEntityDiscussionThreadAction createEntityDiscussionThread(@Nonnull ProjectId projectId,
                                                                                  @Nonnull OWLEntity entity,
                                                                                  @Nonnull String comment) {
        return new CreateEntityDiscussionThreadAction(projectId, entity, comment);
    }

    @GwtSerializationConstructor
    public CreateEntityDiscussionThreadAction() {
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public String getComment() {
        return comment;
    }
}
