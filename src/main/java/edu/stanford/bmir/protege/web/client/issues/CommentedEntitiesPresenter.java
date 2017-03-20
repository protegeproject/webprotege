package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent;
import edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadStatusChangedEvent;
import edu.stanford.bmir.protege.web.shared.issues.GetCommentedEntitiesAction;
import edu.stanford.bmir.protege.web.shared.issues.SortingKey;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.collect.ImmutableSet.of;
import static edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent.ON_BROWSER_TEXT_CHANGED;
import static edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent.ON_COMMENT_POSTED;
import static edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadStatusChangedEvent.ON_STATUS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.issues.Status.CLOSED;
import static edu.stanford.bmir.protege.web.shared.issues.Status.OPEN;
import static edu.stanford.bmir.protege.web.shared.pagination.PageRequest.requestPageWithSize;
import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public class CommentedEntitiesPresenter {

    public static final int PAGE_SIZE = 50;

    private final ProjectId projectId;

    private final CommentedEntitiesView view;

    private final DispatchServiceManager dispatchServiceManager;

    private final SelectionModel selectionModel;

    private final Set<OWLEntity> currentEntites = new HashSet<>();

    private final List<CommentedEntityData> pageElements = new ArrayList<>();

    private final Comparator<CommentedEntityData> entityComparator = Comparator.naturalOrder();

    private final Comparator<CommentedEntityData> lastModifiedComparator =
            Comparator.comparing(CommentedEntityData::getLastModified, Comparator.reverseOrder())
                      .thenComparing(Comparator.naturalOrder());

    @Inject
    public CommentedEntitiesPresenter(@Nonnull ProjectId projectId,
                                      @Nonnull CommentedEntitiesView view,
                                      @Nonnull DispatchServiceManager dispatchServiceManager,
                                      @Nonnull SelectionModel selectionModel) {
        this.projectId = projectId;
        this.view = view;
        this.dispatchServiceManager = dispatchServiceManager;
        this.selectionModel = selectionModel;
    }

    public void start(@Nonnull AcceptsOneWidget container, @Nonnull WebProtegeEventBus eventBus) {
        container.setWidget(view);
        view.setSelectionHandler(this::handleEntitySelected);
        view.setPageNumberChangedHandler(pageNumber -> reload());
        view.setSortingKeyChangedHandler(this::reload);
        reload();
        eventBus.addProjectEventHandler(projectId, ON_BROWSER_TEXT_CHANGED, this::handleBrowserTextChanged);
        eventBus.addProjectEventHandler(projectId, ON_COMMENT_POSTED, this::handleCommentPosted);
        eventBus.addProjectEventHandler(projectId, ON_STATUS_CHANGED, this::handleStatusChanged);
    }

    private void handleBrowserTextChanged(BrowserTextChangedEvent event) {
        if(currentEntites.contains(event.getEntity())) {
            reload();
        }
    }

    private void handleCommentPosted(CommentPostedEvent event) {
        event.getEntity().ifPresent(entityData -> {
            if(currentEntites.contains(entityData.getEntity())) {
                reload();
            }
        });
    }

    private void handleStatusChanged(DiscussionThreadStatusChangedEvent event) {
        reload();
    }

    private void handleEntitySelected(SelectionEvent<CommentedEntityData> event) {
        selectionModel.setSelection(event.getSelectedItem().getEntityData().getEntity());
    }

    private void reload() {
        GWT.log("[CommentedEntitiesPresenter] Reloading list data");
        dispatchServiceManager.execute(new GetCommentedEntitiesAction(projectId,
                                                                      "",
                                                                      of(OPEN, CLOSED),
                                                                      requestPageWithSize(view.getPageNumber(), PAGE_SIZE)),
                                       result -> {
                                           Page<CommentedEntityData> entities = result.getEntities();
                                           view.setPageCount(entities.getPageCount());
                                           view.setPageNumber(entities.getPageNumber());
                                           pageElements.clear();
                                           pageElements.addAll(entities.getPageElements());
                                           if(view.getSelectedSortingKey() == SortingKey.SORT_BY_ENTITY) {
                                               pageElements.sort(entityComparator);
                                           }
                                           else {
                                               pageElements.sort(lastModifiedComparator);
                                           }
                                           view.setEntities(pageElements);
                                           currentEntites.clear();
                                           currentEntites.addAll(entities.getPageElements().stream()
                                                                         .map(c -> c.getEntityData().getEntity())
                                                                         .collect(toList()));
                                       });
    }
}
