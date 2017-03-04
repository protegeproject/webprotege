package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.change.GetWatchedEntityChangesAction;
import edu.stanford.bmir.protege.web.shared.change.GetWatchedEntityChangesResult;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class GetWatchedEntityChangesActionHandler extends AbstractHasProjectActionHandler<GetWatchedEntityChangesAction, GetWatchedEntityChangesResult> {

    @Inject
    public GetWatchedEntityChangesActionHandler(ProjectManager projectManager,
                                                AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_CHANGES;
    }

    @Override
    protected GetWatchedEntityChangesResult execute(GetWatchedEntityChangesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        Set<Watch<?>> watches = project.getWatchManager().getWatches(action.getUserId());
        ImmutableList<ProjectChange> changes = project.getWatchedChangesManager().getProjectChangesForWatches(watches);
        return new GetWatchedEntityChangesResult(changes);
    }

    @Override
    public Class<GetWatchedEntityChangesAction> getActionClass() {
        return GetWatchedEntityChangesAction.class;
    }
}
