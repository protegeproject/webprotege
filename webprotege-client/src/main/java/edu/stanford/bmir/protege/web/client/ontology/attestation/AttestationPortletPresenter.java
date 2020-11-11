package edu.stanford.bmir.protege.web.client.ontology.attestation;

import ch.unifr.digits.webprotege.attestation.client.ClientAttestationService;
import ch.unifr.digits.webprotege.attestation.shared.*;
import com.google.gwt.core.client.Callback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.protege.widgetmap.client.HasFixedPrimaryAxisSize;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.inject.Inject;
import java.util.function.Consumer;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
@Portlet(id = "portlets.OntologyAttestation", title = "Ontology Attestation")
public class AttestationPortletPresenter extends AbstractWebProtegePortletPresenter implements HasFixedPrimaryAxisSize,
        AttestationPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private AttestationView editor;
    private final ProjectId projectId;
    private String name = "John Doe";
    private GetAttestationSettingsActionResult attestationSettings;

    @Inject
    public AttestationPortletPresenter(SelectionModel selectionModel, DispatchServiceManager dispatchServiceManager, ProjectId projectId, DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        editor = new AttestationViewImpl(projectId, this);
        editor.setEnabled(false);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editor.asWidget());
        dispatchServiceManager.execute(new GetRootOntologyIdAction(getProjectId()),
                result -> editor.setValue(result.getObject()));
        dispatchServiceManager.execute(new GetAttestationSettingsAction(),
                result -> attestationSettings = result);
    }

    @Override
    public int getFixedPrimaryAxisSize() {
        return 80;
    }

    @Override
    public void fileSign(String ontologyIri, String versionIri, Callback<Boolean, Object> callback) {
        RevisionNumber head = RevisionNumber.getHeadRevisionNumber();
        ClientAttestationService.signProjectFile(projectId, head, ontologyIri, versionIri,
                name, attestationSettings.getAddressFileContract(), callback);
    }

    @Override
    public void fileVerify(String ontologyIri, String versionIri, Callback<VerifyResult, Object> callback) {
        RevisionNumber head = RevisionNumber.getHeadRevisionNumber();
        ClientAttestationService.verifyProjectFile(projectId, head, ontologyIri, versionIri,
                attestationSettings.getAddressFileContract(), callback);
    }

    @Override
    public void owlSign(String ontologyIri, String versionIri, Callback<Boolean, Object>  callback) {
        Consumer<OntologyHashResult> consumer = (OntologyHashResult result) -> {
            ClientAttestationService.signOntology(ontologyIri, versionIri, name, attestationSettings.getAddressOntologyContract(),
                    result.getHash(), result.getClassHashes(), callback);
        };
        dispatchServiceManager.execute(new OntologyHashAction(projectId, ontologyIri), consumer);
    }

    @Override
    public void owlVerify(String ontologyIri, String versionIri, Callback<VerifyResult, Object> callback) {
        Consumer<OntologyHashResult> consumer = (OntologyHashResult result) -> {
            ClientAttestationService.verifyOntology(ontologyIri, versionIri, attestationSettings.getAddressOntologyContract(),
                    result.getHash(), callback);
        };
        dispatchServiceManager.execute(new OntologyHashAction(projectId, ontologyIri), consumer);
    }
}
