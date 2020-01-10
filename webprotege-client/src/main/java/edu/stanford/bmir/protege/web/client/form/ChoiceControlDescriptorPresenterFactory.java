package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlType;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class ChoiceControlDescriptorPresenterFactory implements FormControlDescriptorPresenterFactory {


    @Nonnull
    private final Provider<ChoiceControlDescriptorPresenter> presenterProvider;


    @Inject
    public ChoiceControlDescriptorPresenterFactory(@Nonnull Provider<ChoiceControlDescriptorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorLabel() {
        return "Choice";
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
                ImmutableList.of()
        );
    }

    @Nonnull
    @Override
    public FormControlDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
