package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.TextControlDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class TextFieldDescriptorPresenterFactory implements FormFieldDescriptorPresenterFactory {

    @Nonnull
    private final Provider<TextFieldDescriptorPresenter> presenterProvider;


    @Inject
    public TextFieldDescriptorPresenterFactory(@Nonnull Provider<TextFieldDescriptorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorLabel() {
        return "Text";
    }

    @Nonnull
    @Override
    public String getDescriptorType() {
        return TextControlDescriptor.getType();
    }

    public static TextControlDescriptor createDefault() {
        return new TextControlDescriptor(
                LanguageMap.empty(),
                StringType.SIMPLE_STRING,
                LineMode.SINGLE_LINE,
                "",
                LanguageMap.empty()
        );
    }

    @Nonnull
    @Override
    public FormControlDescriptor createDefaultDescriptor() {
        return createDefault();
    }

    @Nonnull
    @Override
    public FormFieldDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
