package edu.stanford.bmir.protege.web.client.frame;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */

@Portlet(id = "portlets.owl.EntityDescriptionEditor",
        title = "OWL Entity Description Editor",
        tooltip = "Allows the description of the selected entity to be edited in Manchester Syntax.  The complete OWL 2 syntax is supported.")
public class OWLEntityDescriptionEditorPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final ManchesterSyntaxFrameEditorPresenter presenter;

    @Inject
    public OWLEntityDescriptionEditorPortletPresenter(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, ManchesterSyntaxFrameEditorPresenter presenter, DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
        this.presenter = presenter;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        presenter.start(eventBus);
        portletUi.setWidget(presenter.getView());
        handleAfterSetEntity(getSelectedEntity());
        setDisplaySelectedEntityNameAsSubtitle(true);
        presenter.setEntityDisplay(this);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        Optional<OWLEntity> selectedEntity = getSelectedEntity();
        if (selectedEntity.isPresent()) {
            presenter.setSubject(selectedEntity.get());
            setNothingSelectedVisible(false);
        }
        else {
            setNothingSelectedVisible(true);
            presenter.clearSubject();
        }
    }
}
