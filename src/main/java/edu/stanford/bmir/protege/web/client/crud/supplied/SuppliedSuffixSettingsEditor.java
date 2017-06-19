package edu.stanford.bmir.protege.web.client.crud.supplied;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;

import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class SuppliedSuffixSettingsEditor extends Composite implements EntityCrudKitSuffixSettingsEditor<SuppliedNameSuffixSettings> {

    interface SuffixGeneratorSettingsEditorUiBinder extends UiBinder<HTMLPanel, SuppliedSuffixSettingsEditor> {

    }

    private static SuffixGeneratorSettingsEditorUiBinder ourUiBinder = GWT.create(SuffixGeneratorSettingsEditorUiBinder.class);

    @UiField
    protected ListBox whiteSpaceTreatmentListBox;

    public SuppliedSuffixSettingsEditor() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        for(WhiteSpaceTreatment whiteSpaceTreatment : WhiteSpaceTreatment.values()) {
            whiteSpaceTreatmentListBox.addItem(whiteSpaceTreatment.getDisplayName());
        }
    }


    @Override
    public boolean isWellFormed() {
        return true;
    }

    public WhiteSpaceTreatment getWhiteSpaceTreatment() {
        int selIndex = whiteSpaceTreatmentListBox.getSelectedIndex();
        int whiteSpaceTreatmentIndex;
        if(selIndex == 0) {
            whiteSpaceTreatmentIndex = 0;
        }
        else {
            whiteSpaceTreatmentIndex = selIndex;
        }
        return WhiteSpaceTreatment.values()[whiteSpaceTreatmentIndex];
    }

    @Override
    public void setValue(SuppliedNameSuffixSettings object) {
        int whiteSpaceTreatmentIndex = object.getWhiteSpaceTreatment().ordinal();
        whiteSpaceTreatmentListBox.setSelectedIndex(whiteSpaceTreatmentIndex);
    }

    @Override
    public void clearValue() {

    }

    @Override
    public Optional<SuppliedNameSuffixSettings> getValue() {
        return Optional.of(new SuppliedNameSuffixSettings(getWhiteSpaceTreatment()));
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<SuppliedNameSuffixSettings>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}