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
        EntityCrudKitDescriptorManager descriptorManager = GWT.create(EntityCrudKitDescriptorManager.class);
        descriptors = descriptorManager.getDescriptors();
        for (EntityCrudKit descriptor : descriptors) {
            schemeSelectorListBox.addItem(descriptor.getDisplayName());
            touchedEditors.add(Optional.<EntityCrudKitSuffixSettingsEditor>absent());
        }
        schemeSelectorListBox.setSelectedIndex(0);
        updateEditor();
    }

    @UiHandler("schemeSelectorListBox")
    protected void handleSchemeChanged(ChangeEvent valueChangeEvent) {
        updateEditor();
    }

    @UiHandler("schemeSelectorListBox")
    protected void handleKeyPressChange(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            updateEditor();
        }
    }

    @UiHandler("iriPrefixEditor")
    protected void handleIRIPrefixChanged(ValueChangeEvent<String> prefixChangedEvent) {
//        updateExampleField();
    }


    @SuppressWarnings("unchecked")
    private Optional<EntityCrudKitSuffixSettingsEditor> updateEditor() {
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
        }
        schemeSpecificSettingsEditorHolder.setWidget(editor);
        iriPrefixEditor.setValue(descriptor.getDefaultPrefixSettings().getIRIPrefix());
        return Optional.of(editor);
    }


//    private void updateEditorForSelectedFactory() {
//        SuffixSettingsEditorKit selectedEditorKit = getSelectedFactory();
//        if(lastEditorKit != null) {
//            if(lastEditorKit.getSchemeId().equals(selectedEditorKit.getSchemeId())) {
//                return;
//            }
//        }
//        setFactory(selectedEditorKit, Optional.<SuffixSettings>absent());
//    }
//
//    @SuppressWarnings("unchecked")
//    private <F extends SuffixSettingsEditorKit<S>, S extends SuffixSettings> F getSelectedFactory() {
//        final int selectedIndex = schemeSelectorListBox.getSelectedIndex();
//        final SuffixSettingsEditorKit selEditorKit;
//        if (selectedIndex != -1) {
//            selEditorKit = editorKits.get(selectedIndex);
//        }
//        else {
//            selEditorKit = editorKits.get(0);
//        }
//        return (F) selEditorKit;
//    }
//
//    private <F extends SuffixSettingsEditorKit<S>, S extends SuffixSettings> void setFactory(final F selFactory, Optional<S> settings) {
//        lastEditorKit = selFactory;
//        final int index = getFactoryIndex(selFactory);
//        if (index != -1) {
//            if (!schemeSelectorListBox.isItemSelected(index)) {
//                schemeSelectorListBox.setSelectedIndex(index);
//            }
//            Optional<EntityCrudKitSuffixSettingsEditor<S>> touchedEditor = getTouchedEditor(index);
//            if(touchedEditor.isPresent() && !settings.isPresent()) {
//                // Restore
//                EntityCrudKitSuffixSettingsEditor<S> editor = touchedEditor.get();
//                setCurrentEditor(editor);
//            }
//            else {
//                setupEditorWithSettings(selFactory, settings);
//            }
//        }
//    }
//
//    private <F extends SuffixSettingsEditorKit<S>, S extends SuffixSettings> int getFactoryIndex(F selFactory) {
//        for(int index = 0; index < editorKits.size(); index++) {
//            SuffixSettingsEditorKit<?> editorKit = editorKits.get(index);
//            if(editorKit.getSchemeId().equals(selFactory.getSchemeId())) {
//                return index;
//            }
//        }
//        return -1;
//    }
//
//    private <F extends SuffixSettingsEditorKit<S>, S extends SuffixSettings> void setupEditorWithSettings(final F selFactory, final Optional<S> settings) {
//        if(settings.isPresent()) {
//            EntityCrudKitSuffixSettingsEditor<S> editor = selFactory.getSuffixSettingsEditor();
//            editor.setSettings(settings.get());
//            touchedEditors.set(getFactoryIndex(selFactory), Optional.<EntityCrudKitSuffixSettingsEditor>of(editor));
//            setCurrentEditor(editor);
//        }
//        else {
//            selFactory.createDefaultSettings(new AsyncCallback<S>() {
//                @Override
//                public void onFailure(Throwable caught) {
//                }
//
//                @Override
//                public void onSuccess(S result) {
//                    EntityCrudKitSuffixSettingsEditor<S> editor = selFactory.getSuffixSettingsEditor();
//                    editor.setSettings(result);
//                    touchedEditors.set(getFactoryIndex(selFactory), Optional.<EntityCrudKitSuffixSettingsEditor>of(editor));
//                    setCurrentEditor(editor);
//                }
//            });
//        }
//
//    }
//
//    private <S extends SuffixSettings> void setCurrentEditor(EntityCrudKitSuffixSettingsEditor<S> editor) {
//        schemeSpecificSettingsEditorHolder.setWidget(editor);
//        updateExampleField();
//    }
//
//    private void updateExampleField() {
//        EntityCrudKitSuffixSettingsEditor<?> editor = getCurrentEditor();
////        exampleField.setText(editor.getSuffixSettings().generateExample(getIRIPrefix()));
//    }
//
//    private EntityCrudKitSuffixSettingsEditor<?> getCurrentEditor() {
//        return (EntityCrudKitSuffixSettingsEditor<?>) schemeSpecificSettingsEditorHolder.getWidget();
//    }
//
//    @SuppressWarnings("unchecked")
//    public <S extends SuffixSettings> Optional<EntityCrudKitSuffixSettingsEditor<S>> getTouchedEditor(int index) {
//        Optional<EntityCrudKitSuffixSettingsEditor> touchedEditor = touchedEditors.get(index);
//        if(touchedEditor.isPresent()) {
//            return Optional.of((EntityCrudKitSuffixSettingsEditor<S>) touchedEditor.get());
//        }
//        else {
//            return Optional.absent();
//        }
//    }
//
//    public void setSettings(EntityCrudKitSuffixSettings suffixSettings) {
//        iriPrefixEditor.setText(suffixSettings.getIRIPrefix());
////        final SuffixSettings schemeSpecificSettings = settings.getSchemeSpecificSettings();
////        SuffixSettingsId schemeId = schemeSpecificSettings.getId();
////        SuffixSettingsEditorKit<SuffixSettings> editorKit = SuffixSettingsEditorKitManager.get().getFactory(schemeId);
////        setFactory(editorKit, Optional.<SuffixSettings>of(schemeSpecificSettings));
//
//    }
//
//    public IRIGeneratorSettings getSettings() {
//        EntityCrudKitSuffixSettingsEditor<?> editor = getSuffixSettingsEditor();
//        SuffixSettings schemeSpecificSettings = editor.getSettings();
//        return new IRIGeneratorSettings(getIRIPrefix(), schemeSpecificSettings);
//    }
//
//    @SuppressWarnings("unchecked")
//    private <S extends SuffixSettings> EntityCrudKitSuffixSettingsEditor<S> getSuffixSettingsEditor() {
//        return (EntityCrudKitSuffixSettingsEditor<S>) schemeSpecificSettingsEditorHolder.getWidget();
//    }

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
        Optional<EntityCrudKitSuffixSettingsEditor> editor = updateEditor();
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
        Optional<EntityCrudKitSuffixSettingsEditor> selEditor = updateEditor();
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