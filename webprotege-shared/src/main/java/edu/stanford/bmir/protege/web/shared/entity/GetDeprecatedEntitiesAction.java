package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
public class GetDeprecatedEntitiesAction implements ProjectAction<GetDeprecatedEntitiesResult> {

    private ProjectId projectId;

    private PageRequest pageRequest;

    private Set<EntityType<?>> entityTypes;

    @GwtSerializationConstructor
    private GetDeprecatedEntitiesAction() {
    }

    public GetDeprecatedEntitiesAction(@Nonnull ProjectId projectId,
                                       @Nonnull PageRequest pageRequest,
                                       @Nonnull Set<EntityType<?>> entityTypes) {
        this.projectId = checkNotNull(projectId);
        this.pageRequest = checkNotNull(pageRequest);
        this.entityTypes = new HashSet<>(checkNotNull(entityTypes));
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public PageRequest getPageRequest() {
        return pageRequest;
    }

    public Set<EntityType<?>> getEntityTypes() {
        return new HashSet<>(entityTypes);
    }
}
