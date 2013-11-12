package edu.stanford.bmir.protege.web.server.dispatch.impl;

import edu.stanford.bmir.protege.web.client.dispatch.actions.*;
import edu.stanford.bmir.protege.web.server.app.GetClientApplicationPropertiesActionHandler;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitsActionHandler;
import edu.stanford.bmir.protege.web.server.crud.SetEntityCrudKitSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.csv.GetCSVGridActionHandler;
import edu.stanford.bmir.protege.web.server.csv.ImportCSVFileActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerNotFoundException;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.dispatch.handlers.*;
import edu.stanford.bmir.protege.web.server.events.GetProjectEventsActionHandler;
import edu.stanford.bmir.protege.web.server.individuals.CreateNamedIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.individuals.GetIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.mail.GetEmailAddressActionHandler;
import edu.stanford.bmir.protege.web.server.mail.SetEmailAddressActionHandler;
import edu.stanford.bmir.protege.web.server.notes.AddNoteToEntityActionHandler;
import edu.stanford.bmir.protege.web.server.notes.AddReplyToNoteActionHandler;
import edu.stanford.bmir.protege.web.server.notes.DeleteNoteActionHandler;
import edu.stanford.bmir.protege.web.server.notes.SetNoteStatusActionHandler;
import edu.stanford.bmir.protege.web.server.usage.GetUsageActionHandler;
import edu.stanford.bmir.protege.web.server.watches.AddWatchActionHandler;
import edu.stanford.bmir.protege.web.server.watches.RemoveWatchActionHandler;
import edu.stanford.bmir.protege.web.shared.app.GetClientApplicationPropertiesAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsAction;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.csv.GetCSVGridAction;
import edu.stanford.bmir.protege.web.shared.csv.ImportCSVFileAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityAction;
import edu.stanford.bmir.protege.web.shared.notes.AddReplyToNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.DeleteNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.SetNoteStatusAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.MoveProjectsToTrashAction;
import edu.stanford.bmir.protege.web.shared.project.RemoveProjectsFromTrashAction;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageAction;
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

        register(new GetClientApplicationPropertiesActionHandler(), GetClientApplicationPropertiesAction.class);

        register(new GetAvailableProjectsHandler(), GetAvailableProjectsAction.class);

        register(new LoadProjectActionHandler(), LoadProjectAction.class);

        register(new GetProjectEventsActionHandler(), GetProjectEventsAction.class);

        register(new GetClassFrameActionHandler(), GetClassFrameAction.class);
        register(new UpdateClassFrameActionHandler(), UpdateClassFrameAction.class);
        
        register(new GetObjectPropertyFrameActionHandler(), GetObjectPropertyFrameAction.class);
        register(new UpdateObjectPropertyFrameHandler(), UpdateObjectPropertyFrameAction.class);

        register(new GetDataPropertyFrameActionHandler(), GetDataPropertyFrameAction.class);
        register(new UpdateDataPropertyFrameHandler(), UpdateDataPropertyFrameAction.class);

        register(new GetAnnotationPropertyFrameActionHandler(), GetAnnotationPropertyFrameAction.class);
        register(new UpdateAnnotationPropertyFrameActionHandler(), UpdateAnnotationPropertyFrameAction.class);

        register(new GetNamedIndividualFrameActionHandler(), GetNamedIndividualFrameAction.class);
        register(new UpdateNamedIndividualFrameHandler(), UpdateNamedIndividualFrameAction.class);
        
        register(new GetRootOntologyIdActionHandler(), GetRootOntologyIdAction.class);
        register(new GetOntologyAnnotationsActionHandler(), GetOntologyAnnotationsAction.class);
        register(new SetOntologyAnnotationsActionHandler(), SetOntologyAnnotationsAction.class);
        register(new GetEntityAnnotationsActionHandler(), GetEntityAnnotationsAction.class);

        register(new DeleteEntityActionHandler(), DeleteEntityAction.class);

        // Entity creation
        register(new CreateClassActionHandler(), CreateClassAction.class);
        register(new CreateClassesActionHandler(), CreateClassesAction.class);
        register(new CreateObjectPropertyActionHandler(), CreateObjectPropertiesAction.class);
        register(new CreateDataPropertiesActionHandler(), CreateDataPropertiesAction.class);
        register(new CreateAnnotationPropertiesActionHandler(), CreateAnnotationPropertiesAction.class);
        register(new CreateNamedIndividualsActionHandler(), CreateNamedIndividualsAction.class);

        // Watches
        register(new AddWatchActionHandler(), AddWatchAction.class);
        register(new RemoveWatchActionHandler(), RemoveWatchesAction.class);


        // User stuff
        register(new GetCurrentUserInSessionActionHandler(), GetCurrentUserInSessionAction.class);
        register(new SetEmailAddressActionHandler(), SetEmailAddressAction.class);
        register(new GetEmailAddressActionHandler(), GetEmailAddressAction.class);

        // Notes
        register(new GetDiscussionThreadActionHandler(), GetDiscussionThreadAction.class);
        register(new AddNoteToEntityActionHandler(), AddNoteToEntityAction.class);
        register(new AddReplyToNoteActionHandler(), AddReplyToNoteAction.class);
        register(new SetNoteStatusActionHandler(), SetNoteStatusAction.class);
        register(new DeleteNoteActionHandler(), DeleteNoteAction.class);


        register(new MoveProjectsToTrashActionHandler(), MoveProjectsToTrashAction.class);
        register(new RemoveProjectsFromTrashActionHandler(), RemoveProjectsFromTrashAction.class);

        // CSV
        register(new GetCSVGridActionHandler(), GetCSVGridAction.class);
        register(new ImportCSVFileActionHandler(), ImportCSVFileAction.class);

        // Usage
        register(new GetUsageActionHandler(), GetUsageAction.class);

        register(new GetIndividualsActionHandler(), GetIndividualsAction.class);


        // TODO: Plugin
        register(new GetEntityCrudKitsActionHandler(), GetEntityCrudKitsAction.class);
        register(new SetEntityCrudKitSettingsActionHandler(), SetEntityCrudKitSettingsAction.class);
        register(new GetEntityCrudKitSettingsActionHandler(), GetEntityCrudKitSettingsAction.class);
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
