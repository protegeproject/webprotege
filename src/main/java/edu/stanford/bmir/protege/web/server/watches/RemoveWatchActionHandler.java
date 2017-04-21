package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.watches.RemoveWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.RemoveWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class RemoveWatchActionHandler extends AbstractHasProjectActionHandler<RemoveWatchesAction, RemoveWatchesResult> {

    @Inject
    public RemoveWatchActionHandler(ProjectManager projectManager,
                                    AccessManager accessManager) {
        super(projectManager, accessManager);
    }


    @Override
    protected RemoveWatchesResult execute(RemoveWatchesAction action, Project project, ExecutionContext executionContext) {
        EventTag tag = project.getEventManager().getCurrentTag();
        for(Watch watch : action.getWatches()) {
            project.getWatchManager().removeWatch(watch);
        }
        return new RemoveWatchesResult(project.getEventManager().getEventsFromTag(tag));
    }

    @Override
    public Class<RemoveWatchesAction> getActionClass() {
        return RemoveWatchesAction.class;
    }
}
