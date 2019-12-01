package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.ChoiceControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceFieldType;
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
public class ChoiceFieldDescriptorPresenterFactory implements FormFieldDescriptorPresenterFactory {


    @Nonnull
    private final Provider<ChoiceFieldDescriptorPresenter> presenterProvider;


    @Inject
    public ChoiceFieldDescriptorPresenterFactory(@Nonnull Provider<ChoiceFieldDescriptorPresenter> presenterProvider) {
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
        return ChoiceControlDescriptor.getType();
    }

    @Nonnull
    @Override
    public FormControlDescriptor createDefaultDescriptor() {
        return new ChoiceControlDescriptor(
                ChoiceFieldType.SEGMENTED_BUTTON,
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    @Nonnull
    @Override
    public FormFieldDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
