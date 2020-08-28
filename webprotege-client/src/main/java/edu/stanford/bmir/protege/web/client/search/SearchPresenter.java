package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.LangTagFilterPresenter;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.lang.GetProjectLangTagsAction;
import edu.stanford.bmir.protege.web.shared.lang.GetProjectLangTagsResult;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.*;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class SearchPresenter implements HasInitialFocusable {

    private static final int SEARCH_DELAY_MILLIS = 900;

    private static final int PAGE_CHANGE_DELAY_MILLIS = 250;

    private final ProjectId projectId;

    private final SearchView view;


    private final Set<EntityType<?>> entityTypes = new HashSet<>();

    private final DispatchServiceManager dispatchServiceManager;

    private final Timer searchTimer = new Timer() {
        @Override
        public void run() {
            performSearch();
        }
    };

    private final Timer pageChangeTimer = new Timer() {
        @Override
        public void run() {
            performSearch();
        }
    };

    private final EntitySearchResultPresenterFactory resultPresenterFactory;

    private final EntitySearchFilterTokenFieldPresenter entitySearchFilterTokenFieldPresenter;

    private final LangTagFilterPresenter langTagFilterPresenter;

    private final List<EntitySearchResultPresenter> resultPresenters = new ArrayList<>();

    @Inject
    public SearchPresenter(@Nonnull ProjectId projectId,
                           @Nonnull SearchView view,
                           @Nonnull DispatchServiceManager dispatchServiceManager,
                           @Nonnull EntitySearchResultPresenterFactory resultPresenterFactory,
                           EntitySearchFilterTokenFieldPresenter entitySearchFilterTokenFieldPresenter,
                           @Nonnull LangTagFilterPresenter langTagFilterPresenter) {
        this.projectId = projectId;
        this.view = view;
        this.dispatchServiceManager = dispatchServiceManager;
        this.resultPresenterFactory = resultPresenterFactory;
        this.entitySearchFilterTokenFieldPresenter = checkNotNull(entitySearchFilterTokenFieldPresenter);
        this.langTagFilterPresenter = langTagFilterPresenter;
    }

    public void start() {
        view.setSearchStringChangedHandler(() -> {
            view.setPageNumber(1);
            restartSearchTimer();
        });
        view.setPageNumberChangedHandler(pageNumber -> {
            restartPageChangeTimer();
        });
        dispatchServiceManager.beginBatch();
        dispatchServiceManager.execute(new GetProjectLangTagsAction(projectId),
                                       this::handleProjectLangTags);
        entitySearchFilterTokenFieldPresenter.start(view.getSearchFilterContainer());
        entitySearchFilterTokenFieldPresenter.setSearchFiltersChangedHandler(this::performSearch);
        dispatchServiceManager.execute(new GetSearchSettingsAction(projectId),
                                       this::handleSearchSettings);
        dispatchServiceManager.executeCurrentBatch();
        entitySearchFilterTokenFieldPresenter.setPlaceholder("");
        langTagFilterPresenter.setPlaceholder("");
    }

    private void handleSearchSettings(GetSearchSettingsResult result) {
        view.setSearchFilterVisible(!result.getFilters().isEmpty());
    }

    private void handleProjectLangTags(GetProjectLangTagsResult result) {
        boolean langTagsPresent = !result.getLangTags().isEmpty();
        view.setLangTagFilterVisible(langTagsPresent);
        if(langTagsPresent) {
            langTagFilterPresenter.start(view.getLangTagFilterContainer());
            langTagFilterPresenter.setLangTagFilterChangedHandler(this::handleLangTagFilterChanged);
        }
    }

    private void handleLangTagFilterChanged() {
        restartSearchTimer();
    }

    private void restartSearchTimer() {
        searchTimer.cancel();
        searchTimer.schedule(SEARCH_DELAY_MILLIS);
    }

    private void restartPageChangeTimer() {
        pageChangeTimer.cancel();
        pageChangeTimer.schedule(PAGE_CHANGE_DELAY_MILLIS);
    }

    public void setSearchResultChosenHandler(SearchResultChosenHandler handler) {
        view.setSearchResultChosenHandler(handler);
    }

    public Optional<OWLEntityData> getSelectedSearchResult() {
        int selIndex = view.getSelectedSearchResultIndex();
        if(selIndex == -1) {
            return Optional.empty();
        }
        EntitySearchResultPresenter resultPresenter = resultPresenters.get(selIndex);
        return Optional.of(resultPresenter.getEntity().getEntityData());
    }

    public IsWidget getView() {
        return view;
    }

    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    public void setEntityTypes(EntityType<?> ... entityTypes) {
        this.entityTypes.clear();
        this.entityTypes.addAll(Arrays.asList(entityTypes));
    }

    private void performSearch() {
        if(view.getSearchString().length() < 1) {
            view.clearEntitySearchResults();
            resultPresenters.clear();
            return;
        }
        LangTagFilter langTagFilter = langTagFilterPresenter.getFilter();
        int pageNumber = view.getPageNumber();
        ImmutableList<EntitySearchFilter> searchFilters = entitySearchFilterTokenFieldPresenter.getSearchFilters();
        dispatchServiceManager.execute(new PerformEntitySearchAction(projectId,
                                                                     view.getSearchString(),
                                                                     entityTypes,
                                                                     langTagFilter,
                                                                     searchFilters,
                                                                     PageRequest.requestPage(pageNumber)),
                                       view,
                                       this::displaySearchResult);
    }

    private void displaySearchResult(PerformEntitySearchResult result) {
        if(!view.getSearchString().equals(result.getSearchString())) {
            return;
        }
        resultPresenters.clear();
        Page<EntitySearchResult> resultsPage = result.getResults();
        resultsPage.getPageElements()
               .stream()
               .map(r -> {
                   EntitySearchResultPresenter presenter = resultPresenterFactory.create(r);
                   presenter.start();
                   return presenter;
               })
               .forEach(resultPresenters::add);
        ImmutableList<EntitySearchResultView> resultViews = resultPresenters.stream()
                                                                        .map(EntitySearchResultPresenter::getView)
                                                                        .collect(toImmutableList());
        view.setEntitySearchResults(resultViews);
        view.setPageCount(resultsPage.getPageCount());
        view.setPageNumber(resultsPage.getPageNumber());
        view.setTotalResultCount(resultsPage.getTotalElements());
    }

}
