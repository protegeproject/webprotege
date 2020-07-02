package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.FixedChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.MultiChoiceControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-10
 */
public class MultiChoiceControlDescriptorPresenterFactory implements FormControlDescriptorPresenterFactory {

    @Nonnull
    private final Provider<MultiChoiceControlDescriptorPresenter> presenterProvider;

    @Inject
    public MultiChoiceControlDescriptorPresenterFactory(@Nonnull Provider<MultiChoiceControlDescriptorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorLabel() {
        return "Multiple choice";
    }

    @Nonnull
    @Override
    public String getDescriptorType() {
        return MultiChoiceControlDescriptor.getType();
    }

    @Nonnull
    @Override
    public FormControlDescriptor createDefaultDescriptor() {
        return MultiChoiceControlDescriptor.get(
                FixedChoiceListSourceDescriptor.get(ImmutableList.of()),
                ImmutableList.of()
        );
    }

    @Nonnull
    @Override
    public FormControlDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
