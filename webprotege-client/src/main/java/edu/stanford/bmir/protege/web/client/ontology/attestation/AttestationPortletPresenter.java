package edu.stanford.bmir.protege.web.client.ontology.attestation;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.widgetmap.client.HasFixedPrimaryAxisSize;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
@Portlet(id = "portlets.OntologyAttestation", title = "Ontology Attestation")
public class AttestationPortletPresenter extends AbstractWebProtegePortletPresenter implements HasFixedPrimaryAxisSize {

    private final DispatchServiceManager dispatchServiceManager;

    private AttestationView editor;

    @Inject
    public AttestationPortletPresenter(SelectionModel selectionModel, DispatchServiceManager dispatchServiceManager, ProjectId projectId, DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
        this.dispatchServiceManager = dispatchServiceManager;
        editor = new AttestationViewImpl(projectId);
        editor.setEnabled(false);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editor.asWidget());
        updateDisplay();
    }

    private void updateDisplay() {
        dispatchServiceManager.execute(new GetRootOntologyIdAction(getProjectId()),
                                       result -> editor.setValue(result.getObject()));
    }

    @Override
    public int getFixedPrimaryAxisSize() {
        return 80;
    }
}
