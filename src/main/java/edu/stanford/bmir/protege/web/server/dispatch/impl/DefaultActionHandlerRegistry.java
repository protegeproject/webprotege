package edu.stanford.bmir.protege.web.server.dispatch.impl;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerNotFoundException;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerRegistry;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    @Inject
    public DefaultActionHandlerRegistry(Set<ActionHandler> handlers) {
        for(ActionHandler<?, ?> actionHandler : handlers) {
            register(actionHandler);
        }

//        register(new GetClientApplicationPropertiesActionHandler());

//        register(new GetUserIdsActionHandler(metaProjectManager));

//        register(new GetAvailableProjectsHandler());

//        register(new LoadProjectActionHandler());

//        register(new GetProjectEventsActionHandler());

//        register(new GetProjectSettingsActionHandler(projectMetadataManager));
//        register(new SetProjectSettingsActionHandler(projectMetadataManager));

//        register(new GetClassFrameActionHandler());
//        register(new UpdateClassFrameActionHandler());
        
//        register(new GetObjectPropertyFrameActionHandler());
//        register(new UpdateObjectPropertyFrameHandler());

//        register(new GetDataPropertyFrameActionHandler());
//        register(new UpdateDataPropertyFrameHandler());

//        register(new GetAnnotationPropertyFrameActionHandler());
//        register(new UpdateAnnotationPropertyFrameActionHandler());

//        register(new GetNamedIndividualFrameActionHandler());
//        register(new UpdateNamedIndividualFrameHandler());
        
//        register(new GetRootOntologyIdActionHandler());
//        register(new GetOntologyAnnotationsActionHandler());
//        register(new SetOntologyAnnotationsActionHandler());
//        register(new GetEntityAnnotationsActionHandler());

//        register(new DeleteEntityActionHandler());

        // Entity creation
//        register(new CreateClassActionHandler());
//        register(new CreateClassesActionHandler());
//        register(new CreateObjectPropertyActionHandler());
//        register(new CreateDataPropertiesActionHandler());
//        register(new CreateAnnotationPropertiesActionHandler());
//        register(new CreateNamedIndividualsActionHandler());

//        register(new LookupEntitiesActionHandler());

        // Watches
//        register(new AddWatchActionHandler());
//        register(new RemoveWatchActionHandler());


        // User stuff
//        register(new GetCurrentUserInSessionActionHandler());
//        register(new SetEmailAddressActionHandler());
//        register(new GetEmailAddressActionHandler());

        // Notes
//        register(new GetDiscussionThreadActionHandler());
//        register(new AddNoteToEntityActionHandler());
//        register(new AddReplyToNoteActionHandler());
//        register(new SetNoteStatusActionHandler());
//        register(new DeleteNoteActionHandler());


//        register(new MoveProjectsToTrashActionHandler());
//        register(new RemoveProjectsFromTrashActionHandler());

        // CSV
//        register(new GetCSVGridActionHandler());
//        register(new ImportCSVFileActionHandler());

        // Usage
//        register(new GetUsageActionHandler());
//
//        register(new GetIndividualsActionHandler());
//
//        register(new GetEntityRenderingActionHandler());

        // Metrics
//        register(new GetMetricsActionHandler());

        // TODO: Plugin
//        register(new GetEntityCrudKitsActionHandler());
//        register(new SetEntityCrudKitSettingsActionHandler());
//        register(new GetEntityCrudKitSettingsActionHandler());


//        register(new GetManchesterSyntaxFrameActionHandler());
//        register(new SetManchesterSyntaxFrameActionHandler());
//        register(new CheckManchesterSyntaxFrameActionHandler());
//        register(new GetManchesterSyntaxFrameCompletionsActionHandler());
//
//        register(new ResetPasswordActionHandler(metaProjectManager,
//                                                new ResetPasswordMailer(mailManager)));
    }


    private  <A extends Action<R>, R extends Result> void register(ActionHandler<A, R> handler) {
        registry.put(handler.getActionClass(), handler);
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
