package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.SetEntityWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.SetEntityWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
public class SetEntityWatchesActionHandler extends AbstractHasProjectActionHandler<SetEntityWatchesAction, SetEntityWatchesResult> {

    @Inject
    public SetEntityWatchesActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }


    @Override
    public Class<SetEntityWatchesAction> getActionClass() {
        return SetEntityWatchesAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(SetEntityWatchesAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected SetEntityWatchesResult execute(SetEntityWatchesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        final EventManager<ProjectEvent<?>> eventManager = project.getEventManager();
        EventTag startTag = eventManager.getCurrentTag();
        WatchManager watchManager = project.getWatchManager();
        UserId userId = action.getUserId();
        Set<Watch<?>> watches = watchManager.getDirectWatches(action.getEntity(), userId);
        for(Watch<?> watch : watches) {
            watchManager.removeWatch(watch, userId);
        }
        for(Watch<?> watch : action.getWatches()) {
            watchManager.addWatch(watch, userId);
        }
        return new SetEntityWatchesResult(eventManager.getEventsFromTag(startTag));
    }

}
