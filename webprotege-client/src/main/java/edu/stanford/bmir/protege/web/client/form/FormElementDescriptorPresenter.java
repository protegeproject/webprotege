package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
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

    interface RemoveFormElementDescriptorHandler {
        void handleRemoveFormElementDescriptor();
    }

    @Nonnull
    private ProjectId projectId;

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

    @Nonnull
    private DispatchServiceManager dispatchServiceManager;

    @Inject
    public FormElementDescriptorPresenter(@Nonnull ProjectId projectId,
                                          @Nonnull FormElementDescriptorView view,
                                          @Nonnull NoFieldDescriptorView noFieldDescriptorView,
                                          @Nonnull TextFieldDescriptorPresenterFactory textFieldDescriptorEditorPresenterFactory,
                                          @Nonnull NumberFieldDescriptorPresenterFactory numberFieldDescriptorPresenterFactory,
                                          @Nonnull ChoiceFieldDescriptorPresenterFactory choiceFieldDescriptorPresenterFactory,
                                          @Nonnull ImageDescriptorPresenterFactory imageDescriptorPresenterFactory,
                                          @Nonnull EntityNameFieldDescriptorPresenterFactory entityNameFieldDescriptorPresenterFactory,
                                          @Nonnull SubFormFieldDescriptorPresenterFactory subFormFieldDescriptorPresenterFactory,
                                          @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.view = checkNotNull(view);
        this.noFieldDescriptorView = checkNotNull(noFieldDescriptorView);
        this.dispatchServiceManager = dispatchServiceManager;
        this.fieldPresenterFactories = ImmutableList.of(textFieldDescriptorEditorPresenterFactory,
                                                        numberFieldDescriptorPresenterFactory,
                                                        choiceFieldDescriptorPresenterFactory,
                                                        imageDescriptorPresenterFactory,
                                                        entityNameFieldDescriptorPresenterFactory,
                                                        subFormFieldDescriptorPresenterFactory);
    }

    public Optional<FormElementDescriptor> getFormElementDescriptor() {
        if(currentFieldPresenter.isPresent()) {
            FormFieldDescriptorPresenter p = currentFieldPresenter.get();
            return Optional.of(getFormElementDescriptor(p));
        }
        else {
            return Optional.empty();
        }
    }

    public FormElementDescriptor getFormElementDescriptor(FormFieldDescriptorPresenter fieldDescriptorPresenter) {
        FormFieldDescriptor formFieldDescriptor = fieldDescriptorPresenter.getFormFieldDescriptor();
        return FormElementDescriptor.get(FormElementId.get(view.getFormElementId()),
                                         view.getOwlProperty().map(
                                                 OWLPropertyData::getEntity).orElse(null),
                                         view.getLabel(),
                                         view.getElementRun(),
                                         formFieldDescriptor,
                                         view.getRepeatability(),
                                         view.getOptionality(),
                                         view.getHelp(),
                                         Collections.emptyMap());
    }

    public void setFormElementDescriptor(@Nonnull FormElementDescriptor descriptor) {

        view.clearOwlProperty();
        Optional<OWLProperty> owlProperty = descriptor.getOwlProperty();
        owlProperty.ifPresent(property -> {
            dispatchServiceManager.execute(new GetEntityRenderingAction(projectId,
                                                                        property),
                                           result -> {
                                               OWLEntityData entityData = result.getEntityData();
                                               if(entityData instanceof OWLPropertyData) {
                                                   view.setOwlProperty((OWLPropertyData) entityData);
                                               }
            });
        });


        String elementId = descriptor.getId()
                                     .getId();
        view.setFormElementId(elementId);

        view.setElementRun(descriptor.getElementRun());

        view.setLabel(descriptor.getLabel());

        view.setHelp(descriptor.getHelp());

        view.setRepeatability(descriptor.getRepeatability());

        view.setOptionality(descriptor.getOptionality());



        FormFieldDescriptor formFieldDescriptor = descriptor.getFieldDescriptor();

        setFormFieldDescriptor(formFieldDescriptor);
    }

    public void setNumber(int number) {
        view.setNumber(number);
    }

    public void setRemoveFormElementDescriptorHandler(RemoveFormElementDescriptorHandler handler) {
        view.setRemoveFormElementDescriptorHandler(handler);
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
