package edu.stanford.bmir.protege.web.client.ontology.id;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.protege.widgetmap.client.HasFixedPrimaryAxisSize;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class OntologyIdViewImpl extends Composite implements OntologyIdView, HasFixedPrimaryAxisSize {

    interface OntologyIdViewImplUiBinder extends UiBinder<HTMLPanel, OntologyIdViewImpl> {

    }

    private static OntologyIdViewImplUiBinder ourUiBinder = GWT.create(OntologyIdViewImplUiBinder.class);

    @UiField
    protected TextBoxBase ontologyIRIField;

    @UiField
    protected TextBoxBase versionIRIField;


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

    public OntologyIdViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
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
            final Optional<IRI> versionIRI = object.getVersionIRI();
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