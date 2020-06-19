package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataMatchCriteria;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptorDto;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-18
 */
public class SingleChoiceControlFilterPresenter implements FormControlFilterPresenter {

    @Nonnull
    private final SingleChoiceControlFilterView view;

    @Nonnull
    private final SingleChoiceControlDescriptorDto descriptor;

    @Nonnull
    private final FormControlFactory formControlFactory;

    @AutoFactory
    public SingleChoiceControlFilterPresenter(@Provided @Nonnull SingleChoiceControlFilterView view,
                                              @Nonnull SingleChoiceControlDescriptorDto descriptor,
                                              @Provided @Nonnull FormControlFactory formControlFactory) {
        this.view = checkNotNull(view);
        this.descriptor = checkNotNull(descriptor);
        this.formControlFactory = checkNotNull(formControlFactory);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        FormControlDataEditorFactory dataEditorFactory = formControlFactory.getDataEditorFactory(descriptor);
        FormControl formControl = dataEditorFactory.createFormControl();
        view.getContainer().setWidget(formControl);
    }

    @Nonnull
    @Override
    public Optional<PrimitiveFormControlDataMatchCriteria> getFilter() {
        return Optional.empty();
    }
}
