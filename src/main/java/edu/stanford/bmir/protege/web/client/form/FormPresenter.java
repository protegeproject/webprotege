package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private final FormView formView;

    private final ProjectId projectId;

    @Inject
    public FormPresenter(DispatchServiceManager dispatchServiceManager, FormView formView, ProjectId projectId) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.formView = formView;
        this.projectId = projectId;
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(formView);
    }

    public void setSubject(OWLEntity entity) {
        GWT.log("[FormPresenter] Displaying form data");
        displayForm(new FormData(
                Arrays.asList(
                        new FormElementDescriptor(new FormElementId("TheLabel"), "Label", Repeatability.UNREPEATABLE, new StringFieldDescriptor("Enter label", StringType.SIMPLE_STRING, LineMode.SINGLE_LINE, "")),
                        new FormElementDescriptor(new FormElementId("TheComment"), "Comment", Repeatability.UNREPEATABLE, new StringFieldDescriptor("Enter comment", StringType.LANG_STRING, LineMode.MULTI_LINE, "")),
                        new FormElementDescriptor(new FormElementId("Synonyms"), "Synonyms", Repeatability.REPEATABLE, new StringFieldDescriptor("Enter synonym", StringType.LANG_STRING, LineMode.MULTI_LINE, "")),
                        new FormElementDescriptor(new FormElementId("EngineConfiguration"), "Engine Configuration", Repeatability.REPEATABLE, new ChoiceFieldDescriptor(ChoiceFieldType.COMBO_BOX, Arrays.<ChoiceDescriptor>asList(
                                new ChoiceDescriptor("Twin Jet - Wing Mounted", DataFactory.getOWLEntityData(DataFactory.getOWLClass("http://aero.com/quad"), "Quad")),
                                new ChoiceDescriptor("Twin Jet - Tail Mounted", DataFactory.getOWLEntityData(DataFactory.getOWLClass("http://aero.com/quad"), "Quad")),
                                new ChoiceDescriptor("Tri Jet - Wing/Tail Mounted", DataFactory.getOWLEntityData(DataFactory.getOWLClass("http://aero.com/quad"), "Quad")),
                                new ChoiceDescriptor("Tri Jet - Wing Mounted", DataFactory.getOWLEntityData(DataFactory.getOWLClass("http://aero.com/quad"), "Quad")),
                                new ChoiceDescriptor("Quad Jet - Wing Mounted", DataFactory.getOWLEntityData(DataFactory.getOWLClass("http://aero.com/quad"), "Quad")),
                                new ChoiceDescriptor("Quad Jet - Tail Mounted", DataFactory.getOWLEntityData(DataFactory.getOWLClass("http://aero.com/quad"), "Quad"))
                        ))),
                        new FormElementDescriptor(new FormElementId("Possible Roles"), "Role", Repeatability.UNREPEATABLE, new ChoiceFieldDescriptor(ChoiceFieldType.CHECK_BOX, Arrays.<ChoiceDescriptor>asList(
                                new ChoiceDescriptor("Passenger", DataFactory.getOWLEntityData(DataFactory.getOWLClass("http://aero.com/quad"), "Quad")),
                                new ChoiceDescriptor("Cargo", DataFactory.getOWLEntityData(DataFactory.getOWLClass("http://aero.com/quad"), "Quad")),
                                new ChoiceDescriptor("Combi", DataFactory.getOWLEntityData(DataFactory.getOWLClass("http://aero.com/quad"), "Quad"))
                        )))
                )
        ));
//        dispatchServiceManager.execute(new GetFormDataAction(projectId, entity), new DispatchServiceCallback<GetFormDataResult>() {
//            @Override
//            public void handleSuccess(GetFormDataResult result) {
//                displayForm(result.getFormData());
//            }
//
//            @Override
//            public void handleErrorFinally(Throwable throwable) {
//
//            }
//        });
    }


    private void displayForm(FormData formData) {
        for(FormElementView elementView : formView.getElementViews()) {
            Optional<FormElementData> elementData = elementView.getEditor().getValue();
            GWT.log(elementData.toString());
        }
        formView.clear();
        for(FormElementDescriptor elementDescriptor : formData.getFormElementDescriptors()) {
            Optional<FormElementEditor> elementEditor = getFormElementEditor(elementDescriptor);
            if (elementEditor.isPresent()) {
                FormElementView elementView = new FormElementViewImpl();
                elementView.setFormLabel(elementDescriptor.getFormLabel());
                elementView.setEditor(elementEditor.get());
                Optional<FormElementData> data = formData.getFormElementData(elementDescriptor.getFormElementId());
                if(data.isPresent()) {
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
        Optional<ValueEditorFactory<FormDataTuple>> editorFactory = getValueEditorFactory(descriptor.getFormFieldDescriptor());
        if(!editorFactory.isPresent()) {
            return Optional.<FormElementEditor>absent();
        }
        return Optional.<FormElementEditor>of(
                new FormElementEditorImpl(
                        descriptor.getFormElementId(),
                        editorFactory.get(),
                        descriptor.getRepeatability())
        );
    }

    private Optional<ValueEditorFactory<FormDataTuple>> getValueEditorFactory(final FormFieldDescriptor formFieldDescriptor) {
        if(formFieldDescriptor.getAssociatedFieldTypeId().equals(StringFieldDescriptor.getFieldTypeId())) {
            return Optional.<ValueEditorFactory<FormDataTuple>>of(
                    new ValueEditorFactory<FormDataTuple>() {
                        @Override
                        public ValueEditor<FormDataTuple> createEditor() {
                            StringFieldDescriptor descriptor = (StringFieldDescriptor) formFieldDescriptor;
                            StringFieldEditor stringFieldEditor = new StringFieldEditor(projectId);
                            stringFieldEditor.setPlaceholder(descriptor.getPlaceholder());
                            stringFieldEditor.setLineMode(descriptor.getLineMode());
                            stringFieldEditor.setStringType(descriptor.getStringType());
                            return stringFieldEditor;
                        }
                    }
            );
        }
        else if(formFieldDescriptor.getAssociatedFieldTypeId().equals(ChoiceFieldDescriptor.getFieldTypeId())) {
            return Optional.<ValueEditorFactory<FormDataTuple>>of(
                    new ValueEditorFactory<FormDataTuple>() {
                        @Override
                        public ValueEditor<FormDataTuple> createEditor() {
                            ChoiceFieldDescriptor descriptor = (ChoiceFieldDescriptor) formFieldDescriptor;
                            ChoiceFieldEditor editor;
                            if(descriptor.getType() == ChoiceFieldType.RADIO_BUTTON) {
                                editor = new ChoiceFieldRadioButtonEditor();
                            }
                            else if(descriptor.getType() == ChoiceFieldType.CHECK_BOX) {
                                editor = new ChoiceFieldCheckBoxEditor();
                            }
                            else {
                                editor = new ChoiceFieldComboBoxEditor();
                            }
                            editor.setChoices(descriptor.getChoiceDescriptors());
                            return editor;
                        }
                    }
            );
        }
        else {
            return Optional.<ValueEditorFactory<FormDataTuple>>absent();
        }
    }

    public void clear() {

    }

    public IsWidget getView() {
        return formView;
    }
}
