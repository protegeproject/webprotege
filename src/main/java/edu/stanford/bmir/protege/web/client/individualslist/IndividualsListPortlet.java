package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
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
                                  ProjectId projectId) {
        super(selectionModel, projectId);
        this.presenter = presenter;
    }

    @Override
    public void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setViewTitle("Individuals by Class");
        presenter.installActions(portletUi);
        presenter.start(portletUi);
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
        }
        else {
            presenter.clearType();
        }
    }
}
