package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorViewImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class ChangeListViewImpl extends Composite implements ChangeListView {

    private final PaginatorPresenter paginatorPresenter;

    interface ChangeListViewImplUiBinder extends UiBinder<HTMLPanel, ChangeListViewImpl> {
    }

    private static ChangeListViewImplUiBinder ourUiBinder = GWT.create(ChangeListViewImplUiBinder.class);


    @UiField
    protected HTMLPanel container;

    @UiField(provided = true)
    PaginatorViewImpl paginatorView;

    private boolean detailsVisible = true;

    @Inject
    public ChangeListViewImpl(@Nonnull PaginatorPresenter paginatorPresenter) {
        this.paginatorPresenter = checkNotNull(paginatorPresenter);
        this.paginatorView = (PaginatorViewImpl) paginatorPresenter.getView();
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @Override
    public void addChangeDetailsView(ChangeDetailsView view) {
        view.setDetailsVisible(detailsVisible);
        container.add(view);
    }

    @Override
    public void addSeparator(String separatorText) {
        ChangeListSeparator sep = new ChangeListSeparator();
        sep.setSeparatorText(separatorText);
        container.add(sep);
    }

    @Override
    public void clear() {
        container.clear();
    }

    @Override
    public void setDetailsVisible(boolean detailsVisible) {
        for(int i = 0; i < container.getWidgetCount(); i++) {
            Widget widget = container.getWidget(i);
            if (widget instanceof ChangeDetailsView) {
                ChangeDetailsView view = (ChangeDetailsView) widget;
                view.setDetailsVisible(detailsVisible);
            }
        }
        this.detailsVisible = detailsVisible;
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