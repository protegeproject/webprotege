package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.entitieslist.EntitiesListImpl;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.search.SearchStringChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class IndividualsListViewImpl extends Composite implements IndividualsListView {

    private final PaginatorPresenter paginatorPresenter;

    interface IndividualsListViewImplUiBinder extends UiBinder<HTMLPanel, IndividualsListViewImpl> {

    }

    private static IndividualsListViewImplUiBinder ourUiBinder = GWT.create(IndividualsListViewImplUiBinder.class);

    @UiField
    protected EntitiesListImpl<OWLNamedIndividualData> individualsList;

    @UiField
    protected Label statusLabel;

    @UiField
    protected TextBox searchBox;

    @UiField(provided = true)
    protected PaginatorView paginator;

    @UiField
    protected BusyView busyView;

    private SearchStringChangedHandler searchStringChangedHandler = () -> {};

    @Inject
    public IndividualsListViewImpl(PaginatorPresenter paginatorPresenter) {
        this.paginatorPresenter = paginatorPresenter;
        paginator = paginatorPresenter.getView();
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setBusy(boolean busy) {
        GWT.log("Set Busy: " + busy);
        busyView.setVisible(busy);
    }

    @UiHandler("searchBox")
    protected void handleSearchStringChanged(KeyUpEvent event) {
        searchStringChangedHandler.handleSearchStringChanged();
    }

    @Override
    public void setListData(List<OWLNamedIndividualData> individuals) {
        individualsList.setListData(individuals);
    }

    @Override
    public void addListData(Collection<OWLNamedIndividualData> individuals) {
        individualsList.addAll(individuals);
    }

    @Override
    public void removeListData(Collection<OWLNamedIndividualData> individuals) {
        individualsList.removeAll(individuals);
    }

    @Override
    public Collection<OWLNamedIndividualData> getSelectedIndividuals() {
        return individualsList.getSelectedEntities();
    }

    @Override
    public Optional<OWLNamedIndividualData> getSelectedIndividual() {
        return individualsList.getSelectedEntity();
    }

    @Override
    public void setSelectedIndividual(OWLNamedIndividualData individual) {
        individualsList.setSelectedEntity(individual);
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<OWLNamedIndividualData> handler) {
        return individualsList.addSelectionHandler(handler);
    }

    @Override
    public void setStatusMessage(String statusMessage) {
        statusLabel.setText(statusMessage);
    }

    @Override
    public void setStatusMessageVisible(boolean visible) {
        statusLabel.setVisible(visible);
    }

    @Override
    public String getSearchString() {
        return searchBox.getText();
    }

    @Override
    public void clearSearchString() {
        searchBox.setText("");
    }

    @Override
    public void setSearchStringChangedHandler(SearchStringChangedHandler handler) {
        searchStringChangedHandler = checkNotNull(handler);
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