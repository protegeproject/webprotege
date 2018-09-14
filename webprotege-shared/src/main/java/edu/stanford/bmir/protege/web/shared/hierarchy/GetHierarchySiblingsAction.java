package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Sep 2018
 */
public class GetHierarchySiblingsAction extends AbstractHasProjectAction<GetHierarchySiblingsResult> {

    private OWLEntity entity;

    private HierarchyId hierarchyId;

    private PageRequest pageRequest;

    public GetHierarchySiblingsAction(ProjectId projectId, OWLEntity entity, HierarchyId hierarchyId, PageRequest pageRequest) {
        super(projectId);
        this.entity = checkNotNull(entity);
        this.hierarchyId = checkNotNull(hierarchyId);
        this.pageRequest = checkNotNull(pageRequest);
    }

    @GwtSerializationConstructor
    private GetHierarchySiblingsAction() {
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public HierarchyId getHierarchyId() {
        return hierarchyId;
    }

    @Nonnull
    public PageRequest getPageRequest() {
        return pageRequest;
    }
}
