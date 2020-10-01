package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.MoveHierarchyNodeAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.MoveHierarchyNodeResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
public class MoveHierarchyNodeActionHandler extends AbstractProjectChangeHandler<Boolean, MoveHierarchyNodeAction, MoveHierarchyNodeResult> {

    private final MoveEntityChangeListGeneratorFactory factory;

    @Inject
    public MoveHierarchyNodeActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                          @Nonnull HasApplyChanges applyChanges,
                                          @Nonnull MoveEntityChangeListGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<MoveHierarchyNodeAction> getActionClass() {
        return MoveHierarchyNodeAction.class;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(MoveHierarchyNodeAction action, ExecutionContext executionContext) {
        return factory.create(action);
    }

    @Override
    protected MoveHierarchyNodeResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult, MoveHierarchyNodeAction action, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        return new MoveHierarchyNodeResult(changeApplicationResult.getSubject(),
                                           eventList);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(MoveHierarchyNodeAction action) {
        return EDIT_ONTOLOGY;
    }
}
