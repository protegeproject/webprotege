package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;

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
    private final FormFieldControlStackFactory formFieldControlStackFactory;

    @Nonnull
    private final LanguageMapCurrentLocaleMapper languageMapCurrentLocaleMapper;


    @Inject
    public FormFieldPresenterFactory(@Nonnull Provider<FormFieldView> viewProvider,
                                     @Nonnull FormFieldControlStackFactory formControlFactory,
                                     @Nonnull LanguageMapCurrentLocaleMapper languageMapCurrentLocaleMapper) {
        this.viewProvider = checkNotNull(viewProvider);
        this.formFieldControlStackFactory = checkNotNull(formControlFactory);
        this.languageMapCurrentLocaleMapper = checkNotNull(languageMapCurrentLocaleMapper);
    }

    public FormFieldPresenter create(@Nonnull FormFieldDescriptor fieldDescriptor) {
        FormFieldPresenter presenter = new FormFieldPresenter(viewProvider.get(), fieldDescriptor,
                                                              formFieldControlStackFactory,
                                                              languageMapCurrentLocaleMapper);
        presenter.start();
        return presenter;
    }
}
