package edu.stanford.bmir.protege.web.shared.renderer;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-27
 */
public class GetEntityHtmlRenderingAction implements ProjectAction<GetEntityHtmlRenderingResult> {

    private ProjectId projectId;

    private OWLEntity entity;


    @GwtSerializationConstructor
    private GetEntityHtmlRenderingAction() {
    }

    public GetEntityHtmlRenderingAction(@Nonnull ProjectId projectId,
                                        @Nonnull OWLEntity entity) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }
}
