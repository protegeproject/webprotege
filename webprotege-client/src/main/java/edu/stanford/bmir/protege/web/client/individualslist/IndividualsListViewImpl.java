package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.list.ListBox;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.search.SearchStringChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.lang.DisplayDictionaryLanguage;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
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

    private IndividualsListCellRenderer renderer;

    interface IndividualsListViewImplUiBinder extends UiBinder<HTMLPanel, IndividualsListViewImpl> {

    }

    private static IndividualsListViewImplUiBinder ourUiBinder = GWT.create(IndividualsListViewImplUiBinder.class);

    @UiField
    protected ListBox<OWLNamedIndividual, EntityNode> individualsList;

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
    public IndividualsListViewImpl(@Nonnull PaginatorPresenter paginatorPresenter,
                                   @Nonnull IndividualsListCellRenderer renderer) {
        this.paginatorPresenter = paginatorPresenter;
        paginator = paginatorPresenter.getView();
        this.renderer = checkNotNull(renderer);
        initWidget(ourUiBinder.createAndBindUi(this));
        individualsList.setRenderer(renderer);
        individualsList.setKeyExtractor(node -> (OWLNamedIndividual) node.getEntity());
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
    public void setDisplayLanguage(@Nonnull DisplayDictionaryLanguage language) {
        renderer.setDisplayLanguage(language);
        individualsList.setRenderer(renderer);
    }

    @Override
    public void setListData(List<EntityNode> individuals) {
        individualsList.setListData(individuals);
    }

    @Override
    public void addListData(Collection<EntityNode> individuals) {
//        individualsList.getListData();
//        individualsList.addAll(individuals);
        MessageBox.showAlert("Missing Impl");
    }

    @Override
    public void removeListData(Collection<EntityNode> individuals) {
        individualsList.setListData(ImmutableList.of());
    }

    @Override
    public Collection<EntityNode> getSelectedIndividuals() {
        return individualsList.getSelection();
    }

    @Override
    public Optional<EntityNode> getSelectedIndividual() {
        return individualsList.getFirstSelectedElement();
    }

    @Override
    public void setSelectedIndividual(OWLNamedIndividualData individual) {
        individualsList.setSelection(individual.getEntity());
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<List<EntityNode>> handler) {
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