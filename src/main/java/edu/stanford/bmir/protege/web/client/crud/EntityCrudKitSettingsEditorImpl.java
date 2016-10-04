package edu.stanford.bmir.protege.web.client.crud;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import javax.inject.Inject;
import edu.stanford.bmir.protege.web.client.crud.obo.OBOIdSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.client.crud.supplied.SuppliedSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.client.crud.uuid.UUIDSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.client.inject.WebProtegeClientInjector;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixKit;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class EntityCrudKitSettingsEditorImpl extends Composite implements EntityCrudKitSettingsEditor, HasInitialFocusable {

    private final List<EntityCrudKit> descriptors;

    interface EntityCrudKitSettingsEditorImplUiBinder extends UiBinder<HTMLPanel, EntityCrudKitSettingsEditorImpl> {

    }

    private static EntityCrudKitSettingsEditorImplUiBinder ourUiBinder = GWT.create(EntityCrudKitSettingsEditorImplUiBinder.class);

    @UiField
    protected TextBox iriPrefixEditor;

    @UiField
    protected HTML prefixValidatorMessage;

    @UiField
    protected ListBox suffixSelectorListBox;

    @UiField
    protected SimplePanel schemeSpecificSettingsEditorHolder;

    private boolean prefixIsDirty = false;

    private List<Optional<EntityCrudKitSuffixSettingsEditor>> touchedEditors = new ArrayList<Optional<EntityCrudKitSuffixSettingsEditor>>();

    private final Provider<OBOIdSuffixSettingsEditor> oboIdSuffixSettingsEditorProvider;

    @Inject
    public EntityCrudKitSettingsEditorImpl(Provider<OBOIdSuffixSettingsEditor> oboIdSuffixSettingsEditorProvider) {
        this.oboIdSuffixSettingsEditorProvider = oboIdSuffixSettingsEditorProvider;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        EntityCrudKitManager kitManager = EntityCrudKitManager.get();
        descriptors = new ArrayList<EntityCrudKit>(kitManager.getKits());
        for (EntityCrudKit descriptor : descriptors) {
            suffixSelectorListBox.addItem(descriptor.getDisplayName());
            touchedEditors.add(Optional.<EntityCrudKitSuffixSettingsEditor>absent());
        }
        suffixSelectorListBox.setSelectedIndex(0);
        updateEditor(true);
    }

    @UiHandler("suffixSelectorListBox")
    protected void handleSchemeChanged(ChangeEvent valueChangeEvent) {
        updateEditor(true);
    }

    @UiHandler("suffixSelectorListBox")
    protected void handleKeyPressChange(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            updateEditor(true);
        }
    }

    @UiHandler("iriPrefixEditor")
    protected void handleIRIPrefixChanged(ValueChangeEvent<String> prefixChangedEvent) {
        prefixIsDirty = true;
        validatePrefix();
    }

    @UiHandler("iriPrefixEditor")
    protected void handleIRIPrefixChanged(KeyUpEvent event) {
        validatePrefix();
    }

    private void validatePrefix() {
        int selectedIndex = suffixSelectorListBox.getSelectedIndex();
        if(selectedIndex == -1) {
            return;
        }
        String prefix = iriPrefixEditor.getText().trim();
        EntityCrudKit<?> crudKit = descriptors.get(selectedIndex);
        Optional<String> validationMessage = crudKit.getPrefixValidationMessage(prefix);
        if(validationMessage.isPresent()) {
            prefixValidatorMessage.setHTML(new SafeHtmlBuilder().appendHtmlConstant(validationMessage.get()).toSafeHtml());
            prefixValidatorMessage.setVisible(true);
        }
        else {
            prefixValidatorMessage.setVisible(false);
        }
    }

    @SuppressWarnings("unchecked")
    private Optional<EntityCrudKitSuffixSettingsEditor> updateEditor(boolean forceRefresh) {
        int selIndex = suffixSelectorListBox.getSelectedIndex();
        if (selIndex == -1) {
            return Optional.absent();
        }
        Optional<EntityCrudKitSuffixSettingsEditor> touchedEditor = touchedEditors.get(selIndex);
        EntityCrudKitSuffixSettingsEditor editor;
        final EntityCrudKit descriptor = descriptors.get(selIndex);
        if (touchedEditor.isPresent()) {
            editor = touchedEditor.get();
            if(!prefixIsDirty && forceRefresh) {
                iriPrefixEditor.setValue(descriptor.getDefaultPrefixSettings().getIRIPrefix());
            }
        }
        else {
            editor = getSuffixSettingsEditor(descriptor);
            touchedEditors.set(selIndex, Optional.of(editor));
            editor.setValue(descriptor.getDefaultSuffixSettings());
            if (!prefixIsDirty && forceRefresh) {
                iriPrefixEditor.setValue(descriptor.getDefaultPrefixSettings().getIRIPrefix());
            }
        }
        schemeSpecificSettingsEditorHolder.setWidget(editor);
        validatePrefix();
        return Optional.of(editor);
    }

    private EntityCrudKitSuffixSettingsEditor getSuffixSettingsEditor(EntityCrudKit<?> crudKit) {
        if(crudKit.getKitId().equals(OBOIdSuffixKit.get().getKitId())) {
            return oboIdSuffixSettingsEditorProvider.get();
        }
        else if(crudKit.getKitId().equals(UUIDSuffixKit.get().getKitId())) {
            return new UUIDSuffixSettingsEditor();
        }
        else if(crudKit.getKitId().equals(SuppliedNameSuffixKit.get().getKitId())) {
            return new SuppliedSuffixSettingsEditor();
        }
        else {
            throw new RuntimeException("Unknown Entity Crud Kit");
        }
    }

    private String getIRIPrefix() {
        String trimmedPrefix = iriPrefixEditor.getText().trim();
        return URL.encode(trimmedPrefix);
    }

    @Override
    public boolean isWellFormed() {
        return false;
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return Optional.<Focusable>of(iriPrefixEditor);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(EntityCrudKitSettings object) {
        int index = getDescriptorIndex(object.getSuffixSettings().getKitId());
        if (index == -1) {
            return;
        }
        suffixSelectorListBox.setSelectedIndex(index);
        Optional<EntityCrudKitSuffixSettingsEditor> editor = updateEditor(true);
        String decodedPrefix = URL.decode(object.getPrefixSettings().getIRIPrefix());
        iriPrefixEditor.setText(decodedPrefix);
        if (editor.isPresent()) {
            editor.get().setValue(object.getSuffixSettings());
        }
        prefixIsDirty = false;
        validatePrefix();
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
        prefixIsDirty = false;
    }

    @Override
    public Optional<EntityCrudKitSettings<?>> getValue() {
        Optional<EntityCrudKitSuffixSettingsEditor> selEditor = updateEditor(false);
        if(!selEditor.isPresent()) {
            return Optional.absent();
        }
        EntityCrudKitSuffixSettingsEditor editor = selEditor.get();
        Optional<?> editedValue = editor.getValue();
        if(!editedValue.isPresent()) {
            return Optional.absent();
        }
        return Optional.<EntityCrudKitSettings<?>>of(new EntityCrudKitSettings(new EntityCrudKitPrefixSettings(getIRIPrefix()), (EntityCrudKitSuffixSettings) editedValue.get()));
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<EntityCrudKitSettings<?>>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}