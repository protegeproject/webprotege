package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.GetFormDescriptorAction;
import edu.stanford.bmir.protege.web.shared.form.GetFormDescriptorResult;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private final FormView formView;

    private final ProjectId projectId;

    private final Provider<PrimitiveDataEditor> primitiveDataEditorProvider;

    @Inject
    public FormPresenter(DispatchServiceManager dispatchServiceManager, FormView formView, ProjectId projectId,
                         Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.formView = formView;
        this.projectId = projectId;
        this.primitiveDataEditorProvider = primitiveDataEditorProvider;
    }

    public void start(AcceptsOneWidget container) {
        ScrollPanel sp = new ScrollPanel(formView.asWidget());
        container.setWidget(sp);
    }

    public void setSubject(final OWLEntity entity) {
        GWT.log("[FormPresenter] Displaying form data");

        dispatchServiceManager.execute(new GetFormDescriptorAction(projectId, entity), new DispatchServiceCallback<GetFormDescriptorResult>() {
            @Override
            public void handleSuccess(GetFormDescriptorResult result) {
                displayForm(result.getFormDescriptor(), result.getFormData(), entity);
            }

            @Override
            public void handleErrorFinally(Throwable throwable) {

            }
        });
    }


    private void displayForm(FormDescriptor formDescriptor, FormData formData, final OWLEntity subject) {
        for (FormElementView elementView : formView.getElementViews()) {
            FormElementId elementId = elementView.getId().get();
            Optional<FormDataList> elementData = elementView.getEditor().getValue();
            GWT.log("FormData: " + elementId + " = " + elementData.toString());
        }
        formView.clear();
        for (FormElementDescriptor elementDescriptor : formDescriptor.getFormElementDescriptors()) {
            Optional<FormElementEditor> elementEditor = getFormElementEditor(elementDescriptor);
            if (elementEditor.isPresent()) {
                FormElementView elementView = new FormElementViewImpl();
                elementView.setId(elementDescriptor.getId());
                elementView.setFormLabel(elementDescriptor.getLabel());
                elementView.setEditor(elementEditor.get());
                Optional<FormDataList> data = formData.getFormElementData(elementDescriptor.getId());
                if (data.isPresent()) {
                    elementEditor.get().setValue(data.get());
                }
                else {
                    elementEditor.get().clearValue();
                }

                formView.addFormElementView(elementView);
            }
        }
    }

    private Optional<FormElementEditor> getFormElementEditor(FormElementDescriptor descriptor) {
        Optional<ValueEditorFactory<FormDataValue>> editorFactory = getValueEditorFactory(descriptor.getFieldDescriptor());
        if (!editorFactory.isPresent()) {
            return Optional.<FormElementEditor>absent();
        }
        return Optional.of(
                new FormElementEditorImpl(
                        editorFactory.get(),
                        descriptor.getRepeatability())
        );
    }

    private Optional<ValueEditorFactory<FormDataValue>> getValueEditorFactory(final FormFieldDescriptor formFieldDescriptor) {
        if (formFieldDescriptor.getAssociatedFieldTypeId().equals(StringFieldDescriptor.getFieldTypeId())) {
            return Optional.of(
                    () -> {
                        StringFieldDescriptor descriptor = (StringFieldDescriptor) formFieldDescriptor;
                        StringFieldEditor stringFieldEditor = new StringFieldEditor(primitiveDataEditorProvider);
                        stringFieldEditor.setPlaceholder(descriptor.getPlaceholder());
                        stringFieldEditor.setLineMode(descriptor.getLineMode());
                        stringFieldEditor.setStringType(descriptor.getStringType());
                        if (!descriptor.getPattern().isEmpty()) {
                            stringFieldEditor.setPattern(descriptor.getPattern());
                            stringFieldEditor.setPatternViolationErrorMessage(descriptor.getPatternViolationErrorMessage());
                        }
                        return stringFieldEditor;
                    }
            );
        }
        else if (formFieldDescriptor.getAssociatedFieldTypeId().equals(ChoiceFieldDescriptor.getFieldTypeId())) {
            return Optional.of(
                    () -> {
                        ChoiceFieldDescriptor descriptor = (ChoiceFieldDescriptor) formFieldDescriptor;
                        ChoiceFieldEditor editor;
                        if (descriptor.getType() == ChoiceFieldType.RADIO_BUTTON) {
                            editor = new ChoiceFieldRadioButtonEditor();
                        }
                        else if (descriptor.getType() == ChoiceFieldType.CHECK_BOX) {
                            editor = new ChoiceFieldCheckBoxEditor();
                        }
                        else {
                            editor = new ChoiceFieldComboBoxEditor();
                        }
                        editor.setChoices(descriptor.getChoiceDescriptors());
                        return editor;
                    }
            );
        }
        else if (formFieldDescriptor.getAssociatedFieldTypeId().equals(ClassNameFieldDescriptor.getFieldTypeId())) {
            return Optional.of(
                    () -> new ClassNameFieldEditor(projectId, primitiveDataEditorProvider)
            );
        }
        else if(formFieldDescriptor.getAssociatedFieldTypeId().equals(ImageFieldDescriptor.getFieldTypeId())) {
            return Optional.of(
                    () -> new ImageFieldEditor()
            );
        }
        else if(formFieldDescriptor.getAssociatedFieldTypeId().equals(CompositeFieldDescriptor.getFieldTypeId())) {
            CompositeFieldDescriptor descriptor = (CompositeFieldDescriptor) formFieldDescriptor;
            List<ValueEditorFactory<FormDataValue>> childEditorFactories = new ArrayList<>();
            List<CompositeFieldDescriptorEntry> childDescriptorEntries = new ArrayList<>();
            for(CompositeFieldDescriptorEntry childDescriptor : descriptor.getChildDescriptors()) {
                Optional<ValueEditorFactory<FormDataValue>> childEditorFactory = getValueEditorFactory(childDescriptor.getDescriptor());
                if(!childEditorFactory.isPresent()) {
                    return Optional.absent();
                }
                childDescriptorEntries.add(childDescriptor);
                childEditorFactories.add(childEditorFactory.get());
            }
            return Optional.of(
                    () -> {
                        CompositeFieldEditor editor = new CompositeFieldEditor();
                        for(int i = 0; i < childDescriptorEntries.size(); i++) {
                            FormElementId childId = childDescriptorEntries.get(i).getElementId();
                            ValueEditorFactory<FormDataValue> childFactory = childEditorFactories.get(i);
                            ValueEditor<FormDataValue> childEditor = childFactory.createEditor();
                            double flexGrow = childDescriptorEntries.get(i).getFlexGrow();
                            double flexShrink = childDescriptorEntries.get(i).getFlexShrink();
                            editor.addChildEditor(childId, flexGrow, flexShrink, childEditor);
                        }
                        return editor;
                    }
            );
        }
        else {
            return Optional.<ValueEditorFactory<FormDataValue>>absent();
        }
    }

    public void clear() {

    }

    public IsWidget getView() {
        return formView;
    }
}
