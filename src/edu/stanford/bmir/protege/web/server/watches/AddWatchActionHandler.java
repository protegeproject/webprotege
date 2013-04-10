package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.watches.AddWatchAction;
import edu.stanford.bmir.protege.web.shared.watches.AddWatchResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class AddWatchActionHandler extends AbstractHasProjectActionHandler<AddWatchAction, AddWatchResult> {

    @Override
    public Class<AddWatchAction> getActionClass() {
        return AddWatchAction.class;
    }

    @Override
    protected RequestValidator<AddWatchAction> getAdditionalRequestValidator(AddWatchAction action, RequestContext requestContext) {
        return NullValidator.<AddWatchAction, AddWatchResult>get();
    }

    @Override
    protected AddWatchResult execute(AddWatchAction action, OWLAPIProject project, ExecutionContext executionContext) {
        final EventManager<ProjectEvent<?>> eventManager = project.getEventManager();
        EventTag startTag = eventManager.getCurrentTag();
        WatchManager watchManager = project.getWatchManager();
        watchManager.addWatch(action.getWatch(), action.getUserId());
        return new AddWatchResult(eventManager.getEventsFromTag(startTag));
    }
}
