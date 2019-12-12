package edu.stanford.bmir.protege.web.client.viz;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.viz.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class EntityGraphSettingsPresenter {


    @Nonnull
    private final EntityGraphSettingsView view;

    @Nonnull
    private final EntityGraphSettings currentSettings = EntityGraphSettings.getDefault();

    @Nonnull
    private final EntityGraphFilterListPresenter filterListPresenter;

    @Inject
    public EntityGraphSettingsPresenter(@Nonnull EntityGraphSettingsView view,
                                        @Nonnull EntityGraphFilterListPresenter filterListPresenter) {
        this.view = view;
        this.filterListPresenter = filterListPresenter;
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


    public void setApplySettingsHandler(Runnable runnable) {
        view.setApplySettingsHandler(runnable);
    }

    public void setCancelHandler(Runnable runnable) {
        view.setCancelSettingsHandler(runnable);
    }



    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        filterListPresenter.start(view.getFilterListContainer());
    }
}
