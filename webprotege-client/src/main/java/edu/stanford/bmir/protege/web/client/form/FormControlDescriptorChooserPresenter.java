package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class FormControlDescriptorChooserPresenter {

    @Nonnull
    private final FormControlDescriptorChooserView view;

    @Nonnull
    private final NoControlDescriptorView noControlDescriptorView;

    @Nonnull
    private final ImmutableList<FormControlDescriptorPresenterFactory> fieldPresenterFactories;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final Map<String, FormControlDescriptorPresenter> fieldType2FieldPresenter = new HashMap<>();

    @Nonnull
    private final Map<String, FormControlDescriptor> fieldType2FieldDescriptor = new HashMap<>();

    @Nonnull
    private Optional<FormControlDescriptorPresenter> currentFieldPresenter = Optional.empty();

    @Inject
    public FormControlDescriptorChooserPresenter(@Nonnull FormControlDescriptorChooserView view,
                                                 @Nonnull NoControlDescriptorView noControlDescriptorView,
                                                 @Nonnull ImmutableList<FormControlDescriptorPresenterFactory> fieldPresenterFactories,
                                                 @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.view = view;
        this.noControlDescriptorView = noControlDescriptorView;
        this.fieldPresenterFactories = fieldPresenterFactories;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        fieldPresenterFactories.forEach(factory -> {
            view.addAvailableFieldType(factory.getDescriptorType(), factory.getDescriptorLabel());
        });
        view.setFieldTypeChangedHandler(this::handleFieldTypeChanged);
        handleFieldTypeChanged();
    }

    private void handleFieldTypeChanged() {
        currentFieldPresenter.map(FormControlDescriptorPresenter::getFormFieldDescriptor)
                             .ifPresent(this::cacheFieldDescriptor);

        String fieldType = view.getFieldType();
        FormControlDescriptor nextDescriptor = fieldType2FieldDescriptor.get(fieldType);
        if(nextDescriptor != null) {
            setFormFieldDescriptor(nextDescriptor);
        }
        else {
            fieldPresenterFactories.stream()
                                   .filter(factory -> factory.getDescriptorType()
                                                             .equals(fieldType))
                                   .findFirst()
                                   .map(FormControlDescriptorPresenterFactory::createDefaultDescriptor)
                                   .ifPresent(this::setFormFieldDescriptor);
        }

    }

    public Optional<FormControlDescriptor> getFormFieldDescriptor() {
        return currentFieldPresenter.map(FormControlDescriptorPresenter::getFormFieldDescriptor);
    }

    private void cacheFieldDescriptor(FormControlDescriptor descriptor) {
        fieldType2FieldDescriptor.put(descriptor.getAssociatedType(), descriptor);
    }

    public void setFormFieldDescriptor(FormControlDescriptor formControlDescriptor) {
        String type = formControlDescriptor.getAssociatedType();
        view.setFieldType(type);
        Optional<FormControlDescriptorPresenter> fieldPresenter = getOrCreateFieldPresenter(type);
        fieldPresenter.ifPresent(p -> {
            p.start(view.getFieldEditorContainer());
            p.setFormFieldDescriptor(formControlDescriptor);
            this.currentFieldPresenter = Optional.of(p);
        });
        if(!fieldPresenter.isPresent()) {
            currentFieldPresenter = Optional.empty();
            view.getFieldEditorContainer().setWidget(noControlDescriptorView);
        }
    }


    @Nonnull
    private Optional<FormControlDescriptorPresenter> getOrCreateFieldPresenter(String type) {
        FormControlDescriptorPresenter presenter = fieldType2FieldPresenter.get(type);
        if(presenter != null) {
            return Optional.of(presenter);
        }
        Optional<FormControlDescriptorPresenter> presenterForType = getPresenterForType(type);
        presenterForType
                .ifPresent(p -> {
                    fieldType2FieldPresenter.put(type, p);
                    fieldType2FieldDescriptor.put(type, p.getFormFieldDescriptor());
                });
        return presenterForType;
    }

    private Optional<FormControlDescriptorPresenter> getPresenterForType(String type) {
        return fieldPresenterFactories.stream()
                                      .filter(factory -> factory.getDescriptorType()
                                                                .equals(type))
                                      .map(FormControlDescriptorPresenterFactory::create)
                                      .findFirst();
    }
}
