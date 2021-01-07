package edu.stanford.bmir.protege.web.client.ontology.attestation;

import ch.unifr.digits.webprotege.attestation.client.ClientAttestationService;
import ch.unifr.digits.webprotege.attestation.shared.VerifyAction;
import ch.unifr.digits.webprotege.attestation.shared.VerifyResult;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

public class EditorAttestationPane extends SimplePanel implements EditorPaneAttestationView {

    private final ProjectId projectId;
    private final DispatchServiceManager dispatchServiceManager;

    @UiField
    protected HasText iriField;
    @UiField
    protected Label attest;
    @UiField
    protected Label signer;



    private OWLClassData currentSubject;

    private ClassFrame currentFrame;

    private EntityDisplay entityDisplay = entityData -> {};
    private OWLOntologyID ontologyID;

    interface EditorAttestationPane2UiBinder extends UiBinder<HTMLPanel, EditorAttestationPane> {
    }

    private static EditorAttestationPane2UiBinder ourUiBinder = GWT.create(EditorAttestationPane2UiBinder.class);

    @Inject
    public EditorAttestationPane(ProjectId projectId, DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        setWidget(rootElement);
        attest.getElement().setAttribute("data-attest", "false");
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public Optional<ClassFrame> getValue() {
        if(currentSubject == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(ClassFrame.get(currentSubject, ImmutableSet.<OWLClassData>builder().build(),
                    ImmutableSet.<PropertyValue>builder().build()));
        }
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }

    @Override
    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = entityDisplay;
    }

    @Override
    public void setValue(ClassFrame frame) {
        this.currentFrame = frame;
        this.currentSubject = frame.getSubject();

        String decodedIri = URL.decode(currentSubject.getEntity().getIRI().toString());
        iriField.setText(decodedIri);
        dispatch();
    }

    @Override
    public void clearValue() {
        entityDisplay.setDisplayedEntity(Optional.empty());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<ClassFrame>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public Widget getWidget() {
        return this;
    }


    public void setOntologyID(OWLOntologyID object) {
        this.ontologyID = object;
    }

    private void dispatch() {
        int hashEntity = ClientAttestationService.hashEntity(currentSubject.getEntity());
        String ontologyIri = ontologyID.getOntologyIRI().or(IRI.create("")).toString();
        String versionIri = ontologyID.getVersionIRI().or(IRI.create("")).toString();

        GWT.log("[attestation] verify " + iriField.getText());
        GWT.log("[attestation] hash " + hashEntity);
        VerifyAction action = new VerifyAction(projectId, ontologyIri, versionIri, String.valueOf(hashEntity), currentSubject.getEntity(),
                VerifyAction.Mode.ENTITY);
        Consumer<VerifyResult> consumer = res -> {
            if (res == null) GWT.log("oh no");
            boolean resultBool = Objects.requireNonNull(res).isValid();
            if (resultBool) {
                attest.setText("Attested");
                attest.getElement().setAttribute("data-attest", "true");
                signer.setText("Signed by " + res.getSignerName() + " (" + res.getSigner() +")");
            } else {
                attest.setText("Not attested");
                attest.getElement().setAttribute("data-attest", "false");
                signer.setText("");
            }
        };
        dispatchServiceManager.execute(action, consumer);
    }
}
