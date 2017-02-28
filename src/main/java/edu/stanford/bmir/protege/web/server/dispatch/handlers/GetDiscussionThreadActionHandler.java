package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetDiscussionThreadAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetDiscussionThreadResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.notes.DiscussionThread;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_OBJECT_COMMENTS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class GetDiscussionThreadActionHandler extends AbstractHasProjectActionHandler<GetDiscussionThreadAction, GetDiscussionThreadResult> {

    @Inject
    public GetDiscussionThreadActionHandler(OWLAPIProjectManager projectManager,
                                            AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<GetDiscussionThreadAction> getActionClass() {
        return GetDiscussionThreadAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_OBJECT_COMMENTS;
    }

    @Override
    protected GetDiscussionThreadResult execute(GetDiscussionThreadAction action, OWLAPIProject project, ExecutionContext executionContext) {
        final DiscussionThread discusssionThread = project.getNotesManager().getDiscusssionThread(action.getTargetEntity());
        return new GetDiscussionThreadResult(discusssionThread);
    }
}
