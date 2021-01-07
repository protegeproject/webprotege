package edu.stanford.bmir.protege.web.client.ontology.attestation;

import ch.unifr.digits.webprotege.attestation.client.ClientAttestationService;
import ch.unifr.digits.webprotege.attestation.shared.VerifyResult;
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
    private final AttestationPresenter presenter;

    @UiField
    protected TextBoxBase ontologyIRIField;
    @UiField
    protected TextBoxBase versionIRIField;
    @UiField
    protected Button fileSignButton;
    @UiField
    protected Label fileSignResult;
    @UiField
    protected Button fileVerifyButton;
    @UiField
    protected Label fileVerifyResult;
    @UiField
    protected Button owlSignButton;
    @UiField
    protected Label owlSignResult;
    @UiField
    protected Button owlVerifyButton;
    @UiField
    protected Label owlVerifyResult;

    private boolean dirty = false;

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

    public AttestationViewImpl(ProjectId projectId, AttestationPresenter presenter) {
        this.projectId = projectId;
        this.presenter = presenter;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);

        Callback<Boolean, Object> fileSignCallback = new Callback<Boolean, Object>() {
            @Override
            public void onFailure(Object reason) {
                GWT.log(reason.toString());
                fileSignResult.setText("Error while attesting ontology!");
            }

            @Override
            public void onSuccess(Boolean result) {
                GWT.log("Attestation result: " + result.toString());
                if (result) {
                    fileSignResult.setText("Successfully attested ontology!");
                } else {
                    fileSignResult.setText("Failed to attested ontology!");
                }
            }
        };

        Callback<VerifyResult, Object> fileVerifyCallback = new Callback<VerifyResult, Object>() {
            @Override
            public void onFailure(Object reason) {
                GWT.log(reason.toString());
                fileVerifyResult.setText("Error while verifying ontology!");
            }

            @Override
            public void onSuccess(VerifyResult result) {
                if (!result.isValid()) {
                    fileVerifyResult.setText("Ontology was not attested!");
                } else {
                    fileVerifyResult.setText(result.toString());
                }
            }
        };

        Callback<Boolean, Object> owlSignCallback = new Callback<Boolean, Object>() {
            @Override
            public void onFailure(Object reason) {
                GWT.log(reason.toString());
                owlSignResult.setText("Error while attesting ontology!");
            }

            @Override
            public void onSuccess(Boolean result) {
                GWT.log("Attestation result: " + result.toString());
                if (result) {
                    owlSignResult.setText("Successfully attested ontology!");
                } else {
                    owlSignResult.setText("Failed to attested ontology!");
                }
            }
        };

        Callback<VerifyResult, Object> owlVerifyCallback = new Callback<VerifyResult, Object>() {
            @Override
            public void onFailure(Object reason) {
                GWT.log(reason.toString());
                owlVerifyResult.setText("Error while verifying ontology!");
            }

            @Override
            public void onSuccess(VerifyResult result) {
                if (!result.isValid()) {
                    owlVerifyResult.setText("Ontology was not attested!");
                } else {
                    owlVerifyResult.setText(result.toString());
                }
            }
        };

        fileSignButton.addClickHandler((event -> {
            presenter.fileSign(ontologyIRIField.getValue(), versionIRIField.getValue(), fileSignCallback);
        }));
        fileVerifyButton.addClickHandler((event -> {
            presenter.fileVerify(ontologyIRIField.getValue(), versionIRIField.getValue(), fileVerifyCallback);
        }));
        owlSignButton.addClickHandler((event -> {
            presenter.owlSign(ontologyIRIField.getValue(), versionIRIField.getValue(), owlSignCallback);
        }));
        owlVerifyButton.addClickHandler((event -> {
            presenter.owlVerify(ontologyIRIField.getValue(), versionIRIField.getValue(), owlVerifyCallback);
        }));

        initWidget(rootElement);
    }

    @Override
    public Widget getWidget() {
        return this;
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
