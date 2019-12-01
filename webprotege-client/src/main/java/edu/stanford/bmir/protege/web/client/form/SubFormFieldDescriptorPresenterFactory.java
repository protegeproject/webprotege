package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SubFormControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class SubFormFieldDescriptorPresenterFactory implements FormFieldDescriptorPresenterFactory {

    @Nonnull
    private Provider<SubFormFieldDescriptorPresenter> presenterProvider;

    @Inject
    public SubFormFieldDescriptorPresenterFactory(@Nonnull Provider<SubFormFieldDescriptorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorLabel() {
        return "Sub-form";
    }

    @Nonnull
    @Override
    public String getDescriptorType() {
        return SubFormControlDescriptor.getFieldTypeId();
    }

    @Nonnull
    @Override
    public FormControlDescriptor createDefaultDescriptor() {
        return new SubFormControlDescriptor(FormDescriptor.empty());
    }

    @Nonnull
    @Override
    public FormFieldDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
