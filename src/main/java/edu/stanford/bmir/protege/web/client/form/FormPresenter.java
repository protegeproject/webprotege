package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;

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
        displayForm(new FormDescriptor(
                Arrays.asList(
                        new FormElementDescriptor(
                                new FormElementId("TheLabel"),
                                "Label",
                                Repeatability.UNREPEATABLE,
                                new StringFieldDescriptor(
                                        "Enter label",
                                        StringType.SIMPLE_STRING,
                                        LineMode.SINGLE_LINE,
                                        ""
                                )
                        ),
                        new FormElementDescriptor(new FormElementId("TheComment"), "Comment", Repeatability.UNREPEATABLE, new StringFieldDescriptor("Enter comment", StringType.LANG_STRING, LineMode.MULTI_LINE, "")),
                        new FormElementDescriptor(new FormElementId("Synonyms"), "Synonyms", Repeatability.REPEATABLE, new StringFieldDescriptor("Enter synonym", StringType.LANG_STRING, LineMode.MULTI_LINE, "")),

                        new FormElementDescriptor(
                                new FormElementId("EngineConfiguration"),
                                "Engine Configuration",
                                Repeatability.REPEATABLE,
                                new ChoiceFieldDescriptor(
                                        ChoiceFieldType.COMBO_BOX,
                                        Arrays.<ChoiceDescriptor>asList(
                                    new ChoiceDescriptor("Twin Jet - Wing Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/TJWM"))),
                                    new ChoiceDescriptor("Twin Jet - Tail Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/TJTM"))),
                                    new ChoiceDescriptor("Tri Jet - Wing/Tail Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/TrJWTM"))),
                                    new ChoiceDescriptor("Tri Jet - Wing Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/TrJWM"))),
                                    new ChoiceDescriptor("Quad Jet - Wing Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/QJWM"))),
                                    new ChoiceDescriptor("Quad Jet - Tail Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/QJTM")))
                        ))),
                        new FormElementDescriptor(new FormElementId("PossibleRoles"), "Role", Repeatability.UNREPEATABLE, new ChoiceFieldDescriptor(ChoiceFieldType.CHECK_BOX, Arrays.<ChoiceDescriptor>asList(
                                new ChoiceDescriptor("Passenger", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/Passenger"))),
                                new ChoiceDescriptor("Cargo", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/Cargo"))),
                                new ChoiceDescriptor("Combi", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/Combi")))
                        ))),
                        new FormElementDescriptor(new FormElementId("ClassName"), "The Class", Repeatability.UNREPEATABLE, new ClassNameFieldDescriptor(Collections.<OWLClass>emptySet()))
                )
        ), entity);


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


    private void displayForm(FormDescriptor formDescriptor, final OWLEntity subject) {
        GWT.log("DISPLAY FORM: " + subject);

        for(FormElementView elementView : formView.getElementViews()) {
            Optional<FormDataList> elementData = elementView.getEditor().getValue();
            GWT.log(elementData.toString());
        }
        formView.clear();
        for(FormElementDescriptor elementDescriptor : formDescriptor.getFormElementDescriptors()) {
            Optional<FormElementEditor> elementEditor = getFormElementEditor(elementDescriptor);
            if (elementEditor.isPresent()) {
                FormElementView elementView = new FormElementViewImpl();
                elementView.setFormLabel(elementDescriptor.getFormLabel());
                elementView.setEditor(elementEditor.get());
//                if(elementDescriptor.getFormElementId().equals(new FormElementId("TheLabel"))) {
//                    elementEditor.get().setValue(FormDataList.of(
//                            FormDataPrimitive.get(subject.getIRI())));
//                }
//                else {
                    Optional<FormDataList> data = formDescriptor.getFormElementData(elementDescriptor.getFormElementId());
                    if(data.isPresent()) {
                        elementEditor.get().setValue(data.get());
                    }
                    else {
                        elementEditor.get().clearValue();
                    }

//                }
                formView.addFormElementView(elementView);
            }
        }
    }

    private Optional<FormElementEditor> getFormElementEditor(FormElementDescriptor descriptor) {
        Optional<ValueEditorFactory<FormDataValue>> editorFactory = getValueEditorFactory(descriptor.getFormFieldDescriptor());
        if(!editorFactory.isPresent()) {
            return Optional.<FormElementEditor>absent();
        }
        return Optional.<FormElementEditor>of(
                new FormElementEditorImpl(
                        editorFactory.get(),
                        descriptor.getRepeatability())
        );
    }

    private Optional<ValueEditorFactory<FormDataValue>> getValueEditorFactory(final FormFieldDescriptor formFieldDescriptor) {
        if(formFieldDescriptor.getAssociatedFieldTypeId().equals(StringFieldDescriptor.getFieldTypeId())) {
            return Optional.<ValueEditorFactory<FormDataValue>>of(
                    new ValueEditorFactory<FormDataValue>() {
                        @Override
                        public ValueEditor<FormDataValue> createEditor() {
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
            return Optional.<ValueEditorFactory<FormDataValue>>of(
                    new ValueEditorFactory<FormDataValue>() {
                        @Override
                        public ValueEditor<FormDataValue> createEditor() {
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
                    }
            );
        }
        else if(formFieldDescriptor.getAssociatedFieldTypeId().equals(ClassNameFieldDescriptor.getFieldTypeId())) {
            return Optional.<ValueEditorFactory<FormDataValue>>of(
                    new ValueEditorFactory<FormDataValue>() {
                        @Override
                        public ValueEditor<FormDataValue> createEditor() {
                            return new ClassNameFieldEditor(projectId);
                        }
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
