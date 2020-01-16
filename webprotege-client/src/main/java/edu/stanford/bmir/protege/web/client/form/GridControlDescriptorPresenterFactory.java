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
public class GridControlDescriptorPresenterFactory implements FormControlDescriptorPresenterFactory {

    @Nonnull
    private final Provider<GridControlDescriptorPresenter> presenterProvider;

    @Inject
    public GridControlDescriptorPresenterFactory(@Nonnull Provider<GridControlDescriptorPresenter> presenterProvider) {
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
        return GridControlDescriptor.get(ImmutableList.of(), null);
    }

    @Nonnull
    @Override
    public FormControlDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
