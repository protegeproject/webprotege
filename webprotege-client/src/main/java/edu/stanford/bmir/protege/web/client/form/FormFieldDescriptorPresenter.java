package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class FormFieldDescriptorPresenter implements ObjectPresenter<FormFieldDescriptor> {

    @Nonnull
    private ProjectId projectId;

    @Nonnull
    private final FormFieldDescriptorView view;

    @Nonnull
    private final OwlBindingPresenter bindingPresenter;

    @Nonnull
    private final FormControlDescriptorChooserPresenter fieldDescriptorChooserPresenter;

    @Nonnull
    private Optional<FormFieldId> formFieldId = Optional.empty();

    @Nonnull
    private LanguageMapCurrentLocaleMapper localeMapper = new LanguageMapCurrentLocaleMapper();

    @Nonnull
    private final FormsMessages formsMessages;

    private UuidV4Provider uuidV4Provider;

    @Inject
    public FormFieldDescriptorPresenter(@Nonnull ProjectId projectId,
                                        @Nonnull FormFieldDescriptorView view,
                                        @Nonnull OwlBindingPresenter bindingPresenter,
                                        @Nonnull FormControlDescriptorChooserPresenter fieldChooserPresenter,
                                        @Nonnull FormsMessages formsMessages,
                                        @Nonnull UuidV4Provider uuidV4Provider) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.bindingPresenter = checkNotNull(bindingPresenter);
        this.fieldDescriptorChooserPresenter = checkNotNull(fieldChooserPresenter);
        this.formsMessages = checkNotNull(formsMessages);
        this.uuidV4Provider = uuidV4Provider;
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        String valueForCurrentLocale = localeMapper.getValueForCurrentLocale(view.getLabel());
        if(valueForCurrentLocale.isEmpty()) {
            return formsMessages.missingFieldLabel();
        }
        else {
            return valueForCurrentLocale;
        }
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {
        view.setLabelChangedHandler(elementId -> {
            headerLabelHandler.accept(localeMapper.getValueForCurrentLocale(view.getLabel()));
        });
    }

    @Nonnull
    public Optional<FormFieldDescriptor> getValue() {
        Optional<FormControlDescriptor> formFieldDescriptor = fieldDescriptorChooserPresenter.getFormFieldDescriptor();
        if(!formFieldDescriptor.isPresent()) {
            return Optional.empty();
        }
        FormFieldId formFieldIdToSave = formFieldId.orElseGet(() -> {
            String id = uuidV4Provider.get();
            FormFieldId formFieldId = FormFieldId.get(id);
            this.formFieldId = Optional.of(formFieldId);
            return formFieldId;
        });
        FormFieldDescriptor descriptor = FormFieldDescriptor.get(formFieldIdToSave,
                                                                 bindingPresenter.getBinding().orElse(null),
                                                                 view.getLabel(),
                                                                 view.getFieldRun(),
                                                                 formFieldDescriptor.get(),
                                                                 view.getRepeatability(),
                                                                 view.getOptionality(),
                                                                 view.isReadOnly(),
                                                                 view.getInitialExpansionState(),
                                                                 view.getHelp());
        return Optional.of(descriptor);
    }

    public void setValue(@Nonnull FormFieldDescriptor descriptor) {
        bindingPresenter.clear();
        descriptor.getOwlBinding().ifPresent(bindingPresenter::setBinding);

        if(descriptor.getId().getId().equals("")) {
            this.formFieldId = Optional.of(FormFieldId.get(uuidV4Provider.get()));
        }
        else {
            this.formFieldId = Optional.of(descriptor.getId());
        }

        view.setFieldRun(descriptor.getFieldRun());

        view.setLabel(descriptor.getLabel());

        view.setHelp(descriptor.getHelp());

        view.setRepeatability(descriptor.getRepeatability());

        view.setOptionality(descriptor.getOptionality());

        view.setReadOnly(descriptor.isReadOnly());

        view.setInitialExpansionState(descriptor.getInitialExpansionState());


        FormControlDescriptor formControlDescriptor = descriptor.getFormControlDescriptor();
        fieldDescriptorChooserPresenter.setFormFieldDescriptor(formControlDescriptor);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        fieldDescriptorChooserPresenter.start(view.getFieldDescriptorViewContainer());
        bindingPresenter.start(view.getOwlBindingViewContainer());
    }
}
