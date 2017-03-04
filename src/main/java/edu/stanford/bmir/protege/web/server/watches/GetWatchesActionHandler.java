package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.watches.EntityBasedWatch;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.WATCH_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
public class GetWatchesActionHandler extends AbstractHasProjectActionHandler<GetWatchesAction, GetWatchesResult> {

    @Inject
    public GetWatchesActionHandler(ProjectManager projectManager,
                                   AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<GetWatchesAction> getActionClass() {
        return GetWatchesAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(WATCH_CHANGES, VIEW_PROJECT);
    }

    @Override
    protected GetWatchesResult execute(GetWatchesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        WatchManager watchManager = project.getWatchManager();
        Set<EntityBasedWatch> watches = new HashSet<>();
        for(Watch<?> watch : watchManager.getDirectWatches(action.getEntity(), action.getUserId())) {
            watches.add((EntityBasedWatch) watch);
        }
        return new GetWatchesResult(watches);
    }
}
