package edu.stanford.bmir.protege.web.server.dispatch;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.admin.GetAdminSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.admin.SetAdminSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.auth.ChangePasswordActionHandler;
import edu.stanford.bmir.protege.web.server.auth.GetChapSessionActionHandler;
import edu.stanford.bmir.protege.web.server.auth.PerformLoginActionHandler;
import edu.stanford.bmir.protege.web.server.chgpwd.ResetPasswordActionHandler;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitsActionHandler;
import edu.stanford.bmir.protege.web.server.csv.GetCSVGridActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.handlers.*;
import edu.stanford.bmir.protege.web.server.events.GetProjectEventsActionHandler;
import edu.stanford.bmir.protege.web.server.itemlist.GetPersonIdCompletionsActionHandler;
import edu.stanford.bmir.protege.web.server.itemlist.GetPersonIdItemsActionHandler;
import edu.stanford.bmir.protege.web.server.itemlist.GetUserIdCompletionsActionHandler;
import edu.stanford.bmir.protege.web.server.mail.GetEmailAddressActionHandler;
import edu.stanford.bmir.protege.web.server.mail.SetEmailAddressActionHandler;
import edu.stanford.bmir.protege.web.server.permissions.GetProjectPermissionsActionHandler;
import edu.stanford.bmir.protege.web.server.permissions.RebuildPermissionsActionHandler;
import edu.stanford.bmir.protege.web.server.project.CreateNewProjectActionHandler;
import edu.stanford.bmir.protege.web.server.project.GetProjectDetailsActionHandler;
import edu.stanford.bmir.protege.web.server.user.CreateUserAccountActionHandler;
import edu.stanford.bmir.protege.web.server.user.LogOutUserActionHandler;

import static dagger.Provides.Type.SET;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class ActionHandlersModule {

    @Provides(type = SET)
    public ApplicationActionHandler provideGetAvailableProjectsHandler(GetAvailableProjectsHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideGetProjectDetailsActionHandler(GetProjectDetailsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideLoadProjectActionHandler(LoadProjectActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideCreateNewProjectActionHandler(CreateNewProjectActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideGetProjectEventsActionHandler(GetProjectEventsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideGetCurrentUserInSessionActionHandler(
            GetCurrentUserInSessionActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideSetEmailAddressActionHandler(SetEmailAddressActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideGetEmailAddressActionHandler(GetEmailAddressActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideMoveProjectsToTrashActionHandler(MoveProjectsToTrashActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideRemoveProjectsFromTrashActionHandler(
            RemoveProjectsFromTrashActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideGetCSVGridActionHandler(GetCSVGridActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideResetPasswordActionHandler(ResetPasswordActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideLogOutUserActionHandler(LogOutUserActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideGetChapSessionActionHandler(GetChapSessionActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler providePerformLoginActionHandler(PerformLoginActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideChangePasswordActionHandler(ChangePasswordActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideCreateUserAccountActionHandler(CreateUserAccountActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideGetPermissionsActionHandler(GetProjectPermissionsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideGetPersonIdCompletionsActionHandler(
            GetPersonIdCompletionsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideGetUserIdCompletionsActionHandler(GetUserIdCompletionsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideGetPersonIdItemsActionHandler(GetPersonIdItemsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideGetSystemSettingsActionHandler(GetAdminSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler provideSetAdminSettingsActionHandler(SetAdminSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler providesRebuildPermissionsActionHandler(RebuildPermissionsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ApplicationActionHandler providesGetEntityCrudKitsActionHandler(GetEntityCrudKitsActionHandler handler) {
        return handler;
    }


}
