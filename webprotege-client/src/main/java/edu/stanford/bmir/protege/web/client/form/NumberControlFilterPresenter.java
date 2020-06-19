package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.NumericValueCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataMatchCriteria;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlDescriptorDto;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
public class NumberControlFilterPresenter implements FormControlFilterPresenter {

    @Nonnull
    private final NumberControlFilterView view;

    @Nonnull
    private final NumberControlDescriptorDto descriptor;

    @Nonnull
    private final NumericValueCriteriaPresenter numericValueCriteriaPresenter;

    @AutoFactory
    public NumberControlFilterPresenter(@Provided @Nonnull NumberControlFilterView view,
                                        @Nonnull NumberControlDescriptorDto descriptor,
                                        @Provided @Nonnull NumericValueCriteriaPresenter numericValueCriteriaPresenter) {
        this.view = checkNotNull(view);
        this.descriptor = checkNotNull(descriptor);
        this.numericValueCriteriaPresenter = numericValueCriteriaPresenter;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        numericValueCriteriaPresenter.start(view.getContainer());
    }

    @Nonnull
    @Override
    public Optional<PrimitiveFormControlDataMatchCriteria> getFilter() {
        return Optional.empty();
    }
}
