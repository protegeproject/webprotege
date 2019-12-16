package edu.stanford.bmir.protege.web.client.viz;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.viz.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class EntityGraphSettingsPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityGraphSettingsView view;

    @Nonnull
    private HasBusy hasBusy = busy -> {};

    @Nonnull
    private final EntityGraphFilterListPresenter filterListPresenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private Runnable settingsAppliedHandler = () -> {};

    @Nonnull
    private Runnable cancelHandler = () -> {};

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public EntityGraphSettingsPresenter(@Nonnull ProjectId projectId,
                                        @Nonnull EntityGraphSettingsView view,
                                        @Nonnull EntityGraphFilterListPresenter filterListPresenter,
                                        @Nonnull DispatchServiceManager dispatchServiceManager,
                                        @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                        @Nonnull LoggedInUserProvider loggedInUserProvider) {
        this.projectId = projectId;
        this.view = view;
        this.filterListPresenter = filterListPresenter;
        this.dispatchServiceManager = dispatchServiceManager;
        this.permissionChecker = permissionChecker;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        filterListPresenter.start(view.getFilterListContainer());
        view.setApplySettingsHandler(this::handleApplySettings);
        view.setCancelSettingsHandler(this::handleCancel);
        dispatchServiceManager.execute(new GetUserProjectEntityGraphCriteriaAction(projectId),
                                       hasBusy,
                                       this::displaySettings);
        permissionChecker.hasPermission(BuiltInAction.EDIT_DEFAULT_VISUALIZATION_SETTINGS,
                                        this::updateApplySettingsForProject);

    }

    public void updateApplySettingsForProject(boolean hasPermissionToApplyProjectSettings) {
        if(hasPermissionToApplyProjectSettings) {
            view.setApplySettingsAsProjectDefaultVisible(true);
            view.setApplySettingsAsProjectDefaultHandler(this::handleApplySettingsForProject);
        }
        else {
            view.setApplySettingsAsProjectDefaultVisible(false);
        }
    }

    private void handleApplySettingsForProject() {
        applySettingsForUserId(null);
    }

    private void displaySettings(GetUserProjectEntityGraphCriteriaResult result) {
        dispatchServiceManager.beginBatch();
        EntityGraphSettings settings = result.getSettings();
        view.setRankSpacing(settings.getRankSpacing());
        filterListPresenter.setFilters(settings.getFilters());
        dispatchServiceManager.executeCurrentBatch();
    }

    private void handleCancel() {
        cancelHandler.run();
    }

    private void handleApplySettings() {
        UserId userId = loggedInUserProvider.getCurrentUserId();
        applySettingsForUserId(userId);
    }

    private void applySettingsForUserId(@Nullable UserId userId) {
        EntityGraphSettings settings = EntityGraphSettings.get(
                filterListPresenter.getFilters(),
                view.getRankSpacing()
        );
        dispatchServiceManager.execute(new SetUserProjectEntityGraphSettingsAction(projectId,
                                                                                   userId,
                                                                                   settings),
                                       hasBusy,
                                       result -> {
                                            if(userId != null) {
                                                settingsAppliedHandler.run();
                                            }
                                       });
    }

    public void clear() {
        filterListPresenter.clear();
    }

    public double getRankSpacing() {
        return view.getRankSpacing();
    }

    public void setRankSpacing(double rankSpacing) {
        view.setRankSpacing(rankSpacing);
    }

    public void setSettings(@Nonnull EntityGraphSettings settings) {
        view.setRankSpacing(settings.getRankSpacing());
        filterListPresenter.setFilters(settings.getFilters());
    }

    @Nonnull
    public EntityGraphSettings getSettings() {
        ImmutableList<EntityGraphFilter> filters = filterListPresenter.getFilters();
        return EntityGraphSettings.get(filters, view.getRankSpacing());
    }


    public void setSettingsAppliedHandler(Runnable runnable) {
        this.settingsAppliedHandler = checkNotNull(runnable);
    }

    public void setCancelHandler(Runnable runnable) {
        cancelHandler = checkNotNull(runnable);
    }
}
