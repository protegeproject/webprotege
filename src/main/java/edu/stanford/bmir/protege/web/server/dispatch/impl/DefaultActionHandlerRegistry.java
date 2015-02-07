package edu.stanford.bmir.protege.web.server.dispatch.impl;

import edu.stanford.bmir.protege.web.client.dispatch.actions.*;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.app.App;
import edu.stanford.bmir.protege.web.server.app.GetClientApplicationPropertiesActionHandler;
import edu.stanford.bmir.protege.web.server.chgpwd.ResetPasswordActionHandler;
import edu.stanford.bmir.protege.web.server.chgpwd.ResetPasswordMailer;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitsActionHandler;
import edu.stanford.bmir.protege.web.server.crud.SetEntityCrudKitSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.csv.GetCSVGridActionHandler;
import edu.stanford.bmir.protege.web.server.csv.ImportCSVFileActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerNotFoundException;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.dispatch.handlers.*;
import edu.stanford.bmir.protege.web.server.entities.LookupEntitiesActionHandler;
import edu.stanford.bmir.protege.web.server.events.GetProjectEventsActionHandler;
import edu.stanford.bmir.protege.web.server.frame.*;
import edu.stanford.bmir.protege.web.server.individuals.CreateNamedIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.individuals.GetIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.mail.GetEmailAddressActionHandler;
import edu.stanford.bmir.protege.web.server.mail.MailManager;
import edu.stanford.bmir.protege.web.server.mail.SetEmailAddressActionHandler;
import edu.stanford.bmir.protege.web.server.notes.AddNoteToEntityActionHandler;
import edu.stanford.bmir.protege.web.server.notes.AddReplyToNoteActionHandler;
import edu.stanford.bmir.protege.web.server.notes.DeleteNoteActionHandler;
import edu.stanford.bmir.protege.web.server.notes.SetNoteStatusActionHandler;
import edu.stanford.bmir.protege.web.server.metrics.GetMetricsActionHandler;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectMetadataManager;
import edu.stanford.bmir.protege.web.server.projectsettings.GetProjectSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.projectsettings.SetProjectSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.render.GetEntityRenderingActionHandler;
import edu.stanford.bmir.protege.web.server.usage.GetUsageActionHandler;
import edu.stanford.bmir.protege.web.server.user.GetUserIdsActionHandler;
import edu.stanford.bmir.protege.web.server.watches.AddWatchActionHandler;
import edu.stanford.bmir.protege.web.server.watches.RemoveWatchActionHandler;
import edu.stanford.bmir.protege.web.shared.app.GetClientApplicationPropertiesAction;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsAction;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.csv.GetCSVGridAction;
import edu.stanford.bmir.protege.web.shared.csv.ImportCSVFileAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.LookupEntitiesAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.metrics.GetMetricsAction;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityAction;
import edu.stanford.bmir.protege.web.shared.notes.AddReplyToNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.DeleteNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.SetNoteStatusAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.MoveProjectsToTrashAction;
import edu.stanford.bmir.protege.web.shared.project.RemoveProjectsFromTrashAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageAction;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsAction;
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

    private final MailManager mailManager = App.get().getMailManager();

    private final MetaProjectManager metaProjectManager = MetaProjectManager.getManager();

    private final OWLAPIProjectMetadataManager projectMetadataManager = OWLAPIProjectMetadataManager.getManager();

    // NOT a concurrent map.  This is only written to in the constructor. At runtime it's essentially immutable and the
    // basic maps are safe for multiple readers
    private Map<Class<?>, ActionHandler<?, ?>> registry = new HashMap<Class<?>, ActionHandler<?, ?>>();

    public DefaultActionHandlerRegistry() {

        register(new GetClientApplicationPropertiesActionHandler());

        register(new GetUserIdsActionHandler(metaProjectManager));

        register(new GetAvailableProjectsHandler());

        register(new LoadProjectActionHandler());

        register(new GetProjectEventsActionHandler());

        register(new GetProjectSettingsActionHandler(projectMetadataManager));
        register(new SetProjectSettingsActionHandler(projectMetadataManager));

        register(new GetClassFrameActionHandler());
        register(new UpdateClassFrameActionHandler());
        
        register(new GetObjectPropertyFrameActionHandler());
        register(new UpdateObjectPropertyFrameHandler());

        register(new GetDataPropertyFrameActionHandler());
        register(new UpdateDataPropertyFrameHandler());

        register(new GetAnnotationPropertyFrameActionHandler());
        register(new UpdateAnnotationPropertyFrameActionHandler());

        register(new GetNamedIndividualFrameActionHandler());
        register(new UpdateNamedIndividualFrameHandler());
        
        register(new GetRootOntologyIdActionHandler());
        register(new GetOntologyAnnotationsActionHandler());
        register(new SetOntologyAnnotationsActionHandler());
        register(new GetEntityAnnotationsActionHandler());

        register(new DeleteEntityActionHandler());

        // Entity creation
        register(new CreateClassActionHandler());
        register(new CreateClassesActionHandler());
        register(new CreateObjectPropertyActionHandler());
        register(new CreateDataPropertiesActionHandler());
        register(new CreateAnnotationPropertiesActionHandler());
        register(new CreateNamedIndividualsActionHandler());

        register(new LookupEntitiesActionHandler());

        // Watches
        register(new AddWatchActionHandler());
        register(new RemoveWatchActionHandler());


        // User stuff
        register(new GetCurrentUserInSessionActionHandler());
        register(new SetEmailAddressActionHandler());
        register(new GetEmailAddressActionHandler());

        // Notes
        register(new GetDiscussionThreadActionHandler());
        register(new AddNoteToEntityActionHandler());
        register(new AddReplyToNoteActionHandler());
        register(new SetNoteStatusActionHandler());
        register(new DeleteNoteActionHandler());


        register(new MoveProjectsToTrashActionHandler());
        register(new RemoveProjectsFromTrashActionHandler());

        // CSV
        register(new GetCSVGridActionHandler());
        register(new ImportCSVFileActionHandler());

        // Usage
        register(new GetUsageActionHandler());

        register(new GetIndividualsActionHandler());

        register(new GetEntityRenderingActionHandler());

        // Metrics
        register(new GetMetricsActionHandler());

        // TODO: Plugin
        register(new GetEntityCrudKitsActionHandler());
        register(new SetEntityCrudKitSettingsActionHandler());
        register(new GetEntityCrudKitSettingsActionHandler());


        register(new GetManchesterSyntaxFrameActionHandler());
        register(new SetManchesterSyntaxFrameActionHandler());
        register(new CheckManchesterSyntaxFrameActionHandler());
        register(new GetManchesterSyntaxFrameCompletionsActionHandler());

        register(new ResetPasswordActionHandler(metaProjectManager,
                                                new ResetPasswordMailer(mailManager)));
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
