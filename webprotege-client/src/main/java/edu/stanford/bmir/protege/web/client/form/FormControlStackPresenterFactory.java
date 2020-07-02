package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class FormControlStackPresenterFactory {

    @Nonnull
    private final FormControlStackRepeatingPresenterFactory repeatingPresenterFactory;

    @Nonnull
    private final FormControlStackNonRepeatingPresenterFactory nonRepeatingPresenterFactory;

    @Nonnull
    private final FormControlFactory formControlFactory;

    @Inject
    public FormControlStackPresenterFactory(@Nonnull FormControlStackRepeatingPresenterFactory repeatingPresenterFactory, @Nonnull FormControlStackNonRepeatingPresenterFactory nonRepeatingPresenterFactory, @Nonnull FormControlFactory formControlFactory) {
        this.repeatingPresenterFactory = checkNotNull(repeatingPresenterFactory);
        this.nonRepeatingPresenterFactory = checkNotNull(nonRepeatingPresenterFactory);
        this.formControlFactory = checkNotNull(formControlFactory);
    }

    @Nonnull
    public FormControlStackPresenter create(@Nonnull FormControlDescriptorDto descriptor,
                                            @Nonnull Repeatability repeatability,
                                            @Nonnull FormRegionPosition position) {
        FormControlDataEditorFactory factory = formControlFactory.getDataEditorFactory(descriptor);
        if(checkNotNull(repeatability).equals(Repeatability.NON_REPEATABLE)) {
            FormControl formControl = factory.createFormControl();
            return nonRepeatingPresenterFactory.create(formControl, position);
        }
        else {
            return repeatingPresenterFactory.create(position, factory);
        }
    }
}
