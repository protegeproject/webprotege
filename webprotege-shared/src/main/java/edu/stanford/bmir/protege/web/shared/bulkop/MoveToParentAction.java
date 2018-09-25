package edu.stanford.bmir.protege.web.shared.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MoveToParentAction implements ProjectAction<MoveToParentResult> {

    private ProjectId projectId;

    private ImmutableSet<? extends OWLEntity> entities;

    private OWLEntity entity;

    public MoveToParentAction(@Nonnull ProjectId projectId,
                              @Nonnull ImmutableSet<OWLClass> entities,
                              @Nonnull OWLClass entity) {
        this.projectId = checkNotNull(projectId);
        this.entities = checkNotNull(entities);
        this.entity = checkNotNull(entity);
    }

    private MoveToParentAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ImmutableSet<? extends OWLEntity> getEntities() {
        return entities;
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }
}
