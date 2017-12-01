package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetHierarchyChildrenAction extends AbstractHasProjectAction<GetHierarchyChildrenResult> {

    private OWLEntity entity;

    public GetHierarchyChildrenAction(@Nonnull ProjectId projectId,
                                      @Nonnull OWLEntity entity) {
        super(projectId);
        this.entity = checkNotNull(entity);
    }

    @GwtSerializationConstructor
    private GetHierarchyChildrenAction() {
    }

    public OWLEntity getEntity() {
        return entity;
    }
}
