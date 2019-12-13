package edu.stanford.bmir.protege.web.client.viz;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.viz.*;

import javax.annotation.Nonnull;
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
    private final EntityGraphSettings currentSettings = EntityGraphSettings.getDefault();

    @Nonnull
    private final EntityGraphFilterListPresenter filterListPresenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private Runnable settingsAppliedHandler = () -> {};

    @Nonnull
    private Runnable cancelHandler = () -> {};

    @Inject
    public EntityGraphSettingsPresenter(@Nonnull ProjectId projectId,
                                        @Nonnull EntityGraphSettingsView view,
                                        @Nonnull EntityGraphFilterListPresenter filterListPresenter,
                                        @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.view = view;
        this.filterListPresenter = filterListPresenter;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        filterListPresenter.start(view.getFilterListContainer());
        view.setApplySettingsHandler(this::handleApplySettings);
        view.setCancelSettingsHandler(this::handleCancel);
    }

    private void handleCancel() {
        cancelHandler.run();
    }

    private void handleApplySettings() {
        EntityGraphSettings settings = EntityGraphSettings.get(
                filterListPresenter.getFilters(),
                ImmutableSet.of(),
                view.getRankSpacing()
        );
        dispatchServiceManager.execute(new SetUserProjectEntityGraphSettingsAction(projectId,
                                                                                   settings),
                                       hasBusy,
                                       result -> settingsAppliedHandler.run());
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
        return EntityGraphSettings.get(filters, ImmutableSet.of(), view.getRankSpacing());
    }


    public void setSettingsAppliedHandler(Runnable runnable) {
        this.settingsAppliedHandler = checkNotNull(runnable);
    }

    public void setCancelHandler(Runnable runnable) {
        cancelHandler = checkNotNull(runnable);
    }
}
