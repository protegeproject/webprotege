package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableList;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-16
 */
public class EntitySearchSettingsPresenter {

    @Nonnull
    private final EntitySearchSettingsService service;

    @Nonnull
    private final EntitySearchFilterListPresenter listPresenter;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Inject
    public EntitySearchSettingsPresenter(@Nonnull EntitySearchSettingsService service,
                                         @Nonnull EntitySearchFilterListPresenter listPresenter,
                                         @Nonnull SettingsPresenter settingsPresenter) {
        this.service = checkNotNull(service);
        this.listPresenter = checkNotNull(listPresenter);
        this.settingsPresenter = settingsPresenter;
    }

    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull EventBus eventBus) {
        settingsPresenter.start(container);
        settingsPresenter.setApplySettingsHandler(this::handleApplyFilters);

        AcceptsOneWidget searchFiltersContainer = settingsPresenter.addSection("Search Filters");
        listPresenter.start(searchFiltersContainer, eventBus);
        listPresenter.setDefaultStateCollapsed();

        service.getFilters(this::handleSetFilters);

    }

    private void handleSetFilters(ImmutableList<EntitySearchFilter> filter) {
        listPresenter.setValues(filter);
    }

    private void handleApplyFilters() {
        ImmutableList<EntitySearchFilter> values = listPresenter.getValues();
        service.setFilters(values);
    }
}
