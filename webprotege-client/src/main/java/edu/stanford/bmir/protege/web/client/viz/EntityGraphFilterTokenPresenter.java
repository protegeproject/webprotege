package edu.stanford.bmir.protege.web.client.viz;

import com.google.common.collect.ImmutableList;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.client.library.tokenfield.AddTokenCallback;
import edu.stanford.bmir.protege.web.client.library.tokenfield.AddTokenPrompt;
import edu.stanford.bmir.protege.web.client.library.tokenfield.TokenFieldPresenter;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraphFilter;
import edu.stanford.bmir.protege.web.shared.viz.FilterName;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public class EntityGraphFilterTokenPresenter {

    @Nonnull
    public List<FilterName> getActiveFilters() {
        return filterNameTokenFieldPresenter.getTokenObjects();
    }

    interface ActiveFiltersChangedHandler {
        void handleActiveFiltersChanged();
    }

    @Nonnull
    private final EntityGraphFilterTokenView view;

    @Nonnull
    private final TokenFieldPresenter<FilterName> filterNameTokenFieldPresenter;

    private final List<FilterName> filterNames = new ArrayList<>();

    private Collection<FilterName> activeFilters = new LinkedHashSet<>();

    private ActiveFiltersChangedHandler activeFiltersChangedHandler = () -> {};

    @Inject
    public EntityGraphFilterTokenPresenter(@Nonnull EntityGraphFilterTokenView view,
                                           @Nonnull TokenFieldPresenter<FilterName> filterNameTokenFieldPresenter) {
        this.view = checkNotNull(view);
        this.filterNameTokenFieldPresenter = checkNotNull(filterNameTokenFieldPresenter);
    }

    public void setActiveFiltersChangedHandler(@Nonnull ActiveFiltersChangedHandler handler) {
        this.activeFiltersChangedHandler = checkNotNull(handler);
    }

    public void setActiveFilters(List<FilterName> activeFilters) {
        this.activeFilters.clear();
        this.activeFilters.addAll(activeFilters);
        filterNameTokenFieldPresenter.clear();
        activeFilters.forEach(filterName -> filterNameTokenFieldPresenter.addToken(filterName, filterName.getName()));
    }

    public void setFilters(Collection<FilterName> filterNames) {
        this.filterNames.clear();
        this.filterNames.addAll(filterNames);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        filterNameTokenFieldPresenter.start(view.getTokenFieldContainer());
        filterNameTokenFieldPresenter.setAddTokenPrompt(this::handlePrompt);
        filterNameTokenFieldPresenter.setTokensChangedHandler(tokens -> {
            activeFiltersChangedHandler.handleActiveFiltersChanged();
        });
    }

    public void handlePrompt(ClickEvent event, AddTokenCallback<FilterName> callback) {
        List<FilterName> remainingFilters = new ArrayList<>(filterNames);
        remainingFilters.removeAll(activeFilters);
        view.promptChoice(
                remainingFilters,
                event.getClientX(),
                event.getClientY(),
                filterName -> {
                    activeFilters.add(filterName);
                    callback.addToken(filterName, filterName.getName());
                });
    }
}
