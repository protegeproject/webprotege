package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.form.LanguageMapCurrentLocaleMapper;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.library.tokenfield.TokenFieldPresenter;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
public class EntitySearchFilterTokenFieldPresenter {

    @Nonnull
    private final TokenFieldPresenter<EntitySearchFilter> tokenFieldPresenter;

    @Nonnull
    private final EntitySearchFilterService searchFilterService;

    @Nonnull
    private final LanguageMapCurrentLocaleMapper currentLocaleMapper;

    @Nonnull
    private final EntitySearchFilterTokenFieldView view;

    private SearchFiltersChangedHandler searchFiltersChangedHandler = () -> {};

    @Inject
    public EntitySearchFilterTokenFieldPresenter(@Nonnull TokenFieldPresenter<EntitySearchFilter> tokenFieldPresenter,
                                                 @Nonnull EntitySearchFilterService searchFilterService,
                                                 @Nonnull LanguageMapCurrentLocaleMapper currentLocaleMapper,
                                                 @Nonnull EntitySearchFilterTokenFieldView view) {
        this.tokenFieldPresenter = checkNotNull(tokenFieldPresenter);
        this.searchFilterService = checkNotNull(searchFilterService);
        this.currentLocaleMapper = checkNotNull(currentLocaleMapper);
        this.view = checkNotNull(view);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        tokenFieldPresenter.setAddTokenPrompt((event, callback) -> {
            searchFilterService.getSearchFilters(searchFilters -> {
                Set<EntitySearchFilter> existingFilters = ImmutableSet.copyOf(tokenFieldPresenter.getTokenObjects());
                PopupMenu popupMenu = new PopupMenu();
                searchFilters.stream()
                             .filter(filter -> !existingFilters.contains(filter))
                             .forEach(filter -> {
                                 String label = currentLocaleMapper.getValueForCurrentLocale(filter.getLabel());
                                 popupMenu.addItem(label, () -> {
                                     callback.addToken(filter, label);
                                 });
                             });
                if(popupMenu.isEmpty()) {
                    UIAction action = new AbstractUiAction("No remaining filters") {
                        @Override
                        public void execute() {

                        }
                    };
                    action.setEnabled(false);
                    popupMenu.addItem(action);
                }
                popupMenu.showRelativeTo(view.asWidget());
            });
        });
        container.setWidget(view);
        tokenFieldPresenter.start(view.getTokenFieldContainer());
        tokenFieldPresenter.setTokensChangedHandler(tokens -> searchFiltersChangedHandler.handleSearchFiltersChanged());
        tokenFieldPresenter.setPlaceholder("Add filter");
    }

    @Nonnull
    public ImmutableList<EntitySearchFilter> getSearchFilters() {
        return ImmutableList.copyOf(tokenFieldPresenter.getTokenObjects());
    }

    public void setSearchFiltersChangedHandler(SearchFiltersChangedHandler searchFiltersChangedHandler) {
        this.searchFiltersChangedHandler = checkNotNull(searchFiltersChangedHandler);
    }

    public void setPlaceholder(String placeholder) {
        tokenFieldPresenter.setPlaceholder(placeholder);
    }

    public interface SearchFiltersChangedHandler {
        void handleSearchFiltersChanged();
    }
}
