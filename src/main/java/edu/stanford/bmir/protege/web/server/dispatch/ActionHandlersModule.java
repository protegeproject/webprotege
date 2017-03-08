package edu.stanford.bmir.protege.web.server.dispatch;

import dagger.Module;
import dagger.Provides;
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
import edu.stanford.bmir.protege.web.server.form.GetFormDescriptorActionHander;
import edu.stanford.bmir.protege.web.server.frame.*;
import edu.stanford.bmir.protege.web.server.individuals.CreateNamedIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.individuals.GetIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.issues.*;
import edu.stanford.bmir.protege.web.server.itemlist.GetPersonIdCompletionsActionHandler;
import edu.stanford.bmir.protege.web.server.itemlist.GetPersonIdItemsActionHandler;
import edu.stanford.bmir.protege.web.server.itemlist.GetUserIdCompletionsActionHandler;
import edu.stanford.bmir.protege.web.server.mail.GetEmailAddressActionHandler;
import edu.stanford.bmir.protege.web.server.mail.SetEmailAddressActionHandler;
import edu.stanford.bmir.protege.web.server.merge.ComputeProjectMergeActionHandler;
import edu.stanford.bmir.protege.web.server.merge.MergeUploadedProjectActionHandler;
import edu.stanford.bmir.protege.web.server.metrics.GetMetricsActionHandler;
import edu.stanford.bmir.protege.web.server.permissions.GetProjectPermissionsActionHandler;
import edu.stanford.bmir.protege.web.server.perspective.GetPerspectiveLayoutActionHandler;
import edu.stanford.bmir.protege.web.server.perspective.GetPerspectivesActionHandler;
import edu.stanford.bmir.protege.web.server.perspective.SetPerspectiveLayoutActionHandler;
import edu.stanford.bmir.protege.web.server.perspective.SetPerspectivesActionHandler;
import edu.stanford.bmir.protege.web.server.project.CreateNewProjectActionHandler;
import edu.stanford.bmir.protege.web.server.project.GetProjectDetailsActionHandler;
import edu.stanford.bmir.protege.web.server.projectsettings.GetProjectSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.projectsettings.SetProjectSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.mansyntax.render.GetEntityRenderingActionHandler;
import edu.stanford.bmir.protege.web.server.renderer.GetEntityDataActionHandler;
import edu.stanford.bmir.protege.web.server.revision.GetHeadRevisionNumberActionHandler;
import edu.stanford.bmir.protege.web.server.revision.GetRevisionSummariesActionHandler;
import edu.stanford.bmir.protege.web.server.sharing.GetProjectSharingSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.sharing.SetProjectSharingSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.usage.GetUsageActionHandler;
import edu.stanford.bmir.protege.web.server.user.CreateUserAccountActionHandler;
import edu.stanford.bmir.protege.web.server.user.LogOutUserActionHandler;
import edu.stanford.bmir.protege.web.server.watches.AddWatchActionHandler;
import edu.stanford.bmir.protege.web.server.watches.GetWatchesActionHandler;
import edu.stanford.bmir.protege.web.server.watches.RemoveWatchActionHandler;
import edu.stanford.bmir.protege.web.server.watches.SetEntityWatchesActionHandler;

import static dagger.Provides.Type.SET;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class ActionHandlersModule {

    @Provides(type = SET)
    public ActionHandler provideGetClientApplicationPropertiesActionHandler(
            GetClientApplicationPropertiesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetAvailableProjectsHandler(GetAvailableProjectsHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetProjectDetailsActionHandler(GetProjectDetailsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideLoadProjectActionHandler(LoadProjectActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideCreateNewProjectActionHandler(CreateNewProjectActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetProjectEventsActionHandler(GetProjectEventsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetProjectSettingsActionHandler(GetProjectSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideSetProjectSettingsActionHandler(SetProjectSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetClassFrameActionHandler(GetClassFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideUpdateClassFrameActionHandler(UpdateClassFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetObjectPropertyFrameActionHandler(
            GetObjectPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideUpdateObjectPropertyFrameHandler(UpdateObjectPropertyFrameHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideUpdateDataPropertyFrameHandler(UpdateDataPropertyFrameHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetAnnotationPropertyFrameActionHandler(
            GetAnnotationPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideUpdateAnnotationPropertyFrameActionHandler(
            UpdateAnnotationPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetNamedIndividualFrameActionHandler(
            GetNamedIndividualFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideUpdateNamedIndividualFrameHandler(UpdateNamedIndividualFrameHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetOntologyFramesActionHandler(GetOntologyFramesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetRootOntologyIdActionHandler(GetRootOntologyIdActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetOntologyAnnotationsActionHandler(
            GetOntologyAnnotationsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideSetOntologyAnnotationsActionHandler(
            SetOntologyAnnotationsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetEntityAnnotationsActionHandler(GetEntityAnnotationsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideDeleteEntityActionHandler(DeleteEntityActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideCreateClassActionHandler(CreateClassActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideCreateClassesActionHandler(CreateClassesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideCreateObjectPropertyActionHandler(CreateObjectPropertyActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideCreateDataPropertiesActionHandler(CreateDataPropertiesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideCreateAnnotationPropertiesActionHandler(
            CreateAnnotationPropertiesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideCreateNamedIndividualsActionHandler(
            CreateNamedIndividualsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideLookupEntitiesActionHandler(LookupEntitiesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideAddWatchActionHandler(AddWatchActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideRemoveWatchActionHandler(RemoveWatchActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideSetEntityWatchesActionHandler(SetEntityWatchesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetWatchesActionHandler(GetWatchesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetCurrentUserInSessionActionHandler(
            GetCurrentUserInSessionActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideSetEmailAddressActionHandler(SetEmailAddressActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetEmailAddressActionHandler(GetEmailAddressActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideMoveProjectsToTrashActionHandler(MoveProjectsToTrashActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideRemoveProjectsFromTrashActionHandler(
            RemoveProjectsFromTrashActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetCSVGridActionHandler(GetCSVGridActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideImportCSVFileActionHandler(ImportCSVFileActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetUsageActionHandler(GetUsageActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetIndividualsActionHandler(GetIndividualsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetEntityRenderingActionHandler(GetEntityRenderingActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetEntityDataActionHandler(GetEntityDataActionHandler handler) {
        return handler;
    }


    @Provides(type = SET)
    public ActionHandler provideGetDataPropertyFrameActionHandler(GetDataPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetMetricsActionHandler(GetMetricsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetEntityCrudKitsActionHandler(GetEntityCrudKitsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideSetEntityCrudKitSettingsActionHandler(
            SetEntityCrudKitSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetEntityCrudKitSettingsActionHandler(
            GetEntityCrudKitSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetManchesterSyntaxFrameActionHandler(
            GetManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideSetManchesterSyntaxFrameActionHandler(
            SetManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideCheckManchesterSyntaxFrameActionHandler(
            CheckManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetManchesterSyntaxFrameCompletionsActionHandler(
            GetManchesterSyntaxFrameCompletionsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideResetPasswordActionHandler(ResetPasswordActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetProjectSharingSettingsActionHandler(
            GetProjectSharingSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideSetProjectSharingSettingsActionHandler(
            SetProjectSharingSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideLogOutUserActionHandler(LogOutUserActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetChapSessionActionHandler(GetChapSessionActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler providePerformLoginActionHandler(PerformLoginActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideChangePasswordActionHandler(ChangePasswordActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideCreateUserAccountActionHandler(CreateUserAccountActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetHeadRevisionNumberActionHandler(
            GetHeadRevisionNumberActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetRevisionSummariesActionHandler(GetRevisionSummariesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetPermissionsActionHandler(GetProjectPermissionsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideComputeProjectMergeActionHandler(ComputeProjectMergeActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideMergeUploadedProjectActionHandler(MergeUploadedProjectActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetProjectChangesActionHandler(GetProjectChangesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetWatchedEntityChangesActionHandler(
            GetWatchedEntityChangesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideRevertRevisionActionHandler(RevertRevisionActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetPersonIdCompletionsActionHandler(
            GetPersonIdCompletionsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetUserIdCompletionsActionHandler(GetUserIdCompletionsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetPersonIdItemsActionHandler(GetPersonIdItemsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetPerspectiveLayoutActionHandler(GetPerspectiveLayoutActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideSetPerspectiveLayoutActionHandler(SetPerspectiveLayoutActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetPerspectivesActionHandler(GetPerspectivesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideSetPerspectivesActionHandler(SetPerspectivesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetFormDescriptorActionHander(GetFormDescriptorActionHander handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetIssuesActionHandler(GetIssuesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetDiscussionThreadsActionHandler(GetEntityDiscussionThreadsHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideCreateEntityDiscussionThreadActionHandler(CreateEntityDiscussionThreadHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideAddEntityCommentActionHandler(AddEntityCommentHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideEditCommentAction(EditCommentActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideDeleteEntityCommentActionHandler(DeleteEntityCommentHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideSetDiscussionThreadStatusHandler(SetDiscussionThreadStatusHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ActionHandler provideGetCommentedEntitiesActionHandler(GetCommentedEntitiesActionHandler handler) {
        return handler;
    }

}
