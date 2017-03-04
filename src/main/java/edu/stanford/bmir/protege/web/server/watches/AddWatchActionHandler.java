package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.watches.AddWatchAction;
import edu.stanford.bmir.protege.web.shared.watches.AddWatchResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class AddWatchActionHandler extends AbstractHasProjectActionHandler<AddWatchAction, AddWatchResult> {

    @Inject
    public AddWatchActionHandler(ProjectManager projectManager, AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<AddWatchAction> getActionClass() {
        return AddWatchAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.WATCH_CHANGES;
    }

    @Nonnull
    @Override
    protected RequestValidator getAdditionalRequestValidator(AddWatchAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected AddWatchResult execute(AddWatchAction action, Project project, ExecutionContext executionContext) {
        final EventManager<ProjectEvent<?>> eventManager = project.getEventManager();
        EventTag startTag = eventManager.getCurrentTag();
        WatchManager watchManager = project.getWatchManager();
        watchManager.addWatch(action.getWatch(), action.getUserId());
        return new AddWatchResult(eventManager.getEventsFromTag(startTag));
    }
}
