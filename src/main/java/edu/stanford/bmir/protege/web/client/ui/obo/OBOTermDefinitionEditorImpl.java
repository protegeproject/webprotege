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
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.resources.WebProtegeResourceBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermDefinition;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/06/2013
 */
public class OBOTermDefinitionEditorImpl extends Composite implements OBOTermDefinitionEditor {

    interface OBOTermDefinitionEditorImplUiBinder extends UiBinder<HTMLPanel, OBOTermDefinitionEditorImpl> {

    }

    private static OBOTermDefinitionEditorImplUiBinder ourUiBinder = GWT.create(OBOTermDefinitionEditorImplUiBinder.class);

    @UiField
    protected TextBoxBase definitionField;

    @UiField
    protected XRefListEditor xrefsField;


    private boolean dirty = false;


    public OBOTermDefinitionEditorImpl() {
        WebProtegeResourceBundle.INSTANCE.style().ensureInjected();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @UiHandler("definitionField")
    protected void handleDefinitionChanged(ValueChangeEvent<String> event) {
        dirty = true;
        fireEvent(new DirtyChangedEvent());
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("xrefsField")
    protected void handleXRefsChanged(ValueChangeEvent<Optional<List<OBOXRef>>> event) {
        dirty = true;
        fireEvent(new DirtyChangedEvent());
        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }

    @Override
    public void setValue(OBOTermDefinition object) {
        definitionField.setText(object.getDefinition());
        xrefsField.setValue(object.getXRefs());
        dirty = false;
    }

    @Override
    public void clearValue() {
        definitionField.setText("");
        xrefsField.clearValue();
        dirty = false;
    }

    @Override
    public Optional<OBOTermDefinition> getValue() {
        if(getDefinition().isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(new OBOTermDefinition(xrefsField.getValue().get(), getDefinition()));
    }

    private String getDefinition() {
        return definitionField.getValue().trim();
    }

    @Override
    public boolean isDirty() {
        return dirty || xrefsField.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<OBOTermDefinition>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
