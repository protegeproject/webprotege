package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.MergeEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.MergeEntitiesResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.MERGE_ENTITIES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2018
 */
public class MergeEntitiesActionHandler extends AbstractProjectChangeHandler<OWLEntity, MergeEntitiesAction, MergeEntitiesResult> {

    @Nonnull
    private final MergeEntitiesChangeListGeneratorFactory factory;

    @Inject
    public MergeEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                      @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull MergeEntitiesChangeListGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
        this.factory = checkNotNull(factory);
    }

    @Nonnull
    @Override
    public Class<MergeEntitiesAction> getActionClass() {
        return MergeEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(MergeEntitiesAction action) {
        return MERGE_ENTITIES;
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(MergeEntitiesAction action, ExecutionContext executionContext) {
        return factory.create(action.getSourceEntity(),
                              action.getTargetEntity(),
                              action.getTreatment(),
                              action.getCommitMessage());
    }

    @Override
    protected MergeEntitiesResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult, MergeEntitiesAction action,
                                                     ExecutionContext executionContext,
                                                     EventList<ProjectEvent<?>> eventList) {

        return new MergeEntitiesResult(eventList);
    }
}
