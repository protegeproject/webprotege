package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.NumericPredicate;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public interface NumericValueCriteriaView extends IsWidget {

    @Nonnull
    Optional<Double> getValue();

    void setValue(double value);

    @Nonnull
    NumericPredicate getNumericPredicate();

    void setNumericPredicate(@Nonnull NumericPredicate predicate);

    void clear();
}
