package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

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

    @Nonnull
    private Optional<OWLEntity> currentSubject = Optional.empty();

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
        checkNotNull(entity);
        currentSubject.ifPresent(subject -> {
            Map<FormElementId, FormDataValue> map = new HashMap<>();
            GWT.log("[FormPresenter] Getting element views and data");
            for(FormElementView view : formView.getElementViews()) {
//                try {
                    Optional<FormElementId> elementId = view.getId();
                    Optional<FormDataValue> value = view.getEditor().getValue();
                    if(elementId.isPresent() && value.isPresent()) {
                        map.put(elementId.get(), value.get());
                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
            GWT.log("[FormPresenter] Sending form data to server");
            FormData data = new FormData(map);
            dispatchServiceManager.execute(new SetFormDataAction(projectId,
                                                                 new FormId("MyForm"),
                                                                 subject,
                                                                 data),
                                           result -> {});

        });
        currentSubject = Optional.of(entity);

        dispatchServiceManager.execute(new GetFormDescriptorAction(projectId, new FormId("MyForm"), entity),
                                       result -> displayForm(result.getFormDescriptor(), result.getFormData(), entity));
    }

    private Optional<FormDescriptor> currentFormDescriptor = Optional.empty();

    private void displayForm(FormDescriptor formDescriptor, FormData formData, final OWLEntity subject) {
        if (!currentFormDescriptor.equals(Optional.of(formDescriptor))) {
            GWT.log("[FormPresenter] Rebuilding form");
            formView.clear();
            for (FormElementDescriptor elementDescriptor : formDescriptor.getElements()) {
                Optional<FormElementEditor> elementEditor = getFormElementEditor(elementDescriptor);
                if (elementEditor.isPresent()) {
                    try {
                        GWT.log("[FormPresenter] Creating form element for " + elementDescriptor.getId());
                        FormElementView elementView = new FormElementViewImpl();
                        elementView.setId(elementDescriptor.getId());
                        elementView.setFormLabel(elementDescriptor.getLabel());
                        FormElementEditor editor = elementEditor.get();
                        GWT.log("[FormPresenter] Adding value changed handler for " + editor.getClass().getName());
                        editor.addValueChangeHandler(event -> {
                            GWT.log("[FormPresenter] Value changed in " + elementDescriptor.getId());
                            if(elementDescriptor.getRequired() == Required.REQUIRED) {
                                Optional<FormDataValue> val = editor.getValue();
                                if(val.isPresent()) {
                                    elementView.setRequiredValueNotPresentVisible(false);
                                }
                                else {
                                    elementView.setRequiredValueNotPresentVisible(true);
                                }
                            }
                        });
                        elementView.setEditor(editor);
                        elementView.setRequired(elementDescriptor.getRequired());
                        Optional<FormDataValue> data = formData.getFormElementData(elementDescriptor.getId());
                        if (data.isPresent()) {
                            editor.setValue(data.get());
                        }
                        else {
                            editor.clearValue();
                        }
                        if(elementDescriptor.getRequired() == Required.REQUIRED) {
                            if(editor.getValue().isPresent()) {
                                elementView.setRequiredValueNotPresentVisible(false);
                            }
                            else {
                                elementView.setRequiredValueNotPresentVisible(true);
                            }
                        }
                        formView.addFormElementView(elementView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else {
            formView.getElementViews().forEach(v -> {
                Optional<FormElementId> theId = v.getId();
                if(theId.isPresent()) {
                    FormElementId id = theId.get();
                    Optional<FormDataValue> formElementData = formData.getFormElementData(id);
                    if(formElementData.isPresent()) {
                        v.getEditor().setValue(formElementData.get());
                    }
                    else {
                        v.getEditor().clearValue();
                    }
                    if(v.getRequired() == Required.REQUIRED) {
                        if(formElementData.isPresent()) {
                            v.setRequiredValueNotPresentVisible(false);
                        }
                        else {
                            v.setRequiredValueNotPresentVisible(true);
                        }
                    }
                }
                else {
                    v.getEditor().clearValue();
                }

            });

        }
        currentFormDescriptor = Optional.of(formDescriptor);

    }

    private Optional<FormElementEditor> getFormElementEditor(FormElementDescriptor descriptor) {
        Optional<ValueEditorFactory<FormDataValue>> editorFactory = getValueEditorFactory(descriptor.getFieldDescriptor());
        if (!editorFactory.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(
                new FormElementEditorImpl(
                        editorFactory.get(),
                        descriptor.getRepeatability())
        );
    }

    private Optional<ValueEditorFactory<FormDataValue>> getValueEditorFactory(final FormFieldDescriptor formFieldDescriptor) {
        if (formFieldDescriptor.getAssociatedType().equals(TextFieldDescriptor.getType())) {
            return Optional.of(
                    () -> {
                        TextFieldDescriptor descriptor = (TextFieldDescriptor) formFieldDescriptor;
                        TextFieldEditor textFieldEditor = new TextFieldEditor(primitiveDataEditorProvider);
                        textFieldEditor.setPlaceholder(descriptor.getPlaceholder());
                        textFieldEditor.setLineMode(descriptor.getLineMode());
                        textFieldEditor.setStringType(descriptor.getStringType());
                        if (!descriptor.getPattern().isEmpty()) {
                            textFieldEditor.setPattern(descriptor.getPattern());
                            textFieldEditor.setPatternViolationErrorMessage(descriptor.getPatternViolationErrorMessage());
                        }
                        return textFieldEditor;
                    }
            );
        }
        else if (formFieldDescriptor.getAssociatedType().equals(ChoiceFieldDescriptor.getType())) {
            return Optional.of(
                    () -> {
                        ChoiceFieldDescriptor descriptor = (ChoiceFieldDescriptor) formFieldDescriptor;
                        ChoiceFieldEditor editor;
                        if (descriptor.getWidgetType() == ChoiceFieldType.RADIO_BUTTON) {
                            editor = new ChoiceFieldRadioButtonEditor();
                        }
                        else if (descriptor.getWidgetType() == ChoiceFieldType.CHECK_BOX) {
                            editor = new ChoiceFieldCheckBoxEditor();
                        }
                        else if(descriptor.getWidgetType() == ChoiceFieldType.SEGMENTED_BUTTON) {
                            editor = new ChoiceFieldSegmentedEditor();
                        }
                        else {
                            editor = new ChoiceFieldComboBoxEditor();
                        }
                        editor.setChoices(descriptor.getChoices());
                        editor.setDefaultChoices(descriptor.getDefaultChoices());
                        return editor;
                    }
            );
        }
        else if (formFieldDescriptor.getAssociatedType().equals(ClassNameFieldDescriptor.getFieldTypeId())) {
            return Optional.of(
                    () -> {
                        ClassNameFieldEditor editor = new ClassNameFieldEditor(projectId,
                                                                                             dispatchServiceManager,
                                                                                             primitiveDataEditorProvider);
                        ClassNameFieldDescriptor classNameFieldDescriptor = (ClassNameFieldDescriptor) formFieldDescriptor;
                        editor.setPlaceholder(classNameFieldDescriptor.getPlaceholder());
                        return editor;
                    }
            );
        }
        else if(formFieldDescriptor.getAssociatedType().equals(ImageFieldDescriptor.getFieldTypeId())) {
            return Optional.of(
                    () -> new ImageFieldEditor()
            );
        }
        else if(formFieldDescriptor.getAssociatedType().equals(NumberFieldDescriptor.getTypeId())) {
            return Optional.of(
                    () -> {
                        NumberFieldDescriptor descriptor = (NumberFieldDescriptor) formFieldDescriptor;
                        NumberFieldEditor editor = new NumberFieldEditor();
                        editor.setFormat(descriptor.getNumberFormat());
                        editor.setRange(descriptor.getRange());
                        editor.setLength(descriptor.getLength());
                        editor.setPlaceholder(descriptor.getPlaceholder());
                        return editor;
                    }
            );
        }
        else if(formFieldDescriptor.getAssociatedType().equals(CompositeFieldDescriptor.getFieldTypeId())) {
            CompositeFieldDescriptor descriptor = (CompositeFieldDescriptor) formFieldDescriptor;
            List<ValueEditorFactory<FormDataValue>> childEditorFactories = new ArrayList<>();
            List<CompositeFieldDescriptorEntry> childDescriptorEntries = new ArrayList<>();
            for(CompositeFieldDescriptorEntry childDescriptor : descriptor.getChildDescriptors()) {
                Optional<ValueEditorFactory<FormDataValue>> childEditorFactory = getValueEditorFactory(childDescriptor.getDescriptor());
                if(!childEditorFactory.isPresent()) {
                    return Optional.empty();
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
            return Optional.empty();
        }
    }

    public void clear() {

    }

    public IsWidget getView() {
        return formView;
    }
}
