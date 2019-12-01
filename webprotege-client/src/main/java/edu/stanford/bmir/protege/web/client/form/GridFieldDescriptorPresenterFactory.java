package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class GridFieldDescriptorPresenterFactory implements FormFieldDescriptorPresenterFactory {

    @Nonnull
    private final Provider<GridFieldDescriptorPresenter> presenterProvider;

    @Inject
    public GridFieldDescriptorPresenterFactory(@Nonnull Provider<GridFieldDescriptorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorLabel() {
        return "Grid";
    }

    @Nonnull
    @Override
    public String getDescriptorType() {
        return GridControlDescriptor.getType();
    }

    @Nonnull
    @Override
    public FormControlDescriptor createDefaultDescriptor() {
        return GridControlDescriptor.get(ImmutableList.of());
    }

    @Nonnull
    @Override
    public FormFieldDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
