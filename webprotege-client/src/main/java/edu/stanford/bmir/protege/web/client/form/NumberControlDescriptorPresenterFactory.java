package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class NumberControlDescriptorPresenterFactory implements FormControlDescriptorPresenterFactory {

    @Nonnull
    private final Provider<NumberControlDescriptorPresenter> presenterProvider;

    @Inject
    public NumberControlDescriptorPresenterFactory(@Nonnull Provider<NumberControlDescriptorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorLabel() {
        return "Number";
    }

    @Nonnull
    @Override
    public String getDescriptorType() {
        return NumberControlDescriptor.getTypeId();
    }

    @Nonnull
    @Override
    public FormControlDescriptor createDefaultDescriptor() {
        return new NumberControlDescriptor(
                "#.##",
                NumberControlRange.all(),
                NumberControlType.PLAIN,
                10,
                LanguageMap.empty()
        );
    }

    @Nonnull
    @Override
    public FormControlDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
