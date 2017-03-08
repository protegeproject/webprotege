package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.entitieslist.EntitiesList;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public class CommentedEntitiesViewImpl extends Composite implements CommentedEntitiesView {

    interface CommentedEntitiesViewImplUiBinder extends UiBinder<HTMLPanel, CommentedEntitiesViewImpl> {

    }

    private static CommentedEntitiesViewImplUiBinder ourUiBinder = GWT.create(CommentedEntitiesViewImplUiBinder.class);

    @UiField
    protected EntitiesList<OWLEntityData> entityList;

    @UiField(provided = true)
    protected PaginatorView paginator;

    private final PaginatorPresenter paginatorPresenter;

    private SelectionHandler<OWLEntityData> selectionHandler = event -> {};

    @Inject
    public CommentedEntitiesViewImpl(PaginatorPresenter paginatorPresenter) {
        paginator = paginatorPresenter.getView();
        this.paginatorPresenter = paginatorPresenter;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("entityList")
    protected void handleEntitySelectionChanged(SelectionEvent<OWLEntityData> event) {
        selectionHandler.onSelection(event);
    }

    @Override
    public void setSelectionHandler(@Nonnull SelectionHandler<OWLEntityData> selectionHandler) {
        this.selectionHandler = checkNotNull(selectionHandler);
    }

    @Override
    public void setEntities(@Nonnull List<OWLEntityData> entities) {
        entityList.setListData(entities);
    }

    @Override
    public void clear() {
        entityList.clear();
    }

    @Override
    public void addEntity(@Nonnull OWLEntityData entity) {
        entityList.addAll(Collections.singletonList(entity));
    }

    @Override
    public void removeEntity(@Nonnull OWLEntityData entity) {
        entityList.removeAll(Collections.singletonList(entity));
    }

    @Override
    public void setPageCount(int pageCount) {
        paginatorPresenter.setPageCount(pageCount);
    }

    @Override
    public void setPageNumber(int pageNumber) {
        paginatorPresenter.setPageNumber(pageNumber);
    }

    @Override
    public int getPageNumber() {
        return paginatorPresenter.getPageNumber();
    }

    @Override
    public void setPageNumberChangedHandler(HasPagination.PageNumberChangedHandler handler) {
        paginatorPresenter.setPageNumberChangedHandler(handler);
    }
}