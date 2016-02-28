package edu.stanford.bmir.protege.web.server.dispatch;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import edu.stanford.bmir.protege.web.server.app.GetClientApplicationPropertiesActionHandler;
import edu.stanford.bmir.protege.web.server.auth.ChangePasswordActionHandler;
import edu.stanford.bmir.protege.web.server.auth.GetChapSessionActionHandler;
import edu.stanford.bmir.protege.web.server.auth.PerformLoginActionHandler;
import edu.stanford.bmir.protege.web.server.change.GetProjectChangesActionHandler;
import edu.stanford.bmir.protege.web.server.change.GetWatchedEntityChangesActionHandler;
import edu.stanford.bmir.protege.web.server.change.RevertRevisionActionHandler;
import edu.stanford.bmir.protege.web.server.chgpwd.ResetPasswordActionHandler;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitsActionHandler;
import edu.stanford.bmir.protege.web.server.crud.SetEntityCrudKitSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.csv.GetCSVGridActionHandler;
import edu.stanford.bmir.protege.web.server.csv.ImportCSVFileActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.handlers.*;
import edu.stanford.bmir.protege.web.server.entities.LookupEntitiesActionHandler;
import edu.stanford.bmir.protege.web.server.events.GetProjectEventsActionHandler;
import edu.stanford.bmir.protege.web.server.frame.*;
import edu.stanford.bmir.protege.web.server.individuals.CreateNamedIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.individuals.GetIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.itemlist.GetPersonIdCompletionsActionHandler;
import edu.stanford.bmir.protege.web.server.itemlist.GetPersonIdItemsActionHandler;
import edu.stanford.bmir.protege.web.server.itemlist.GetUserIdCompletionsActionHandler;
import edu.stanford.bmir.protege.web.server.mail.GetEmailAddressActionHandler;
import edu.stanford.bmir.protege.web.server.mail.SetEmailAddressActionHandler;
import edu.stanford.bmir.protege.web.server.merge.ComputeProjectMergeActionHandler;
import edu.stanford.bmir.protege.web.server.merge.MergeUploadedProjectActionHandler;
import edu.stanford.bmir.protege.web.server.metrics.GetMetricsActionHandler;
import edu.stanford.bmir.protege.web.server.notes.AddNoteToEntityActionHandler;
import edu.stanford.bmir.protege.web.server.notes.AddReplyToNoteActionHandler;
import edu.stanford.bmir.protege.web.server.notes.DeleteNoteActionHandler;
import edu.stanford.bmir.protege.web.server.notes.SetNoteStatusActionHandler;
import edu.stanford.bmir.protege.web.server.permissions.GetPermissionsActionHandler;
import edu.stanford.bmir.protege.web.server.perspective.GetPerspectiveLayoutActionHandler;
import edu.stanford.bmir.protege.web.server.perspective.GetPerspectivesActionHandler;
import edu.stanford.bmir.protege.web.server.perspective.SetPerspectiveLayoutActionHandler;
import edu.stanford.bmir.protege.web.server.perspective.SetPerspectivesActionHandler;
import edu.stanford.bmir.protege.web.server.project.CreateNewProjectActionHandler;
import edu.stanford.bmir.protege.web.server.project.GetUIConfigurationActionHandler;
import edu.stanford.bmir.protege.web.server.projectsettings.GetProjectSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.projectsettings.SetProjectSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.render.GetEntityRenderingActionHandler;
import edu.stanford.bmir.protege.web.server.renderer.GetEntityDataActionHandler;
import edu.stanford.bmir.protege.web.server.sharing.GetProjectSharingSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.sharing.SetProjectSharingSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.usage.GetUsageActionHandler;
import edu.stanford.bmir.protege.web.server.user.CreateUserAccountActionHandler;
import edu.stanford.bmir.protege.web.server.user.GetUserIdsActionHandler;
import edu.stanford.bmir.protege.web.server.user.LogOutUserActionHandler;
import edu.stanford.bmir.protege.web.server.watches.AddWatchActionHandler;
import edu.stanford.bmir.protege.web.server.watches.RemoveWatchActionHandler;
import edu.stanford.bmir.protege.web.server.project.SetUIConfigurationActionHandler;
import edu.stanford.bmir.protege.web.server.revision.*;
import edu.stanford.bmir.protege.web.shared.frame.GetOntologyFramesAction;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveLayoutAction;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectivesAction;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ActionHandlersModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<ActionHandler> multibinder = Multibinder.newSetBinder(binder(), ActionHandler.class);
        multibinder.addBinding().to(GetClientApplicationPropertiesActionHandler.class);
        multibinder.addBinding().to(GetUserIdsActionHandler.class);

        multibinder.addBinding().to(GetAvailableProjectsHandler.class);
        multibinder.addBinding().to(LoadProjectActionHandler.class);

        multibinder.addBinding().to(CreateNewProjectActionHandler.class);

        multibinder.addBinding().to(GetProjectEventsActionHandler.class);

        multibinder.addBinding().to(GetProjectSettingsActionHandler.class);
        multibinder.addBinding().to(SetProjectSettingsActionHandler.class);

        multibinder.addBinding().to(GetUIConfigurationActionHandler.class);
        multibinder.addBinding().to(SetUIConfigurationActionHandler.class);

        multibinder.addBinding().to(GetClassFrameActionHandler.class);
        multibinder.addBinding().to(UpdateClassFrameActionHandler.class);

        multibinder.addBinding().to(GetObjectPropertyFrameActionHandler.class);
        multibinder.addBinding().to(UpdateObjectPropertyFrameHandler.class);

        multibinder.addBinding().to(GetDataPropertyFrameActionHandler.class);
        multibinder.addBinding().to(UpdateDataPropertyFrameHandler.class);

        multibinder.addBinding().to(GetAnnotationPropertyFrameActionHandler.class);
        multibinder.addBinding().to(UpdateAnnotationPropertyFrameActionHandler.class);

        multibinder.addBinding().to(GetNamedIndividualFrameActionHandler.class);
        multibinder.addBinding().to(UpdateNamedIndividualFrameHandler.class);

        multibinder.addBinding().to(GetOntologyFramesActionHandler.class);

        multibinder.addBinding().to(GetRootOntologyIdActionHandler.class);
        multibinder.addBinding().to(GetOntologyAnnotationsActionHandler.class);
        multibinder.addBinding().to(SetOntologyAnnotationsActionHandler.class);
        multibinder.addBinding().to(GetEntityAnnotationsActionHandler.class);

        multibinder.addBinding().to(DeleteEntityActionHandler.class);

        multibinder.addBinding().to(CreateClassActionHandler.class);
        multibinder.addBinding().to(CreateClassesActionHandler.class);
        multibinder.addBinding().to(CreateObjectPropertyActionHandler.class);
        multibinder.addBinding().to(CreateDataPropertiesActionHandler.class);
        multibinder.addBinding().to(CreateAnnotationPropertiesActionHandler.class);
        multibinder.addBinding().to(CreateNamedIndividualsActionHandler.class);

        multibinder.addBinding().to(LookupEntitiesActionHandler.class);

        multibinder.addBinding().to(AddWatchActionHandler.class);
        multibinder.addBinding().to(RemoveWatchActionHandler.class);

        multibinder.addBinding().to(GetCurrentUserInSessionActionHandler.class);
        multibinder.addBinding().to(SetEmailAddressActionHandler.class);
        multibinder.addBinding().to(GetEmailAddressActionHandler.class);

        multibinder.addBinding().to(GetDiscussionThreadActionHandler.class);
        multibinder.addBinding().to(AddNoteToEntityActionHandler.class);
        multibinder.addBinding().to(AddReplyToNoteActionHandler.class);
        multibinder.addBinding().to(SetNoteStatusActionHandler.class);
        multibinder.addBinding().to(DeleteNoteActionHandler.class);

        multibinder.addBinding().to(MoveProjectsToTrashActionHandler.class);
        multibinder.addBinding().to(RemoveProjectsFromTrashActionHandler.class);

        multibinder.addBinding().to(GetCSVGridActionHandler.class);
        multibinder.addBinding().to(ImportCSVFileActionHandler.class);

        multibinder.addBinding().to(GetUsageActionHandler.class);

        multibinder.addBinding().to(GetIndividualsActionHandler.class);

        multibinder.addBinding().to(GetEntityRenderingActionHandler.class);
        multibinder.addBinding().to(GetEntityDataActionHandler.class);


        multibinder.addBinding().to(GetDataPropertyFrameActionHandler.class);

        multibinder.addBinding().to(GetMetricsActionHandler.class);

        multibinder.addBinding().to(GetEntityCrudKitsActionHandler.class);
        multibinder.addBinding().to(SetEntityCrudKitSettingsActionHandler.class);
        multibinder.addBinding().to(GetEntityCrudKitSettingsActionHandler.class);

        multibinder.addBinding().to(GetManchesterSyntaxFrameActionHandler.class);
        multibinder.addBinding().to(SetManchesterSyntaxFrameActionHandler.class);
        multibinder.addBinding().to(CheckManchesterSyntaxFrameActionHandler.class);
        multibinder.addBinding().to(GetManchesterSyntaxFrameCompletionsActionHandler.class);

        multibinder.addBinding().to(ResetPasswordActionHandler.class);

        multibinder.addBinding().to(GetProjectSharingSettingsActionHandler.class);

        multibinder.addBinding().to(SetProjectSharingSettingsActionHandler.class);

        multibinder.addBinding().to(LogOutUserActionHandler.class);
        multibinder.addBinding().to(GetChapSessionActionHandler.class);

        multibinder.addBinding().to(PerformLoginActionHandler.class);
        multibinder.addBinding().to(ChangePasswordActionHandler.class);
        multibinder.addBinding().to(CreateUserAccountActionHandler.class);

        multibinder.addBinding().to(GetHeadRevisionNumberActionHandler.class);
        multibinder.addBinding().to(GetRevisionSummariesActionHandler.class);

        multibinder.addBinding().to(GetPermissionsActionHandler.class);

        multibinder.addBinding().to(ComputeProjectMergeActionHandler.class);
        multibinder.addBinding().to(MergeUploadedProjectActionHandler.class);

        multibinder.addBinding().to(GetProjectChangesActionHandler.class);
        multibinder.addBinding().to(GetWatchedEntityChangesActionHandler.class);

        multibinder.addBinding().to(RevertRevisionActionHandler.class);

        multibinder.addBinding().to(GetPersonIdCompletionsActionHandler.class);
        multibinder.addBinding().to(GetUserIdCompletionsActionHandler.class);
        multibinder.addBinding().to(GetPersonIdItemsActionHandler.class);

        multibinder.addBinding().to(GetPerspectiveLayoutActionHandler.class);
        multibinder.addBinding().to(SetPerspectiveLayoutActionHandler.class);
        multibinder.addBinding().to(GetPerspectivesActionHandler.class);
        multibinder.addBinding().to(SetPerspectivesActionHandler.class);
    }
}
