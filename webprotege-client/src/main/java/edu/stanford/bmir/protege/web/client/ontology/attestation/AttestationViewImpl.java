package edu.stanford.bmir.protege.web.client.ontology.attestation;

import ch.unifr.digits.webprotege.attestation.shared.VerifyResult;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.client.DateTimeFormatRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.*;
import java.util.stream.Collectors;

public class AttestationViewImpl extends Composite implements AttestationView {
    interface AttestationViewImplUiBinder extends UiBinder<HTMLPanel, AttestationViewImpl> {}
    private static AttestationViewImplUiBinder ourUiBinder = GWT.create(AttestationViewImplUiBinder.class);

    private final ProjectId projectId;
    private final AttestationPresenter presenter;
    private Set<OWLOntologyID> projectOntologyIDs;

    @UiField
    protected Label versionIRIField;
//    @UiField
//    protected Button fileSignButton;
//    @UiField
//    protected Label fileSignResult;
//    @UiField
//    protected Button fileVerifyButton;
//    @UiField
//    protected Label fileVerifyResult;
    @UiField
    protected Button owlSignButton;
    @UiField
    protected Label owlSignResult;
    @UiField
    protected Button owlVerifyButton;
    @UiField
    protected Label owlVerifyResult;
    @UiField
    protected Label address;
    @UiField
    protected Label status;
    @UiField
    protected Label signer;
    @UiField
    protected ValueListBox<String> projectIRIs;
    @UiField
    protected Label timestamp;

    private boolean dirty = false;

    public AttestationViewImpl(ProjectId projectId, AttestationPresenter presenter) {
        this.projectId = projectId;
        this.presenter = presenter;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);


//        Callback<Boolean, Object> fileSignCallback = new Callback<Boolean, Object>() {
//            @Override
//            public void onFailure(Object reason) {
//                GWT.log(reason.toString());
//                fileSignResult.setText("Error while attesting ontology!");
//            }
//
//            @Override
//            public void onSuccess(Boolean result) {
//                GWT.log("Attestation result: " + result.toString());
//                if (result) {
//                    fileSignResult.setText("Successfully attested ontology!");
//                } else {
//                    fileSignResult.setText("Failed to attested ontology!");
//                }
//            }
//        };

//        Callback<VerifyResult, Object> fileVerifyCallback = new Callback<VerifyResult, Object>() {
//            @Override
//            public void onFailure(Object reason) {
//                GWT.log(reason.toString());
//                fileVerifyResult.setText("Error while verifying ontology!");
//            }
//
//            @Override
//            public void onSuccess(VerifyResult result) {
//                if (!result.isValid()) {
//                    fileVerifyResult.setText("Ontology was not attested!");
//                } else {
//                    fileVerifyResult.setText(result.toString());
//                }
//            }
//        };

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
//                owlVerifyResult.setText("Error while verifying ontology!");
                status.setText("Error while verifying ontology!");
                status.getElement().setAttribute("data-attest", "false");
                signer.setText("");
            }

            @Override
            public void onSuccess(VerifyResult result) {
                if (!result.isValid()) {
//                    owlVerifyResult.setText("Ontology was not attested!");
                    status.setText("Current ontology revision not attested");
                    status.getElement().setAttribute("data-attest", "false");
                    signer.setText("");
                } else {
//                    owlVerifyResult.setText(result.toString());
                    status.setText("Attested");
                    status.getElement().setAttribute("data-attest", "true");
                    signer.setText("Signed by " + result.getSignerName() + " (" + result.getSigner() +")");

                    JsDate jsDate = JsDate.create(result.getTimestamp() * 1000);
                    GWT.log("timestamp " + result.getTimestamp() + " " + jsDate.toString());
                    timestamp.setText(jsDate.toString());
                }
            }
        };

        projectIRIs.addValueChangeHandler(event -> versionIRIField.setText(getVersionIRI(event.getValue()).toString()));
//        fileSignButton.addClickHandler((event -> {
//            presenter.fileSign(projectIRIs.getValue(), versionIRIField.getText(), fileSignCallback);
//        }));
//        fileVerifyButton.addClickHandler((event -> {
//            presenter.fileVerify(projectIRIs.getValue(), versionIRIField.getText(), fileVerifyCallback);
//        }));
        owlSignButton.addClickHandler((event -> {
            GWT.log("Signing: IRI " + projectIRIs.getValue());
            presenter.owlSign(projectIRIs.getValue(), versionIRIField.getText(), owlSignCallback);
        }));
        owlVerifyButton.addClickHandler((event -> {
            GWT.log("Verify: IRI " + projectIRIs.getValue());
            presenter.owlVerify(projectIRIs.getValue(), versionIRIField.getText(), owlVerifyCallback);
        }));

        initWidget(rootElement);
    }

    public void setContractAddress(String caddress) {
        address.setText(caddress);
    }

    @Override
    public void setIDs(Set<OWLOntologyID> ids) {
        projectOntologyIDs = ids;
        List<String> list = ids.stream().map(OWLOntologyID::getOntologyIRI).filter(com.google.common.base.Optional::isPresent)
                .map(com.google.common.base.Optional::get).map(IRI::toString).collect(Collectors.toList());
        projectIRIs.setAcceptableValues(list);
        String def = list.get(list.size()-1);
        projectIRIs.setValue(def);
        versionIRIField.setText(getVersionIRI(def).toString());
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public void clearValue() {
        versionIRIField.setText("");
        dirty = false;
    }

    @Override
    public void setValue(OWLOntologyID object) {
        dirty = false;
        versionIRIField.setText("");
        if(object.isAnonymous()) {
            return;
        }
    }

    @Override
    public Optional<OWLOntologyID> getValue() {
        return getIdForIRI(projectIRIs.getValue());
    }

    @Override
    public boolean isEnabled() {
        return owlSignButton.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {}

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

    private Optional<OWLOntologyID> getIdForIRI(String iri) {
        return projectOntologyIDs.stream().filter(id ->
                id.getOntologyIRI().isPresent() && id.getOntologyIRI().get().toString().equals(iri)
        ).findFirst();
    }

    private IRI getVersionIRI(String iri) {
        Optional<OWLOntologyID> ontologyID = getIdForIRI(iri);
        return ontologyID.map(owlOntologyID -> owlOntologyID.getVersionIRI().or(IRI.create(""))).orElseGet(() -> IRI.create(""));
    }


}
