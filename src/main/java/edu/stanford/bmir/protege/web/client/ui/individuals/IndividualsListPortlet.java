package edu.stanford.bmir.protege.web.client.ui.individuals;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.individualslist.IndividualsListPresenter;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

@Portlet(id = "portlets.IndividualsList", title = "Individuals by Class")
public class IndividualsListPortlet extends AbstractWebProtegePortlet {

    private final IndividualsListPresenter presenter;

    /*
     * Retrieved from the project configuration. If it is set,
     * then the individuals list will always display the instances
     * of the preconfigured class.
     */
    // TODO: This needs fixing
    private Optional<OWLClass> preconfiguredClass = Optional.absent();

    @Inject
    public IndividualsListPortlet(IndividualsListPresenter presenter,
                                  SelectionModel selectionModel,
                                  EventBus eventBus,
                                  ProjectId projectId) {
        super(selectionModel, eventBus, projectId);
        this.presenter = presenter;
        setTitle("Individuals by Class");
        presenter.installActions(this);
        presenter.start(this);
    }

    @Override
    protected void handleAfterSetEntity(java.util.Optional<OWLEntity> entity) {
        Optional<OWLClass> selectedClass;
        if(preconfiguredClass.isPresent()) {
            selectedClass = preconfiguredClass;
        }
        else if(getSelectionModel().getLastSelectedClass().isPresent()) {
            selectedClass = Optional.of(getSelectionModel().getLastSelectedClass().get());
        }
        else {
            selectedClass = Optional.absent();
        }


        if(selectedClass.isPresent()) {
            presenter.setType(selectedClass.get());
            updateTitle(selectedClass);
        }
        else {
            presenter.clearType();
        }
    }

    private void updateTitle(Optional<? extends OWLEntity> entityData) {
        if(entityData.isPresent()) {
            setTitle("Individuals by Class");
        }
        else {
            setTitle("Individuals by Class (nothing selected)");
        }
    }

    @Override
    public void handlePermissionsChanged() {
    }


}
