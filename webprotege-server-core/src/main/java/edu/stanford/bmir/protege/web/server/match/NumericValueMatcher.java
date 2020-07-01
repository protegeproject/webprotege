package edu.stanford.bmir.protege.web.server.match;

import com.google.common.primitives.Doubles;
import edu.stanford.bmir.protege.web.shared.match.criteria.NumericPredicate;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class NumericValueMatcher implements Matcher<String> {

    @Nonnull
    private final NumericPredicate predicate;

    private final double value;

    public NumericValueMatcher(@Nonnull NumericPredicate predicate, double value) {
        this.predicate = checkNotNull(predicate);
        this.value = value;
    }

    @Override
    public boolean matches(@Nonnull String s) {
        if(s.isEmpty()) {
            return false;
        }
        // We use Doubles.tryParse because failures are expected
        // and Double.parseDouble uses exceptions to indicate failure,
        // which are expensive.
        Double d = Doubles.tryParse(s);
        return d != null && predicate.eval(d, value);
    }
}
