package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.FixedChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlType;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class SingleChoiceControlDescriptorPresenterFactory implements FormControlDescriptorPresenterFactory {


    @Nonnull
    private final Provider<SingleControlDescriptorPresenter> presenterProvider;


    @Inject
    public SingleChoiceControlDescriptorPresenterFactory(@Nonnull Provider<SingleControlDescriptorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorLabel() {
        return "Single choice";
    }

    @Nonnull
    @Override
    public String getDescriptorType() {
        return SingleChoiceControlDescriptor.getType();
    }

    @Nonnull
    @Override
    public FormControlDescriptor createDefaultDescriptor() {
        return SingleChoiceControlDescriptor.get(
                SingleChoiceControlType.SEGMENTED_BUTTON,
                FixedChoiceListSourceDescriptor.get(ImmutableList.of())
        );
    }

    @Nonnull
    @Override
    public FormControlDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
