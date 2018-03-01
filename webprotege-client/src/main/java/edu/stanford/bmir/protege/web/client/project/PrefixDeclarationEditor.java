package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Feb 2018
 */
public class PrefixDeclarationEditor extends Composite implements ValueEditor<PrefixDeclaration>, HasEnabled {

    interface PrefixDeclarationEditorUiBinder extends UiBinder<HTMLPanel, PrefixDeclarationEditor> {
    }

    private static PrefixDeclarationEditorUiBinder ourUiBinder = GWT.create(PrefixDeclarationEditorUiBinder.class);

    @UiField
    PlaceholderTextBox prefixNameField;

    @UiField
    PlaceholderTextBox prefixField;

    private boolean dirty = false;

    public PrefixDeclarationEditor() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("prefixNameField")
    protected void prefixNameChanged(ValueChangeEvent<String> event) {
        normalizePrefixName();
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("prefixNameField")
    protected void prefixNameFieldBlurred(BlurEvent blurEvent) {
        normalizePrefixName();
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("prefixField")
    protected void prefixChanged(ValueChangeEvent<String> event) {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("prefixField")
    protected void prefixEdited(KeyUpEvent event) {
        normalizePrefix();
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    /**
     * Normalize the prefix name by replacing spaces with underscores and appending a colon
     * to the prefix name if the user has not already done this.
     */
    private void normalizePrefixName() {
        String prefixName = prefixNameField.getValue().trim();
        String normalizedPrefixName = prefixName.replace(" ", "_");
        if(!normalizedPrefixName.isEmpty() && !normalizedPrefixName.endsWith(":")) {
            normalizedPrefixName = normalizedPrefixName + ":";
        }
        if (!normalizedPrefixName.equals(prefixName)) {
            prefixNameField.setValue(normalizedPrefixName);
        }
    }

    /**
     * Normalize the prefix by replacing spaces with underscores.
     */
    private void normalizePrefix() {
        String prefix = prefixField.getValue();
        String normalizedPrefix = prefix.replace(" ", "_");
        if(!normalizedPrefix.equals(prefix)) {
            prefixField.setValue(normalizedPrefix);
        }
    }

    @Override
    public void setValue(PrefixDeclaration object) {
        dirty = false;
        prefixNameField.setValue(object.getPrefixName());
        prefixField.setValue(object.getPrefix());
    }

    @Override
    public void clearValue() {
        dirty = false;
        prefixNameField.setValue("");
        prefixField.setValue("");
    }

    @Nonnull
    private String getPrefixName() {
        return prefixNameField.getValue().trim();
    }

    @Nonnull
    private String getPrefix() {
        return prefixField.getValue().trim();
    }

    @Override
    public Optional<PrefixDeclaration> getValue() {
        normalizePrefixName();
        normalizePrefix();
        String prefixName = getPrefixName();
        String prefix = getPrefix();
        if(prefix.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(PrefixDeclaration.get(prefixName, prefix));
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<PrefixDeclaration>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
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
    public boolean isWellFormed() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return prefixNameField.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        prefixNameField.setEnabled(enabled);
        prefixField.setEnabled(enabled);
    }
}