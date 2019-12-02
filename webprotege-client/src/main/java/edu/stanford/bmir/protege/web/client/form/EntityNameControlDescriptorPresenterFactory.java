package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.EntityNameControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */
public class EntityNameControlDescriptorPresenterFactory implements FormControlDescriptorPresenterFactory {

    @Nonnull
    private Provider<EntityNameControlDescriptorPresenter> presenterProvider;

    @Inject
    public EntityNameControlDescriptorPresenterFactory(@Nonnull Provider<EntityNameControlDescriptorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorLabel() {
        return "Entity name";
    }

    @Nonnull
    @Override
    public String getDescriptorType() {
        return EntityNameControlDescriptor.getFieldTypeId();
    }

    @Nonnull
    @Override
    public FormControlDescriptor createDefaultDescriptor() {
        return EntityNameControlDescriptor.getDefault();
    }

    @Nonnull
    @Override
    public FormControlDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
