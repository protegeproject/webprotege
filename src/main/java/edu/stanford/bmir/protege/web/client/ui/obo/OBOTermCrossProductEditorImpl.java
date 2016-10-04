package edu.stanford.bmir.protege.web.client.ui.obo;

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
import com.google.gwt.user.client.ui.Widget;
import javax.inject.Inject;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.obo.OBORelationship;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermCrossProduct;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermRelationships;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/06/2013
 */
public class OBOTermCrossProductEditorImpl extends Composite implements OBOTermCrossProductEditor {

    interface OBOTermCrossProductEditorImplUiBinder extends UiBinder<HTMLPanel, OBOTermCrossProductEditorImpl> {

    }

    private static OBOTermCrossProductEditorImplUiBinder ourUiBinder = GWT.create(OBOTermCrossProductEditorImplUiBinder.class);


    @UiField(provided = true)
    protected PrimitiveDataEditorImpl genusField;

    @UiField(provided =  true)
    protected OBOTermRelationshipEditor relationshipsField;


    @Inject
    public OBOTermCrossProductEditorImpl(PrimitiveDataEditorImpl genusField, OBOTermRelationshipEditor relationshipEditor) {
        this.genusField = genusField;
        relationshipsField = relationshipEditor;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public Widget getWidget() {
        return this;
    }


    @UiHandler("genusField")
    protected void handleGenusChanged(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
        fireEvent(new DirtyChangedEvent());
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("relationshipsField")
    protected void handleRelationshipsChanged(ValueChangeEvent<Optional<List<OBORelationship>>> event) {
        fireEvent(new DirtyChangedEvent());
        ValueChangeEvent.fire(this, getValue());
    }


    @Override
    public boolean isWellFormed() {
        return genusField.isWellFormed() && relationshipsField.isWellFormed();
    }

    @Override
    public void setValue(OBOTermCrossProduct object) {
        if (object.isEmpty()) {
            clearValue();
        }
        else {
            genusField.setValue(object.getGenus().get());
            relationshipsField.setValue(new ArrayList<OBORelationship>(object.getRelationships().getRelationships()));
        }
    }

    @Override
    public void clearValue() {
        genusField.clearValue();
        relationshipsField.clearValue();
    }

    @Override
    public Optional<OBOTermCrossProduct> getValue() {
        if(!genusField.getValue().isPresent()) {
            return Optional.absent();
        }
        if(!relationshipsField.getValue().isPresent()) {
            return Optional.absent();
        }
        final OWLClassData genus = (OWLClassData) genusField.getValue().get();
        final OBOTermRelationships relationships = new OBOTermRelationships(new HashSet<OBORelationship>(relationshipsField.getValue().get()));
        return Optional.of(new OBOTermCrossProduct(Optional.of(genus), relationships));
    }

    @Override
    public boolean isDirty() {
        return genusField.isDirty() || relationshipsField.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<OBOTermCrossProduct>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
