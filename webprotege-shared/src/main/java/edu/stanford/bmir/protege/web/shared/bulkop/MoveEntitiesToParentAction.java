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
public class MoveEntitiesToParentAction implements ProjectAction<MoveEntitiesToParentResult>, HasCommitMessage {

    private ProjectId projectId;

    private ImmutableSet<? extends OWLEntity> entities;

    private OWLEntity entity;

    private String commitMessage;

    public MoveEntitiesToParentAction(@Nonnull ProjectId projectId,
                                      @Nonnull ImmutableSet<OWLClass> entities,
                                      @Nonnull OWLClass entity,
                                      @Nonnull String commitMessage) {
        this.projectId = checkNotNull(projectId);
        this.entities = checkNotNull(entities);
        this.entity = checkNotNull(entity);
        this.commitMessage = checkNotNull(commitMessage);
    }

    private MoveEntitiesToParentAction() {
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

    @Nonnull
    @Override
    public String getCommitMessage() {
        return commitMessage;
    }
}
