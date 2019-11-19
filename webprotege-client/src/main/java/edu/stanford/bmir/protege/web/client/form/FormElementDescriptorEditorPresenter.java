package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class FormElementDescriptorEditorPresenter {

    @Nonnull
    private final FormElementDescriptorEditorView view;

    @Nonnull
    private final ImmutableList<FormFieldDescriptorEditorPresenterFactory> fieldPresenterFactories;

    @Nonnull
    private final Map<String, FormFieldDescriptorPresenter> fieldType2FieldPresenter = new HashMap<>();

    @Nonnull
    private final Map<String, FormFieldDescriptor> fieldType2FieldDescriptor = new HashMap<>();

    private FormFieldDescriptorPresenter currentFieldPresenter;


    @Inject
    public FormElementDescriptorEditorPresenter(@Nonnull FormElementDescriptorEditorView view,
                                                @Nonnull TextFieldDescriptorEditorPresenterFactory textFieldDescriptorEditorPresenterFactory,
                                                @Nonnull NumberFieldDescriptorPresenterFactory numberFieldDescriptorPresenterFactory,
                                                @Nonnull ChoiceFieldDescriptorPresenterFactory choiceFieldDescriptorPresenterFactory) {
        this.view = checkNotNull(view);
        this.fieldPresenterFactories = ImmutableList.of(textFieldDescriptorEditorPresenterFactory, numberFieldDescriptorPresenterFactory,
                                                        choiceFieldDescriptorPresenterFactory);
    }

    public void setNumber(int number) {
        view.setNumber(number);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        List<String> availableTypes = fieldPresenterFactories.stream()
                                                             .map(factory -> factory.getDescriptorEditorType())
                                                             .collect(Collectors.toList());
        view.setAvailableFieldTypes(availableTypes);
        view.setFieldType(availableTypes.get(0));
        view.setFieldTypeChangedHandler(this::handleFieldTypeChanged);
    }

    private void handleFieldTypeChanged() {
        if(currentFieldPresenter != null) {
            FormFieldDescriptor descriptor = currentFieldPresenter.getFormFieldDescriptor();
            fieldType2FieldDescriptor.put(descriptor.getAssociatedType(), descriptor);
        }
        String fieldType = view.getFieldType();
        FormFieldDescriptor nextDescriptor = fieldType2FieldDescriptor.get(fieldType);
        if(nextDescriptor != null) {
            setFormFieldDescriptor(nextDescriptor);
        }
        else {
            fieldPresenterFactories.stream()
                                   .filter(factory -> factory.getDescriptorEditorType().equals(fieldType))
                                   .findFirst()
                                   .map(factory -> factory.createDefaultDescriptor())
                                   .ifPresent(desc -> setFormFieldDescriptor(desc));
        }

    }

    public void setFormElementDescriptor(@Nonnull FormElementDescriptor descriptor) {
        String elementId = descriptor.getId().getId();
        view.setFormElementId(elementId);

        view.setLabel(descriptor.getLabel());

        view.setHelp(descriptor.getHelp());

        view.setRepeatability(descriptor.getRepeatability());

        view.setOptionality(descriptor.getOptionality());

        Optional<OWLProperty> owlProperty = descriptor.getOwlProperty();
        view.clearOwlProperty();
        owlProperty.ifPresent(view::setOwlProperty);

        FormFieldDescriptor formFieldDescriptor = descriptor.getFieldDescriptor();

        setFormFieldDescriptor(formFieldDescriptor);
    }

    public void setFormFieldDescriptor(FormFieldDescriptor formFieldDescriptor) {
        String type = formFieldDescriptor.getAssociatedType();
        view.setFieldType(type);
        FormFieldDescriptorPresenter fieldPresenter = getOrCreateFieldPresenter(type);
        fieldPresenter.start(view.getFieldEditorContainer());
        fieldPresenter.setFormFieldDescriptor(formFieldDescriptor);
        this.currentFieldPresenter = fieldPresenter;
    }

    public FormElementDescriptor getFormElementDescriptor() {
        return null;
    }

    @Nonnull
    public FormFieldDescriptorPresenter getOrCreateFieldPresenter(String type) {
        FormFieldDescriptorPresenter presenter = fieldType2FieldPresenter.get(type);
        if(presenter == null) {
            presenter = fieldPresenterFactories.stream()
                                                .filter(factory -> factory.getDescriptorEditorType().equalsIgnoreCase(type))
                                               .map(formFieldDescriptorEditorPresenterFactory -> {
                                                   FormFieldDescriptorPresenter p = formFieldDescriptorEditorPresenterFactory.create();
                                                   p.setFormFieldDescriptor(formFieldDescriptorEditorPresenterFactory.createDefaultDescriptor());
                                                   return p;
                                               })
                                                .findFirst()
                    .get();
            fieldType2FieldPresenter.put(type, presenter);
            fieldType2FieldDescriptor.put(type, presenter.getFormFieldDescriptor());
        }
        return presenter;
    }
}
