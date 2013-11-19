package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import edu.stanford.bmir.protege.web.client.action.HasCreateHandler;
import edu.stanford.bmir.protege.web.client.action.HasDeleteHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;

import java.util.Collection;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public interface IndividualsListView extends HasSelectionHandlers<OWLNamedIndividualData>, HasCreateHandler, HasDeleteHandler {

    void setListData(List<OWLNamedIndividualData> individuals);

    void addListData(Collection<OWLNamedIndividualData> individuals);

    void removeListData(Collection<OWLNamedIndividualData> individuals);

    Collection<OWLNamedIndividualData> getSelectedIndividuals();

    Optional<OWLNamedIndividualData> getSelectedIndividual();

    void setSelectedIndividual(OWLNamedIndividualData individual);
}
