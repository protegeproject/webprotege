package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.watches.RemoveWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.RemoveWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class RemoveWatchActionHandler extends AbstractHasProjectActionHandler<RemoveWatchesAction, RemoveWatchesResult> {

    @Override
    protected RequestValidator<RemoveWatchesAction> getAdditionalRequestValidator(RemoveWatchesAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected RemoveWatchesResult execute(RemoveWatchesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        EventTag tag = project.getEventManager().getCurrentTag();
        for(Watch<?> watch : action.getWatches()) {
            project.getWatchManager().removeWatch(watch, action.getUserId());
        }
        return new RemoveWatchesResult(project.getEventManager().getEventsFromTag(tag));
    }

    @Override
    public Class<RemoveWatchesAction> getActionClass() {
        return RemoveWatchesAction.class;
    }
}
