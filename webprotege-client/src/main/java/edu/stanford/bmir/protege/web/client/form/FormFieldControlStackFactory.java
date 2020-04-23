package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-23
 */
public class FormFieldControlStackFactory {

    @Nonnull
    private final FormControlFactory formControlFactory;

    @Nonnull
    private final Provider<PaginatorPresenter> paginatorPresenterProvider;

    @Inject
    public FormFieldControlStackFactory(@Nonnull FormControlFactory formControlFactory,
                                        @Nonnull Provider<PaginatorPresenter> paginatorPresenterProvider) {
        this.formControlFactory = checkNotNull(formControlFactory);
        this.paginatorPresenterProvider = paginatorPresenterProvider;
    }

    @Nonnull
    public FormControlStack create(@Nonnull FormControlDescriptor formControlDescriptor,
                                   @Nonnull Repeatability repeatability,
                                   @Nonnull FormRegionPosition position) {
        FormControlDataEditorFactory formDataEditorFactory = formControlFactory.getDataEditorFactory(formControlDescriptor);
        return new FormControlStackImpl(formDataEditorFactory,
                                        repeatability,
                                        position,
                                        paginatorPresenterProvider.get());
    }
}
