package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.search.SearchStringChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public interface IndividualsListView extends HasSelectionHandlers<List<EntityNode>>, HasBusy, IsWidget {

    public AcceptsOneWidget getTypeFieldContainer();

    void setListData(List<EntityNode> individuals);

    void addListData(Collection<EntityNode> individuals);

    void removeListData(Collection<EntityNode> individuals);

    Collection<EntityNode> getSelectedIndividuals();

    Optional<EntityNode> getSelectedIndividual();

    void setSelectedIndividual(OWLNamedIndividualData individual);

    void setStatusMessage(String statusMessage);

    void setStatusMessageVisible(boolean visible);

    String getSearchString();

    void clearSearchString();

    @Nonnull
    InstanceRetrievalMode getRetrievalMode();

    void setRetrievalMode(@Nonnull InstanceRetrievalMode retrievalType);

    void setRetrievalModeEnabled(boolean enabled);

    void setSearchStringChangedHandler(@Nonnull SearchStringChangedHandler handler);

    void setInstanceRetrievalTypeChangedHandler(@Nonnull InstanceRetrievalTypeChangedHandler handler);

    void setPageCount(int pageCount);

    void setPageNumber(int pageNumber);

    int getPageNumber();

    void setPageNumberChangedHandler(HasPagination.PageNumberChangedHandler handler);

    void setDisplayLanguage(@Nonnull DisplayNameSettings language);

    void updateNode(@Nonnull EntityNode entityNode);
}
