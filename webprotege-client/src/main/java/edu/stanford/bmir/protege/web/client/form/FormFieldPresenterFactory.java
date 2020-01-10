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
    private final FormControlFactory formControlFactory;

    @Inject
    public FormFieldPresenterFactory(@Nonnull Provider<FormFieldView> viewProvider,
                                     @Nonnull FormControlFactory formControlFactory) {
        this.viewProvider = checkNotNull(viewProvider);
        this.formControlFactory = checkNotNull(formControlFactory);
    }

    public FormFieldPresenter create(@Nonnull FormFieldDescriptor fieldDescriptor) {
        FormFieldPresenter presenter = new FormFieldPresenter(viewProvider.get(), fieldDescriptor, formControlFactory);
        presenter.start();
        return presenter;
    }
}
