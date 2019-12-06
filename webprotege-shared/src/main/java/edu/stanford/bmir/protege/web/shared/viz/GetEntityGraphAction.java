package edu.stanford.bmir.protege.web.shared.viz;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public class GetEntityGraphAction implements ProjectAction<GetEntityGraphResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private EdgeCriteria edgeCriteria;

    public GetEntityGraphAction(@Nonnull ProjectId projectId,
                                @Nonnull OWLEntity entity,
                                EdgeCriteria edgeCriteria) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.edgeCriteria = checkNotNull(edgeCriteria);
    }

    @GwtSerializationConstructor
    private GetEntityGraphAction() {
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

    @Nonnull
    public EdgeCriteria getEdgeCriteria() {
        return edgeCriteria;
    }
}
