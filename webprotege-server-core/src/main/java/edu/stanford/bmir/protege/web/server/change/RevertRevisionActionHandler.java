package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.change.RevertRevisionAction;
import edu.stanford.bmir.protege.web.shared.change.RevertRevisionResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
public class RevertRevisionActionHandler extends AbstractProjectChangeHandler<Boolean, RevertRevisionAction, RevertRevisionResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RevisionReverterChangeListGeneratorFactory factory;

    @Inject
    public RevertRevisionActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                       @Nonnull HasApplyChanges applyChanges,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull RevisionReverterChangeListGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
        this.projectId = projectId;
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<RevertRevisionAction> getActionClass() {
        return RevertRevisionAction.class;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(RevertRevisionAction action,
                                                                    ExecutionContext executionContext) {
        RevisionNumber revisionNumber = action.getRevisionNumber();
        return factory.create(revisionNumber);
    }

    @Override
    protected RevertRevisionResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult,
                                                      RevertRevisionAction action,
                                                      ExecutionContext executionContext,
                                                      EventList<ProjectEvent<?>> eventList) {
        return RevertRevisionResult.get(projectId, action.getRevisionNumber(), eventList);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(RevertRevisionAction action) {
        return BuiltInAction.REVERT_CHANGES;
    }
}
