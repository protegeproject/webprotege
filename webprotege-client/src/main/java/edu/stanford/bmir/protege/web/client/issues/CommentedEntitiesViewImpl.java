package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.resources.WebProtegeCellListResources;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;
import edu.stanford.bmir.protege.web.shared.issues.SortingKey;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.BOUND_TO_SELECTION;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public class CommentedEntitiesViewImpl extends Composite implements CommentedEntitiesView, HasSelectionHandlers<CommentedEntityData> {

    private final CommentedEntityDataRenderer renderer;

    interface CommentedEntitiesViewImplUiBinder extends UiBinder<HTMLPanel, CommentedEntitiesViewImpl> {

    }

    private static CommentedEntitiesViewImplUiBinder ourUiBinder = GWT.create(CommentedEntitiesViewImplUiBinder.class);

    public static final OWLEntityVisitorEx<String> CSS_CLASS_NAME_VISITOR = new OWLEntityVisitorEx<String>() {
        @Nonnull
        @Override
        public String visit(@Nonnull OWLClass cls) {
            return BUNDLE.style().classIconInset();
        }

        @Nonnull
        @Override
        public String visit(@Nonnull OWLObjectProperty property) {
            return BUNDLE.style().objectPropertyIconInset();
        }

        @Nonnull
        @Override
        public String visit(@Nonnull OWLDataProperty property) {
            return BUNDLE.style().dataPropertyIconInset();
        }

        @Nonnull
        @Override
        public String visit(@Nonnull OWLNamedIndividual individual) {
            return BUNDLE.style().individualIconInset();
        }

        @Nonnull
        @Override
        public String visit(@Nonnull OWLDatatype datatype) {
            return BUNDLE.style().datatypeIconInset();
        }

        @Nonnull
        @Override
        public String visit(@Nonnull OWLAnnotationProperty property) {
            return BUNDLE.style().annotationPropertyIconInset();
        }
    };

    @UiField(provided = true)
    protected CellList<CommentedEntityData> list;

    @UiField(provided = true)
    protected PaginatorView paginator;

    @UiField
    protected ListBox sortingKey;

    private final PaginatorPresenter paginatorPresenter;

    private ListDataProvider<CommentedEntityData> listDataProvider = new ListDataProvider<>();

    private Runnable sortingKeyChangedHandler = () -> {};

    @Inject
    public CommentedEntitiesViewImpl(PaginatorPresenter paginatorPresenter) {
        paginator = paginatorPresenter.getView();
        this.paginatorPresenter = paginatorPresenter;

        ProvidesKey<CommentedEntityData> keyProvider = item -> item.getEntityData().getEntity();
        renderer = new CommentedEntityDataRenderer();
        list = new CellList<>(renderer,
                              WebProtegeCellListResources.INSTANCE,
                              keyProvider);
        final SingleSelectionModel<CommentedEntityData> selectionModel = new SingleSelectionModel<>(keyProvider);
        list.setSelectionModel(selectionModel);
        listDataProvider.addDataDisplay(list);
        list.setKeyboardSelectionPolicy(BOUND_TO_SELECTION);

        initWidget(ourUiBinder.createAndBindUi(this));
        selectionModel.addSelectionChangeHandler(event -> {
            SelectionEvent.fire(this, selectionModel.getSelectedObject());
        });

        list.addDomHandler(mouseUpEvent -> SelectionEvent.fire(this,
                                                               selectionModel.getSelectedObject()),
                           MouseUpEvent.getType());


    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<CommentedEntityData> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    private HandlerRegistration selectionHandlerRegistration = () -> {};

    @UiHandler("sortingKey")
    protected void handleSortingKeyChanged(ChangeEvent event) {
        sortingKeyChangedHandler.run();
    }

    @Override
    public SortingKey getSelectedSortingKey() {
        return SortingKey.valueOf(sortingKey.getSelectedValue());
    }

    @Override
    public void setSortingKeyChangedHandler(Runnable sortingKeyChangedHandler) {
        this.sortingKeyChangedHandler = sortingKeyChangedHandler;
    }

    @Override
    public void setSelectionHandler(@Nonnull SelectionHandler<CommentedEntityData> selectionHandler) {
        selectionHandlerRegistration.removeHandler();
        selectionHandlerRegistration = addHandler(selectionHandler, SelectionEvent.getType());
    }

    @Override
    public void setEntities(@Nonnull List<CommentedEntityData> entities) {
        GWT.log("[CommentedEntitiesViewImpl] setEntities: " + entities.size());
        listDataProvider.getList().clear();
        listDataProvider.getList().addAll(entities);
        list.setPageSize(entities.size());
        listDataProvider.refresh();
        listDataProvider.flush();
    }

    @Override
    public void clear() {
        listDataProvider.getList().clear();
        listDataProvider.refresh();
        list.setRowCount(0);
    }

    @Override
    public void refresh() {
        list.redraw();
    }

    @Override
    public void addEntity(@Nonnull CommentedEntityData entity) {
        listDataProvider.getList().add(0, entity);
        list.setPageSize(listDataProvider.getList().size());
        listDataProvider.refresh();
    }

    @Override
    public void removeEntity(@Nonnull CommentedEntityData entity) {
        listDataProvider.getList().remove(entity);
        list.setPageSize(listDataProvider.getList().size());
        listDataProvider.refresh();
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

    @Override
    public void setGoToEntityHandler(@Nonnull GoToEntityHandler handler) {
        renderer.setGoToButtonHandler(handler::handleGoToEntity);
    }
}