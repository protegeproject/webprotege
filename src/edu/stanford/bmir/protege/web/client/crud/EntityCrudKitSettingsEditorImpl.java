package edu.stanford.bmir.protege.web.client.crud;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.crud.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class EntityCrudKitSettingsEditorImpl extends Composite implements EntityCrudKitSettingsEditor, HasInitialFocusable {

//    private final List<EntityCrudKitSuffixSettingsEditor<?>> editorKit

    private final List<EntityCrudKit> descriptors;

    interface EntityCrudKitSettingsEditorImplUiBinder extends UiBinder<HTMLPanel, EntityCrudKitSettingsEditorImpl> {

    }

    private static EntityCrudKitSettingsEditorImplUiBinder ourUiBinder = GWT.create(EntityCrudKitSettingsEditorImplUiBinder.class);

    @UiField
    protected TextBox iriPrefixEditor;

    @UiField
    protected ListBox schemeSelectorListBox;

    @UiField
    protected SimplePanel schemeSpecificSettingsEditorHolder;

    @UiField
    protected HasText exampleField;


    private EntityCrudKitSuffixSettingsEditor<?> lastEditorKit;

    private List<Optional<EntityCrudKitSuffixSettingsEditor>> touchedEditors = new ArrayList<Optional<EntityCrudKitSuffixSettingsEditor>>();

    public EntityCrudKitSettingsEditorImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        EntityCrudKitManager kitManager = EntityCrudKitManager.get();
        descriptors = new ArrayList<EntityCrudKit>(kitManager.getKits());
        for (EntityCrudKit descriptor : descriptors) {
            schemeSelectorListBox.addItem(descriptor.getDisplayName());
            touchedEditors.add(Optional.<EntityCrudKitSuffixSettingsEditor>absent());
        }
        schemeSelectorListBox.setSelectedIndex(0);
        updateEditor(true);
    }

    @UiHandler("schemeSelectorListBox")
    protected void handleSchemeChanged(ChangeEvent valueChangeEvent) {
        updateEditor(false);
    }

    @UiHandler("schemeSelectorListBox")
    protected void handleKeyPressChange(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            updateEditor(false);
        }
    }

    @UiHandler("iriPrefixEditor")
    protected void handleIRIPrefixChanged(ValueChangeEvent<String> prefixChangedEvent) {
//        updateExampleField();
    }


    @SuppressWarnings("unchecked")
    private Optional<EntityCrudKitSuffixSettingsEditor> updateEditor(boolean forceRefresh) {
        int selIndex = schemeSelectorListBox.getSelectedIndex();
        if (selIndex == -1) {
            return Optional.absent();
        }
        Optional<EntityCrudKitSuffixSettingsEditor> touchedEditor = touchedEditors.get(selIndex);
        EntityCrudKitSuffixSettingsEditor editor;
        final EntityCrudKit descriptor = descriptors.get(selIndex);
        if (touchedEditor.isPresent()) {
            editor = touchedEditor.get();
        }
        else {
            editor = descriptor.getSuffixSettingsEditor();
            touchedEditors.set(selIndex, Optional.of(editor));
            editor.setValue(descriptor.getDefaultSuffixSettings());
            iriPrefixEditor.setValue(descriptor.getDefaultPrefixSettings().getIRIPrefix());
        }
        schemeSpecificSettingsEditorHolder.setWidget(editor);
        return Optional.of(editor);
    }

    private String getIRIPrefix() {
        return iriPrefixEditor.getText().trim();
    }


    @Override
    public boolean isWellFormed() {
        return false;
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(EntityCrudKitSettings object) {
        int index = getDescriptorIndex(object.getSuffixSettings().getKitId());
        if (index == -1) {
            return;
        }
        schemeSelectorListBox.setSelectedIndex(index);
        Optional<EntityCrudKitSuffixSettingsEditor> editor = updateEditor(true);
        iriPrefixEditor.setText(object.getPrefixSettings().getIRIPrefix());
        if (editor.isPresent()) {
            editor.get().setValue(object.getSuffixSettings());
        }

    }

    public int getDescriptorIndex(EntityCrudKitId id) {
        for (int index = 0; index < descriptors.size(); index++) {
            EntityCrudKit descriptor = descriptors.get(index);
            if (descriptor.getKitId().equals(id)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public void clearValue() {
    }

    @Override
    public Optional<EntityCrudKitSettings> getValue() {
        Optional<EntityCrudKitSuffixSettingsEditor> selEditor = updateEditor(false);
        if(!selEditor.isPresent()) {
            return Optional.absent();
        }
        EntityCrudKitSuffixSettingsEditor editor = selEditor.get();
        Optional<?> editedValue = editor.getValue();
        if(!editedValue.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new EntityCrudKitSettings(new EntityCrudKitPrefixSettings(getIRIPrefix()), (EntityCrudKitSuffixSettings) editedValue.get()));
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return null;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<EntityCrudKitSettings>> handler) {
        return null;
    }
}