package edu.stanford.bmir.protege.web.server.dispatch.impl;

import edu.stanford.bmir.protege.web.client.dispatch.actions.*;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerNotFoundException;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.dispatch.handlers.*;
import edu.stanford.bmir.protege.web.server.events.GetProjectEventsActionHandler;
import edu.stanford.bmir.protege.web.server.notes.AddNoteToEntityActionHandler;
import edu.stanford.bmir.protege.web.server.notes.AddReplyToNoteActionHandler;
import edu.stanford.bmir.protege.web.server.notes.DeleteNoteActionHandler;
import edu.stanford.bmir.protege.web.server.notes.SetNoteStatusActionHandler;
import edu.stanford.bmir.protege.web.server.watches.AddWatchActionHandler;
import edu.stanford.bmir.protege.web.server.watches.RemoveWatchActionHandler;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityAction;
import edu.stanford.bmir.protege.web.shared.notes.AddReplyToNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.DeleteNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.SetNoteStatusAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.MoveProjectsToTrashAction;
import edu.stanford.bmir.protege.web.shared.project.RemoveProjectsFromTrashAction;
import edu.stanford.bmir.protege.web.shared.watches.AddWatchAction;
import edu.stanford.bmir.protege.web.shared.watches.RemoveWatchesAction;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public class DefaultActionHandlerRegistry implements ActionHandlerRegistry {

    // NOT a concurrent map.  This is only written to in the constructor. At runtime it's essentially immutable and the
    // basic maps are safe for multiple readers
    private Map<Class<?>, ActionHandler<?, ?>> registry = new HashMap<Class<?>, ActionHandler<?, ?>>();

    public DefaultActionHandlerRegistry() {

        register(new GetAvailableProjectsHandler(), GetAvailableProjectsAction.class);
        register(new LoadProjectActionHandler(), LoadProjectAction.class);

        register(new GetProjectEventsActionHandler(), GetProjectEventsAction.class);

        register(new GetClassFrameActionHandler(), GetClassFrameAction.class);
        register(new UpdateClassFrameActionHandler(), UpdateClassFrameAction.class);

        register(new GetNamedIndividualFrameActionHandler(), GetNamedIndividualFrameAction.class);
        register(new UpdateNamedIndividualFrameHandler(), UpdateNamedIndividualFrameAction.class);
        
        register(new GetRootOntologyIdActionHandler(), GetRootOntologyIdAction.class);
        register(new GetOntologyAnnotationsActionHandler(), GetOntologyAnnotationsAction.class);
        register(new GetEntityAnnotationsActionHandler(), GetEntityAnnotationsAction.class);

        register(new DeleteEntityActionHandler(), DeleteEntityAction.class);

        // Entity creation
        register(new CreateClassActionHandler(), CreateClassAction.class);
        register(new CreateClassesActionHandler(), CreateClassesAction.class);
        register(new CreateObjectPropertyActionHandler(), CreateObjectPropertiesAction.class);
        register(new CreateDataPropertiesActionHandler(), CreateDataPropertiesAction.class);
        register(new CreateAnnotationPropertiesActionHandler(), CreateAnnotationPropertiesAction.class);

        // Watches
        register(new AddWatchActionHandler(), AddWatchAction.class);
        register(new RemoveWatchActionHandler(), RemoveWatchesAction.class);


        // User stuff
        register(new GetCurrentUserInSessionActionHandler(), GetCurrentUserInSessionAction.class);

        // Notes
        register(new GetDiscussionThreadActionHandler(), GetDiscussionThreadAction.class);
        register(new AddNoteToEntityActionHandler(), AddNoteToEntityAction.class);
        register(new AddReplyToNoteActionHandler(), AddReplyToNoteAction.class);
        register(new SetNoteStatusActionHandler(), SetNoteStatusAction.class);
        register(new DeleteNoteActionHandler(), DeleteNoteAction.class);


        register(new MoveProjectsToTrashActionHandler(), MoveProjectsToTrashAction.class);
        register(new RemoveProjectsFromTrashActionHandler(), RemoveProjectsFromTrashAction.class);
    }


    private  <A extends Action<R>, R extends Result> void register(ActionHandler<A, R> handler, Class<A> forAction) {
        registry.put(forAction, handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A extends Action<R>, R extends Result> ActionHandler<A, R> getActionHandler(A action) {
        checkNotNull(action, "action must not be null");
        ActionHandler<A, R> handler = (ActionHandler<A, R>) registry.get(action.getClass());
        if(handler == null) {
            throw new ActionHandlerNotFoundException(action);
        }
        return handler;
    }
}
