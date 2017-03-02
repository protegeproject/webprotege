package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public interface IndividualsListView extends HasSelectionHandlers<OWLNamedIndividualData>, HasBusy, IsWidget {

    interface SearchStringChangedHandler {
        void handleSearchStringChanged();
    }

    void setListData(List<OWLNamedIndividualData> individuals);

    void addListData(Collection<OWLNamedIndividualData> individuals);

    void removeListData(Collection<OWLNamedIndividualData> individuals);

    Collection<OWLNamedIndividualData> getSelectedIndividuals();

    Optional<OWLNamedIndividualData> getSelectedIndividual();

    void setSelectedIndividual(OWLNamedIndividualData individual);

    void setStatusMessage(String statusMessage);

    void setStatusMessageVisible(boolean visible);

    String getSearchString();

    void clearSearchString();

    void setSearchStringChangedHandler(SearchStringChangedHandler handler);

    void setPageCount(int pageCount);

    void setPageNumber(int pageNumber);

    int getPageNumber();

    void setPageNumberChangedHandler(PaginatorPresenter.PageNumberChangedHandler handler);
}
