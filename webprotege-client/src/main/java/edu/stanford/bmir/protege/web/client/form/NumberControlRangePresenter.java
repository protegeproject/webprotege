package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlRange;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class NumberControlRangePresenter {

    @Nonnull
    private final NumberControlRangeView view;

    @Inject
    public NumberControlRangePresenter(@Nonnull NumberControlRangeView view) {
        this.view = checkNotNull(view);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setNumberFieldRange(@Nonnull NumberControlRange numberRange) {
        view.clear();
        view.setAnyNumber(numberRange.isAnyNumber());
        if(!numberRange.isAnyNumber()) {
            double lowerBound = numberRange.getLowerBound();
            if(lowerBound != Double.MIN_VALUE) {
                view.setLowerBound(lowerBound);
            }
            view.setLowerBoundType(numberRange.getLowerBoundType());
            double upperBound = numberRange.getUpperBound();
            if(upperBound != Double.MAX_VALUE) {
                view.setUpperBound(upperBound);
            }
            view.setUpperBoundType(numberRange.getUpperBoundType());
        }
    }

    public NumberControlRange getNumberFieldRange() {
        if(view.isAnyNumber()) {
            return NumberControlRange.all();
        }
        else {
            return NumberControlRange.range(
                    view.getLowerBound(),
                    view.getLowerBoundType(),
                    view.getUpperBound(),
                    view.getUpperBoundType()
            );
        }
    }
}
