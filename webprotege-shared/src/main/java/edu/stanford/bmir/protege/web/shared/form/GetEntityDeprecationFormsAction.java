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
 * 2020-10-21
 */
public class GetEntityDeprecationFormsAction implements ProjectAction<GetEntityDeprecationFormsResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    @GwtSerializationConstructor
    private GetEntityDeprecationFormsAction() {
    }

    public GetEntityDeprecationFormsAction(@Nonnull ProjectId projectId,
                                           @Nonnull OWLEntity entity) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }
}
