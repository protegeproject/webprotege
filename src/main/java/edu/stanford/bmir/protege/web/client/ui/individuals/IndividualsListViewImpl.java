package edu.stanford.bmir.protege.web.client.ui.individuals;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.entitieslist.EntitiesListImpl;
import edu.stanford.bmir.protege.web.client.individualslist.IndividualsListView;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;

import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class IndividualsListViewImpl extends Composite implements IndividualsListView {

    interface IndividualsListViewImplUiBinder extends UiBinder<HTMLPanel, IndividualsListViewImpl> {

    }

    private static IndividualsListViewImplUiBinder ourUiBinder = GWT.create(IndividualsListViewImplUiBinder.class);

    @UiField
    protected EntitiesListImpl<OWLNamedIndividualData> individualsList;

    public IndividualsListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
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
        return individualsList.getSelectedEntity().asSet();
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

}