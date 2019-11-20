package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class FormElementDescriptorPresenter {

    @Nonnull
    private final FormElementDescriptorView view;

    @Nonnull
    private final NoFieldDescriptorView noFieldDescriptorView;

    @Nonnull
    private final ImmutableList<FormFieldDescriptorPresenterFactory> fieldPresenterFactories;

    @Nonnull
    private final Map<String, FormFieldDescriptorPresenter> fieldType2FieldPresenter = new HashMap<>();

    @Nonnull
    private final Map<String, FormFieldDescriptor> fieldType2FieldDescriptor = new HashMap<>();

    @Nonnull
    private Optional<FormFieldDescriptorPresenter> currentFieldPresenter = Optional.empty();


    @Inject
    public FormElementDescriptorPresenter(@Nonnull FormElementDescriptorView view,
                                          @Nonnull NoFieldDescriptorView noFieldDescriptorView,
                                          @Nonnull TextFieldDescriptorPresenterFactory textFieldDescriptorEditorPresenterFactory,
                                          @Nonnull NumberFieldDescriptorPresenterFactory numberFieldDescriptorPresenterFactory,
                                          @Nonnull ChoiceFieldDescriptorPresenterFactory choiceFieldDescriptorPresenterFactory,
                                          @Nonnull ImageDescriptorPresenterFactory imageDescriptorPresenterFactory,
                                          @Nonnull EntityNameFieldDescriptorPresenterFactory entityNameFieldDescriptorPresenterFactory) {
        this.view = checkNotNull(view);
        this.noFieldDescriptorView = checkNotNull(noFieldDescriptorView);
        this.fieldPresenterFactories = ImmutableList.of(textFieldDescriptorEditorPresenterFactory,
                                                        numberFieldDescriptorPresenterFactory,
                                                        choiceFieldDescriptorPresenterFactory,
                                                        imageDescriptorPresenterFactory,
                                                        entityNameFieldDescriptorPresenterFactory);
    }

    public FormElementDescriptor getFormElementDescriptor() {
        return null;
    }

    public void setFormElementDescriptor(@Nonnull FormElementDescriptor descriptor) {
        String elementId = descriptor.getId()
                                     .getId();
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

    public void setNumber(int number) {
        view.setNumber(number);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        fieldPresenterFactories.forEach(factory -> {
            view.addAvailableFieldType(factory.getDescriptorType(), factory.getDescriptorLabel());
        });
        view.setFieldTypeChangedHandler(this::handleFieldTypeChanged);
    }

    private void handleFieldTypeChanged() {
        currentFieldPresenter.map(FormFieldDescriptorPresenter::getFormFieldDescriptor)
                             .ifPresent(this::cacheFieldDescriptor);

        String fieldType = view.getFieldType();
        FormFieldDescriptor nextDescriptor = fieldType2FieldDescriptor.get(fieldType);
        if(nextDescriptor != null) {
            setFormFieldDescriptor(nextDescriptor);
        }
        else {
            fieldPresenterFactories.stream()
                                   .filter(factory -> factory.getDescriptorType()
                                                             .equals(fieldType))
                                   .findFirst()
                                   .map(FormFieldDescriptorPresenterFactory::createDefaultDescriptor)
                                   .ifPresent(this::setFormFieldDescriptor);
        }

    }

    private void cacheFieldDescriptor(FormFieldDescriptor descriptor) {
        fieldType2FieldDescriptor.put(descriptor.getAssociatedType(), descriptor);
    }

    public void setFormFieldDescriptor(FormFieldDescriptor formFieldDescriptor) {
        String type = formFieldDescriptor.getAssociatedType();
        view.setFieldType(type);
        Optional<FormFieldDescriptorPresenter> fieldPresenter = getOrCreateFieldPresenter(type);
        fieldPresenter.ifPresent(p -> {
            p.start(view.getFieldEditorContainer());
            p.setFormFieldDescriptor(formFieldDescriptor);
            this.currentFieldPresenter = Optional.of(p);
        });
        if(!fieldPresenter.isPresent()) {
            currentFieldPresenter = Optional.empty();
            view.getFieldEditorContainer().setWidget(noFieldDescriptorView);
        }
    }


    @Nonnull
    public Optional<FormFieldDescriptorPresenter> getOrCreateFieldPresenter(String type) {
        FormFieldDescriptorPresenter presenter = fieldType2FieldPresenter.get(type);
        if(presenter != null) {
            return Optional.of(presenter);
        }
        Optional<FormFieldDescriptorPresenter> presenterForType = getPresenterForType(type);
        presenterForType
                .ifPresent(p -> {
                    fieldType2FieldPresenter.put(type, p);
                    fieldType2FieldDescriptor.put(type, p.getFormFieldDescriptor());
                });
        return presenterForType;
    }

    public Optional<FormFieldDescriptorPresenter> getPresenterForType(String type) {
        return fieldPresenterFactories.stream()
                                      .filter(factory -> factory.getDescriptorType()
                                                                .equals(type))
                                      .map(FormFieldDescriptorPresenterFactory::create)
                                      .findFirst();
    }
}
