package edu.stanford.bmir.protege.web.client.viz;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraphFilter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
public class EntityGraphFilterListPresenter {

    @Nonnull
    private final EntityGraphFilterListView view;

    @Nonnull
    private final Provider<EntityGraphFilterPresenter> presenterProvider;

    @Nonnull
    private final Provider<EntityGraphFilterListItemView> itemViewProvider;

    @Nonnull
    private final List<EntityGraphFilterPresenter> filterPresenters = new ArrayList<>();

    @Inject
    public EntityGraphFilterListPresenter(@Nonnull EntityGraphFilterListView view,
                                          @Nonnull Provider<EntityGraphFilterPresenter> presenterProvider,
                                          @Nonnull Provider<EntityGraphFilterListItemView> itemViewProvider) {
        this.view = checkNotNull(view);
        this.presenterProvider = checkNotNull(presenterProvider);
        this.itemViewProvider = checkNotNull(itemViewProvider);
    }


    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setAddItemHandler(this::handleAddFilter);
    }


    @Nonnull
    ImmutableList<EntityGraphFilter> getFilters() {
        return filterPresenters.stream()
                               .map(EntityGraphFilterPresenter::getFilter)
                               .filter(Optional::isPresent)
                               .map(Optional::get)
                               .collect(toImmutableList());
    }

    public void clear() {
        view.clear();
        filterPresenters.clear();
    }

    public void setFilters(@Nonnull List<EntityGraphFilter> filters) {
        clear();
        checkNotNull(filters);
        filters.forEach(filter -> {
            EntityGraphFilterPresenter presenter = presenterProvider.get();
            presenter.setFilter(filter);
            addPresenter(presenter);
        });
    }
    private void handleAddFilter() {
        EntityGraphFilterPresenter presenter = presenterProvider.get();
        addPresenter(presenter);
    }

    private void addPresenter(EntityGraphFilterPresenter presenter) {
        EntityGraphFilterListItemView itemView = itemViewProvider.get();
        filterPresenters.add(presenter);
        presenter.start(itemView.getContainer());
        view.addItem(itemView);
        itemView.setDeleteHandler(() -> {
            view.removeItem(itemView);
            filterPresenters.remove(presenter);
        });
    }
}
