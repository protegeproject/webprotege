package edu.stanford.bmir.protege.web.client.irigen;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.irigen.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class IRIGeneratorSettingsEditorImpl extends Composite implements IRIGeneratorSettingsEditor, HasInitialFocusable {

    private final List<SuffixSettingsEditorKit> editorKits;

    interface IRIGeneratorSettingsEditorImplUiBinder extends UiBinder<HTMLPanel, IRIGeneratorSettingsEditorImpl> {

    }

    private static IRIGeneratorSettingsEditorImplUiBinder ourUiBinder = GWT.create(IRIGeneratorSettingsEditorImplUiBinder.class);

    @UiField
    protected TextBox iriPrefixEditor;

    @UiField
    protected ListBox schemeSelectorListBox;

    @UiField
    protected SimplePanel schemeSpecificSettingsEditorHolder;

    @UiField
    protected HasText exampleField;


    private SuffixSettingsEditorKit<?> lastEditorKit;

    private List<Optional<SuffixSettingsEditor>> touchedEditors = new ArrayList<Optional<SuffixSettingsEditor>>();

    public IRIGeneratorSettingsEditorImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        editorKits = SuffixSettingsEditorKitManager.get().getFactories();
        for(SuffixSettingsEditorKit editorKit : editorKits) {
            SuffixSettingsId schemeId = editorKit.getSchemeId();
            schemeSelectorListBox.addItem(schemeId.getDisplayName());
            touchedEditors.add(Optional.<SuffixSettingsEditor>absent());
        }
        iriPrefixEditor.setText("http://webprotege.stanford.edu/entities/");
        setFactory(editorKits.get(0), Optional.<SuffixSettings>absent());
    }

    @UiHandler("schemeSelectorListBox")
    protected void handleSchemeChanged(ChangeEvent valueChangeEvent) {
        updateEditorForSelectedFactory();
    }


    @UiHandler("schemeSelectorListBox")
    protected void handleKeyPressChange(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            updateEditorForSelectedFactory();
        }
    }

    @UiHandler("iriPrefixEditor")
    protected void handleIRIPrefixChanged(ValueChangeEvent<String> prefixChangedEvent) {
        updateExampleField();
    }



    private void updateEditorForSelectedFactory() {
        SuffixSettingsEditorKit selectedEditorKit = getSelectedFactory();
        if(lastEditorKit != null) {
            if(lastEditorKit.getSchemeId().equals(selectedEditorKit.getSchemeId())) {
                return;
            }
        }
        setFactory(selectedEditorKit, Optional.<SuffixSettings>absent());
    }

    @SuppressWarnings("unchecked")
    private <F extends SuffixSettingsEditorKit<S>, S extends SuffixSettings> F getSelectedFactory() {
        final int selectedIndex = schemeSelectorListBox.getSelectedIndex();
        final SuffixSettingsEditorKit selEditorKit;
        if (selectedIndex != -1) {
            selEditorKit = editorKits.get(selectedIndex);
        }
        else {
            selEditorKit = editorKits.get(0);
        }
        return (F) selEditorKit;
    }

    private <F extends SuffixSettingsEditorKit<S>, S extends SuffixSettings> void setFactory(final F selFactory, Optional<S> settings) {
        lastEditorKit = selFactory;
        final int index = getFactoryIndex(selFactory);
        if (index != -1) {
            if (!schemeSelectorListBox.isItemSelected(index)) {
                schemeSelectorListBox.setSelectedIndex(index);
            }
            Optional<SuffixSettingsEditor<S>> touchedEditor = getTouchedEditor(index);
            if(touchedEditor.isPresent() && !settings.isPresent()) {
                // Restore
                SuffixSettingsEditor<S> editor = touchedEditor.get();
                setCurrentEditor(editor);
            }
            else {
                setupEditorWithSettings(selFactory, settings);
            }
        }
    }

    private <F extends SuffixSettingsEditorKit<S>, S extends SuffixSettings> int getFactoryIndex(F selFactory) {
        for(int index = 0; index < editorKits.size(); index++) {
            SuffixSettingsEditorKit<?> editorKit = editorKits.get(index);
            if(editorKit.getSchemeId().equals(selFactory.getSchemeId())) {
                return index;
            }
        }
        return -1;
    }

    private <F extends SuffixSettingsEditorKit<S>, S extends SuffixSettings> void setupEditorWithSettings(final F selFactory, final Optional<S> settings) {
        if(settings.isPresent()) {
            SuffixSettingsEditor<S> editor = selFactory.getEditor();
            editor.setSettings(settings.get());
            touchedEditors.set(getFactoryIndex(selFactory), Optional.<SuffixSettingsEditor>of(editor));
            setCurrentEditor(editor);
        }
        else {
            selFactory.createDefaultSettings(new AsyncCallback<S>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(S result) {
                    SuffixSettingsEditor<S> editor = selFactory.getEditor();
                    editor.setSettings(result);
                    touchedEditors.set(getFactoryIndex(selFactory), Optional.<SuffixSettingsEditor>of(editor));
                    setCurrentEditor(editor);
                }
            });
        }

    }

    private <S extends SuffixSettings> void setCurrentEditor(SuffixSettingsEditor<S> editor) {
        schemeSpecificSettingsEditorHolder.setWidget(editor);
        updateExampleField();
    }

    private void updateExampleField() {
        SuffixSettingsEditor<?> editor = getCurrentEditor();
//        exampleField.setText(editor.getSettings().generateExample(getIRIPrefix()));
    }

    private SuffixSettingsEditor<?> getCurrentEditor() {
        return (SuffixSettingsEditor<?>) schemeSpecificSettingsEditorHolder.getWidget();
    }

    @SuppressWarnings("unchecked")
    public <S extends SuffixSettings> Optional<SuffixSettingsEditor<S>> getTouchedEditor(int index) {
        Optional<SuffixSettingsEditor> touchedEditor = touchedEditors.get(index);
        if(touchedEditor.isPresent()) {
            return Optional.of((SuffixSettingsEditor<S>) touchedEditor.get());
        }
        else {
            return Optional.absent();
        }
    }

    public void setSettings(IRIGeneratorSettings settings) {
        iriPrefixEditor.setText(settings.getIRIPrefix());
        final SuffixSettings schemeSpecificSettings = settings.getSchemeSpecificSettings();
        SuffixSettingsId schemeId = schemeSpecificSettings.getId();
        SuffixSettingsEditorKit<SuffixSettings> editorKit = SuffixSettingsEditorKitManager.get().getFactory(schemeId);
        setFactory(editorKit, Optional.<SuffixSettings>of(schemeSpecificSettings));

    }

    public IRIGeneratorSettings getSettings() {
        SuffixSettingsEditor<?> editor = getEditor();
        SuffixSettings schemeSpecificSettings = editor.getSettings();
        return new IRIGeneratorSettings(getIRIPrefix(), schemeSpecificSettings);
    }

    @SuppressWarnings("unchecked")
    private <S extends SuffixSettings> SuffixSettingsEditor<S> getEditor() {
        return (SuffixSettingsEditor<S>) schemeSpecificSettingsEditorHolder.getWidget();
    }

    private String getIRIPrefix() {
        return iriPrefixEditor.getText().trim();
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }

    @Override
    public void setValue(IRIGeneratorSettings object) {
        setSettings(object);
    }

    @Override
    public void clearValue() {

    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return Optional.<Focusable>of(iriPrefixEditor);
    }

    @Override
    public Optional<IRIGeneratorSettings> getValue() {
        return Optional.of(getSettings());
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<IRIGeneratorSettings>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}