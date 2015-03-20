package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetDiscussionThreadAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetDiscussionThreadResult;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.notes.DiscussionThread;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class GetDiscussionThreadActionHandler extends AbstractHasProjectActionHandler<GetDiscussionThreadAction, GetDiscussionThreadResult> {

    @Inject
    public GetDiscussionThreadActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    public Class<GetDiscussionThreadAction> getActionClass() {
        return GetDiscussionThreadAction.class;
    }

    @Override
    protected RequestValidator<GetDiscussionThreadAction> getAdditionalRequestValidator(GetDiscussionThreadAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetDiscussionThreadResult execute(GetDiscussionThreadAction action, OWLAPIProject project, ExecutionContext executionContext) {
        final DiscussionThread discusssionThread = project.getNotesManager().getDiscusssionThread(action.getTargetEntity());
        return new GetDiscussionThreadResult(discusssionThread);
    }
}
