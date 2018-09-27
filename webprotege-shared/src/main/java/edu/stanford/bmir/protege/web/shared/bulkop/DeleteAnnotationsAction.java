package edu.stanford.bmir.protege.web.shared.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
public class DeleteAnnotationsAction implements ProjectAction<DeleteAnnotationsResult> {

    private ProjectId projectId;

    private ImmutableSet<OWLEntity> entities;

    private AnnotationSimpleMatchingCriteria criteria;

    public DeleteAnnotationsAction(@Nonnull ProjectId projectId,
                                   @Nonnull ImmutableSet<OWLEntity> entities,
                                   @Nonnull AnnotationSimpleMatchingCriteria criteria) {
        this.projectId = checkNotNull(projectId);
        this.entities = checkNotNull(entities);
        this.criteria = checkNotNull(criteria);
    }

    @GwtSerializationConstructor
    private DeleteAnnotationsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ImmutableSet<OWLEntity> getEntities() {
        return entities;
    }

    @Nonnull
    public AnnotationSimpleMatchingCriteria getCriteria() {
        return criteria;
    }
}
