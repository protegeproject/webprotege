package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchAction;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


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

    @Inject
    public SearchPresenter(@Nonnull ProjectId projectId,
                           @Nonnull SearchView view,
                           @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.view = view;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void start() {
        view.setSearchStringChangedHandler(() -> {
            view.setPageNumber(1);
            restartSearchTimer();
        });
        view.setPageNumberChangedHandler(pageNumber -> {
            restartPageChangeTimer();
        });
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
        return view.getSelectedSearchResult();
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
        if(view.getSearchString().length() <= 1) {
            view.clearSearchMatches();
            return;
        }
        GWT.log("[SearchPresenter] Performing search");
        int pageNumber = view.getPageNumber();
        dispatchServiceManager.execute(new PerformEntitySearchAction(projectId,
                                                                     view.getSearchString(),
                                                                     entityTypes,
                                                                     PageRequest.requestPage(pageNumber)),
                                       view,
                                       this::displaySearchResult);
    }

    private void displaySearchResult(PerformEntitySearchResult result) {
        if(!view.getSearchString().equals(result.getSearchString())) {
            return;
        }
        Page<EntitySearchResult> results = result.getResults();
        view.setSearchMatches(result.getTotalResultCount(),
                              results.getPageElements());
        view.setPageCount(results.getPageCount());
        view.setPageNumber(results.getPageNumber());
    }


}
