package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptorDto;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
public class FormFieldPresenterFactory {

    @Nonnull
    private final Provider<FormFieldView> viewProvider;

    @Nonnull
    private final LanguageMapCurrentLocaleMapper languageMapCurrentLocaleMapper;

    @Nonnull
    private final FormControlStackPresenterFactory controlStackPresenterFactory;


    // Can't @AutoFactory this because it's injected into another @AutoFactory :(
    @Inject
    public FormFieldPresenterFactory(@Nonnull Provider<FormFieldView> viewProvider,
                                     @Nonnull LanguageMapCurrentLocaleMapper languageMapCurrentLocaleMapper,
                                     @Nonnull FormControlStackPresenterFactory controlStackPresenterFactory) {
        this.viewProvider = checkNotNull(viewProvider);
        this.controlStackPresenterFactory = checkNotNull(controlStackPresenterFactory);
        this.languageMapCurrentLocaleMapper = checkNotNull(languageMapCurrentLocaleMapper);
    }

    @Nonnull
    public FormFieldPresenter create(@Nonnull FormFieldDescriptorDto fieldDescriptor) {

        FormControlStackPresenter controlStackPresenter = controlStackPresenterFactory.create(fieldDescriptor.getFormControlDescriptor(),
                                                                                              fieldDescriptor.getRepeatability(),
                                                                                              FormRegionPosition.TOP_LEVEL);
        return new FormFieldPresenter(
                viewProvider.get(),
                fieldDescriptor,
                controlStackPresenter,
                languageMapCurrentLocaleMapper
        );
    }
}
