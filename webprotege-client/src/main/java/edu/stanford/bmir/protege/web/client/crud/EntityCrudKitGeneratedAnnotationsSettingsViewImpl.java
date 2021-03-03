package edu.stanford.bmir.protege.web.client.crud;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

public class EntityCrudKitGeneratedAnnotationsSettingsViewImpl extends Composite implements EntityCrudKitGeneratedAnnotationsSettingsView {

    interface CrudKitGeneratedAnnotationsSettingsViewImplUiBinder extends UiBinder<HTMLPanel, EntityCrudKitGeneratedAnnotationsSettingsViewImpl> {

    }

    private static CrudKitGeneratedAnnotationsSettingsViewImplUiBinder ourUiBinder = GWT.create(
            CrudKitGeneratedAnnotationsSettingsViewImplUiBinder.class);

    @UiField(provided = true)
    ValueListFlexEditorImpl<GeneratedAnnotationDescriptor> list;

    @Inject
    public EntityCrudKitGeneratedAnnotationsSettingsViewImpl(Provider<GeneratedAnnotationDescriptorValueEditor> editorProvider) {
        list = new ValueListFlexEditorImpl<>(() -> {
            GeneratedAnnotationDescriptorValueEditor editor = editorProvider.get();
            editor.start();
            return editor;
        });
        list.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setValues(@Nonnull ImmutableList<GeneratedAnnotationDescriptor> values) {
        list.setValue(values);
    }

    @Nonnull
    @Override
    public ImmutableList<GeneratedAnnotationDescriptor> getValues() {
        return ImmutableList.copyOf(list.getValue().orElse(ImmutableList.of()));
    }
}