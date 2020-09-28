package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-28
 */
public class GetEntityCreationFormsAction implements ProjectAction<GetEntityCreationFormsResult> {

    private ProjectId projectId;

    private OWLEntity parentEntity;

    public GetEntityCreationFormsAction(@Nonnull ProjectId projectId, OWLEntity parentEntity) {
        this.projectId = checkNotNull(projectId);
        this.parentEntity = checkNotNull(parentEntity);
    }

    @GwtSerializationConstructor
    private GetEntityCreationFormsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLEntity getParentEntity() {
        return parentEntity;
    }
}
