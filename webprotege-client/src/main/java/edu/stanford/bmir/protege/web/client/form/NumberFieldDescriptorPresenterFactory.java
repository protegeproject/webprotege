package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.NumberFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.NumberFieldRange;
import edu.stanford.bmir.protege.web.shared.form.field.NumberFieldType;
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
public class NumberFieldDescriptorPresenterFactory implements FormFieldDescriptorEditorPresenterFactory {

    @Nonnull
    private final Provider<NumberFieldDescriptorPresenter> presenterProvider;

    @Inject
    public NumberFieldDescriptorPresenterFactory(@Nonnull Provider<NumberFieldDescriptorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorEditorType() {
        return NumberFieldDescriptor.getTypeId();
    }

    @Nonnull
    @Override
    public FormFieldDescriptor createDefaultDescriptor() {
        return new NumberFieldDescriptor(
                "#.##",
                NumberFieldRange.all(),
                NumberFieldType.PLAIN,
                10,
                LanguageMap.empty()
        );
    }

    @Nonnull
    @Override
    public FormFieldDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
