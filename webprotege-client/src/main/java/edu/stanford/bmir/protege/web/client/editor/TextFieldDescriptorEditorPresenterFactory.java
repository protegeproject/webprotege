package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.LineMode;
import edu.stanford.bmir.protege.web.shared.form.field.StringType;
import edu.stanford.bmir.protege.web.shared.form.field.TextFieldDescriptor;
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
public class TextFieldDescriptorEditorPresenterFactory implements FormFieldDescriptorEditorPresenterFactory {

    @Nonnull
    private final Provider<TextFieldDescriptorEditorPresenter> presenterProvider;


    @Inject
    public TextFieldDescriptorEditorPresenterFactory(@Nonnull Provider<TextFieldDescriptorEditorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorEditorType() {
        return TextFieldDescriptor.getType();
    }

    public static TextFieldDescriptor createDefault() {
        return new TextFieldDescriptor(
                LanguageMap.empty(),
                StringType.SIMPLE_STRING,
                LineMode.SINGLE_LINE,
                "",
                LanguageMap.empty()
        );
    }

    @Nonnull
    @Override
    public FormFieldDescriptor createDefaultDescriptor() {
        return createDefault();
    }

    @Nonnull
    @Override
    public FormFieldDescriptorEditorPresenter create() {
        return presenterProvider.get();
    }
}
