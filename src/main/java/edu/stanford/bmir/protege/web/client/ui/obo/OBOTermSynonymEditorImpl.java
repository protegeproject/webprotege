package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonym;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonymScope;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;

import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/06/2013
 */
public class OBOTermSynonymEditorImpl extends Composite implements OBOTermSynonymEditor {

    interface OBOTermSynonymEditorImplUiBinder extends UiBinder<HTMLPanel, OBOTermSynonymEditorImpl> {

    }

    private static OBOTermSynonymEditorImplUiBinder ourUiBinder = GWT.create(OBOTermSynonymEditorImplUiBinder.class);

    @UiField
    protected TextBox synonymField;


    @UiField
    protected ListBox synonymTypeField;


    @UiField
    protected XRefListEditor xrefListField;


    private boolean dirty;


    public OBOTermSynonymEditorImpl() {
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        setupSynonymTypeField();
    }

    private void setupSynonymTypeField() {
        for(OBOTermSynonymScope synonymScope : OBOTermSynonymScope.values()) {
            synonymTypeField.addItem(synonymScope.getDisplayText());
        }
    }



    @Override
    public Widget getWidget() {
        return this;
    }


    @UiHandler("synonymField")
    protected void handleSynonymNameChanged(ValueChangeEvent<String> event) {
        setDirty(true);
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("synonymTypeField")
    protected void handleSynonymScopeChanged(ChangeEvent event) {
        setDirty(true);
        ValueChangeEvent.fire(this, getValue());

    }

    @UiHandler("xrefListField")
    protected void handleXRefsChanged(ValueChangeEvent<Optional<List<OBOXRef>>> event) {
        setDirty(true);
        ValueChangeEvent.fire(this, getValue());
    }



    @Override
    public boolean isWellFormed() {
        return true;
    }

    @Override
    public void setValue(OBOTermSynonym object) {
        synonymField.setValue(object.getName());
        synonymTypeField.setSelectedIndex(object.getScope().ordinal());
        xrefListField.setValue(object.getXRefs());
        dirty = false;
    }

    @Override
    public void clearValue() {
        synonymField.setText("");
        synonymTypeField.setSelectedIndex(0);
        xrefListField.clearValue();
        dirty = false;
    }

    @Override
    public Optional<OBOTermSynonym> getValue() {
        final String synonymName = getSynonymName();
        if(synonymName.isEmpty()) {
            return Optional.absent();
        }
        final List<OBOXRef> xrefs = xrefListField.getValue().or(Collections.<OBOXRef>emptyList());
        return Optional.of(new OBOTermSynonym(xrefs, synonymName, getSynonymScope()));
    }

    private OBOTermSynonymScope getSynonymScope() {
        final int selectedIndex = synonymTypeField.getSelectedIndex();
        if(selectedIndex == -1) {
            return OBOTermSynonymScope.EXACT;
        }
        return OBOTermSynonymScope.values()[selectedIndex];
    }

    private String getSynonymName() {
        return synonymField.getValue().trim();
    }


    private void setDirty(boolean dirty) {
        this.dirty = dirty;
        fireEvent(new DirtyChangedEvent());
    }

    @Override
    public boolean isDirty() {
        return dirty || xrefListField.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<OBOTermSynonym>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
