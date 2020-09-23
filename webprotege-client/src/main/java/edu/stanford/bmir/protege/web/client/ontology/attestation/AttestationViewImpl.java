package edu.stanford.bmir.protege.web.client.ontology.attestation;

import ch.unifr.digits.webprotege.attestation.AttestationService;
import ch.unifr.digits.webprotege.attestation.contract.VerifyContractReturn;
import ch.unifr.digits.webprotege.attestation.contract.VerifyResult;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.protege.widgetmap.client.HasFixedPrimaryAxisSize;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Optional;

public class AttestationViewImpl extends Composite implements AttestationView, HasFixedPrimaryAxisSize {
    interface AttestationViewImplUiBinder extends UiBinder<HTMLPanel, AttestationViewImpl> {}
    private static AttestationViewImplUiBinder ourUiBinder = GWT.create(AttestationViewImplUiBinder.class);

    private final ProjectId projectId;

    @UiField
    protected TextBoxBase ontologyIRIField;
    @UiField
    protected TextBoxBase versionIRIField;
    @UiField
    protected TextBox name;
    @UiField
    protected TextBox address;
    @UiField
    protected Button signButton;
    @UiField
    protected Label signResult;
    @UiField
    protected Button verifyButton;
    @UiField
    protected Label verifyResult;


    private boolean dirty = false;

    @Override
    public Widget getWidget() {
        return this;
    }

    @UiHandler("ontologyIRIField")
    protected void handleOntologyIRIChanged(ValueChangeEvent<String> event) {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("versionIRIField")
    protected void handleVersionIRIChanged(ValueChangeEvent<String> event) {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("name")
    protected void handleNameChanged(ValueChangeEvent<String> event) {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("address")
    protected void handleAddressChanged(ValueChangeEvent<String> event) {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    public AttestationViewImpl(ProjectId projectId) {
        this.projectId = projectId;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        name.getElement().setPropertyString("placeholder", "Enter your name");

        RevisionNumber head = RevisionNumber.getHeadRevisionNumber();

        Callback<Boolean, Object> signCallback = new Callback<Boolean, Object>() {
            @Override
            public void onFailure(Object reason) {
                GWT.log(reason.toString());
                signResult.setText("Error while attesting ontology!");
            }

            @Override
            public void onSuccess(Boolean result) {
                GWT.log("Attestation result: " + result.toString());
                if (result) {
                    signResult.setText("Successfully attested ontology!");
                } else {
                    signResult.setText("Failed to attested ontology!");
                }
            }
        };

        Callback<VerifyResult, Object> verifyCallback = new Callback<VerifyResult, Object>() {
            @Override
            public void onFailure(Object reason) {
                GWT.log(reason.toString());
                verifyResult.setText("Error while verifying ontology!");
            }

            @Override
            public void onSuccess(VerifyResult result) {
                if (!result.isValid()) {
                    verifyResult.setText("Ontology was not attested!");
                } else {
//                    String text = "Signer name: " + result.getSignerName() + "\n"
//                            + "Signer address: " + result.getSigner() + "\n"
//                            + "Timestamp: " + result.getTimestamp();
                    verifyResult.setText(result.toString());
                }
            }
        };

        signButton.addClickHandler((event -> {
            AttestationService.signOntology(projectId, head, ontologyIRIField.getValue(), versionIRIField.getValue(),
                    name.getValue(), address.getValue(), signCallback);
        }));
        verifyButton.addClickHandler((event -> {
            AttestationService.verifyOntology(projectId, head, ontologyIRIField.getValue(), versionIRIField.getValue(),
                    address.getValue(), verifyCallback);
        }));
        initWidget(rootElement);
    }


    @Override
    public void clearValue() {
        ontologyIRIField.setText("");
        versionIRIField.setText("");
        dirty = false;
    }

    @Override
    public void setValue(OWLOntologyID object) {
        dirty = false;
        ontologyIRIField.setValue("");
        versionIRIField.setValue("");
        if(object.isAnonymous()) {
            return;
        }
        if (object.getOntologyIRI().isPresent()) {
            ontologyIRIField.setValue(object.getOntologyIRI().get().toString());
            final com.google.common.base.Optional<IRI> versionIRI = object.getVersionIRI();
            if(versionIRI.isPresent()) {
                versionIRIField.setValue(versionIRI.get().toString());
            }
            else {
                versionIRIField.setText("");
            }
        }
    }

    @Override
    public Optional<OWLOntologyID> getValue() {
        String ontologyIRI = ontologyIRIField.getValue().trim();
        if(ontologyIRI.isEmpty()) {
            return Optional.of(new OWLOntologyID());
        }
        String versionIRI = versionIRIField.getValue().trim();
        if(versionIRI.isEmpty()) {
            return Optional.of(new OWLOntologyID(IRI.create(ontologyIRI)));
        }
        else {
            return Optional.of(new OWLOntologyID(IRI.create(ontologyIRI), IRI.create(versionIRI)));
        }
    }

    @Override
    public boolean isEnabled() {
        return ontologyIRIField.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        ontologyIRIField.setEnabled(enabled);
        versionIRIField.setEnabled(enabled);
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<OWLOntologyID>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }

    @Override
    public int getFixedPrimaryAxisSize() {
        return 100;
    }


}
