package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.issues.GetCommentedEntitiesAction;
import edu.stanford.bmir.protege.web.shared.issues.Status;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.ImmutableSet.of;
import static edu.stanford.bmir.protege.web.shared.issues.Status.CLOSED;
import static edu.stanford.bmir.protege.web.shared.issues.Status.OPEN;
import static edu.stanford.bmir.protege.web.shared.pagination.PageRequest.requestPageWithSize;
import static java.util.Collections.sort;

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
        reload();
    }

    private void handleEntitySelected(SelectionEvent<CommentedEntityData> event) {
        selectionModel.setSelection(event.getSelectedItem().getEntityData().getEntity());
    }

    private void reload() {
        dispatchServiceManager.execute(new GetCommentedEntitiesAction(projectId,
                                                                      "",
                                                                      of(OPEN, CLOSED),
                                                                      requestPageWithSize(view.getPageNumber(), PAGE_SIZE)),
                                       result -> {
                                           Page<CommentedEntityData> entities = result.getEntities();
                                           view.setPageCount(entities.getPageCount());
                                           view.setPageNumber(entities.getPageNumber());
                                           List<CommentedEntityData> pageElements = entities.getPageElements();
                                           sort(pageElements);
                                           view.setEntities(pageElements);
                                       });
    }
}
