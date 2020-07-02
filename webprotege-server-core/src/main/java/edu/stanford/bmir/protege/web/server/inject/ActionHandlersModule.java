package edu.stanford.bmir.protege.web.server.inject;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.stanford.bmir.protege.web.server.app.GetApplicationSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.app.SetApplicationSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.auth.ChangePasswordActionHandler;
import edu.stanford.bmir.protege.web.server.auth.GetChapSessionActionHandler;
import edu.stanford.bmir.protege.web.server.auth.PerformLoginActionHandler;
import edu.stanford.bmir.protege.web.server.chgpwd.ResetPasswordActionHandler;
import edu.stanford.bmir.protege.web.server.csv.GetCSVGridActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
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
import edu.stanford.bmir.protege.web.server.project.GetAvailableProjectsWithPermissionActionHandler;
import edu.stanford.bmir.protege.web.server.project.GetProjectDetailsActionHandler;
import edu.stanford.bmir.protege.web.server.user.CreateUserAccountActionHandler;
import edu.stanford.bmir.protege.web.server.user.LogOutUserActionHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class ActionHandlersModule {

    @Provides @IntoSet
    public ApplicationActionHandler provideGetAvailableProjectsHandler(GetAvailableProjectsHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetProjectDetailsActionHandler(GetProjectDetailsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideLoadProjectActionHandler(LoadProjectActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideCreateNewProjectActionHandler(CreateNewProjectActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetProjectEventsActionHandler(GetProjectEventsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetCurrentUserInSessionActionHandler(
            GetCurrentUserInSessionActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideSetEmailAddressActionHandler(SetEmailAddressActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetEmailAddressActionHandler(GetEmailAddressActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideMoveProjectsToTrashActionHandler(MoveProjectsToTrashActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideRemoveProjectsFromTrashActionHandler(
            RemoveProjectsFromTrashActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetCSVGridActionHandler(GetCSVGridActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideResetPasswordActionHandler(ResetPasswordActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideLogOutUserActionHandler(LogOutUserActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetChapSessionActionHandler(GetChapSessionActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler providePerformLoginActionHandler(PerformLoginActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideChangePasswordActionHandler(ChangePasswordActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideCreateUserAccountActionHandler(CreateUserAccountActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetPermissionsActionHandler(GetProjectPermissionsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetPersonIdCompletionsActionHandler(
            GetPersonIdCompletionsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetUserIdCompletionsActionHandler(GetUserIdCompletionsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetPersonIdItemsActionHandler(GetPersonIdItemsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetSystemSettingsActionHandler(GetApplicationSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideSetAdminSettingsActionHandler(SetApplicationSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler providesRebuildPermissionsActionHandler(RebuildPermissionsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler providesGetAvailableProjectsWithPermissionActionHandler(GetAvailableProjectsWithPermissionActionHandler handler) {
        return handler;
    }
}
