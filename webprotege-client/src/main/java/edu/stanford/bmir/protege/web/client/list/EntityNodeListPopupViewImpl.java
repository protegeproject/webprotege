package edu.stanford.bmir.protege.web.client.list;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.entity.EntityNodeHtmlRenderer;
import edu.stanford.bmir.protege.web.client.individualslist.EntityNodeListCellRenderer;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorViewImpl;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Sep 2018
 */
public class EntityNodeListPopupViewImpl extends Composite implements EntityNodeListPopupView {

    private AcceptSelectionHandler acceptSelectionHandler = (sel) -> {};

    interface EntityNodeListPopupViewImplUiBinder extends UiBinder<HTMLPanel, EntityNodeListPopupViewImpl> {

    }

    private static EntityNodeListPopupViewImplUiBinder ourUiBinder = GWT.create(EntityNodeListPopupViewImplUiBinder.class);

    private final PaginatorPresenter paginatorPresenter;

    @UiField
    Label titleLabel;

    @UiField
    ListBox<OWLEntityData, EntityNode> listBox;

    @UiField(provided = true)
    PaginatorViewImpl paginatorView;

    @Inject
    public EntityNodeListPopupViewImpl(@Nonnull PaginatorPresenter paginatorPresenter,
                                       @Nonnull EntityNodeListCellRenderer renderer) {
        this.paginatorPresenter = checkNotNull(paginatorPresenter);
        this.paginatorView = (PaginatorViewImpl) paginatorPresenter.getView();
        initWidget(ourUiBinder.createAndBindUi(this));
        listBox.setRenderer(renderer);
    }

    @UiHandler("listBox")
    protected void handleListBoxDoubleClicked(DoubleClickEvent event) {
        GWT.log("[EntityNodeListPopupViewImpl] Double clicked");
        listBox.getFirstSelectedElement()
               .ifPresent(sel -> acceptSelectionHandler.handleAcceptSelection(sel.getEntityData()));
    }

    @UiHandler("listBox")
    protected void handleListBoxClicked(ClickEvent event) {
        listBox.getFirstSelectedElement()
               .ifPresent(sel -> acceptSelectionHandler.handleAcceptSelection(sel.getEntityData()));
    }

    @Override
    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
        return listBox.addDoubleClickHandler(handler);
    }

    @Override
    public void setAcceptSelectionHandler(@Nonnull AcceptSelectionHandler handler) {
        this.acceptSelectionHandler = checkNotNull(handler);
    }

    @Override
    public void setListData(@Nonnull List<EntityNode> pageElements) {
        listBox.setListData(pageElements);
    }

    @Nonnull
    @Override
    public Optional<EntityNode> getFirstSelectedElement() {
        return listBox.getFirstSelectedElement();
    }

    @Nonnull
    @Override
    public List<EntityNode> getSelection() {
        return listBox.getSelection();
    }

    @Override
    public void setTitleLabel(@Nonnull String title) {
        titleLabel.setText(checkNotNull(title));
        titleLabel.setVisible(true);
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
    public void setPageNumberChangedHandler(PageNumberChangedHandler handler) {
        paginatorPresenter.setPageNumberChangedHandler(handler);
    }
}