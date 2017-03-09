package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.resources.WebProtegeCellListResources;
import edu.stanford.bmir.protege.web.shared.TimeUtil;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public class CommentedEntitiesViewImpl extends Composite implements CommentedEntitiesView, HasSelectionHandlers<CommentedEntityData> {

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

    private final PaginatorPresenter paginatorPresenter;

    private ListDataProvider<CommentedEntityData> listDataProvider = new ListDataProvider<>();

    @Inject
    public CommentedEntitiesViewImpl(PaginatorPresenter paginatorPresenter) {
        paginator = paginatorPresenter.getView();
        this.paginatorPresenter = paginatorPresenter;

        CommentedEntityDataKeyProvider keyProvider = new CommentedEntityDataKeyProvider();
        list = new CellList<>(new CommentedEntityCellData(),
                              WebProtegeCellListResources.INSTANCE,
                              keyProvider);
        final SingleSelectionModel<CommentedEntityData> selectionModel = new SingleSelectionModel<>(keyProvider);
        list.setSelectionModel(selectionModel);
        listDataProvider.addDataDisplay(list);
        list.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.BOUND_TO_SELECTION);

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

    private class CommentedEntityCellData extends AbstractCell<CommentedEntityData> {

        @Override
        public void render(Context context, CommentedEntityData value, SafeHtmlBuilder safeHtmlBuilder) {
            StringBuilder sb = new StringBuilder();
            String cssClassName = value.getEntityData().accept(CSS_CLASS_NAME_VISITOR, "empty-icon-inset" );
            sb.append("<div style='" +
                              "border-bottom: 1px solid #f0f0f0;" +
                              "padding: 3px;" +
                              " align: middle;'>" );

            sb.append("<div class=\"" )
              .append(cssClassName)
              .append("\" style='line-height: 20px; display: inline-block; padding 4px;'>" );
            sb.append("<span style='font-weight: 500;'>");
            sb.append(value.getEntityData().getBrowserText());
            sb.append("</span>");
            sb.append("<div>" );

            String statusBadgeStyle =
                    "display: inline-block;" +
                            "    min-width: 60px;" +
                            "    border-radius: 3px;" +
                            "    color: white;" +
                            "    font-size: 10px;" +
                            "    padding: 4px;" +
                            "    margin: 2px;" +
                            "    text-align: center;" +
                            "    line-height: normal;";

            if (value.getOpenThreadCount() > 0) {
                sb.append("<div style='" )
                  .append(statusBadgeStyle)
                  .append("    background: #909;'>" )
                  .append(value.getOpenThreadCount()).append(" unresolved</div>" );
            }
            else {
                sb.append("<div style='" )
                  .append(statusBadgeStyle)
                  .append(" background: #090;'>" )
                  .append(value.getTotalThreadCount()).append(" resolved</div>" );
            }
            sb.append("<div style='display: inline-block; padding-left:10px;'>" );
            sb.append("Updated: " );
            sb.append(TimeUtil.getTimeRendering(value.getLastModified()));
            sb.append("</div>" );

            sb.append("</div>" );
            sb.append("</div>" );

            sb.append("</div>" );
            safeHtmlBuilder.appendHtmlConstant(sb.toString());
        }
    }

    private class CommentedEntityDataKeyProvider implements ProvidesKey<CommentedEntityData> {

        @Override
        public Object getKey(CommentedEntityData item) {
            return item.getEntityData().getEntity();
        }
    }
}