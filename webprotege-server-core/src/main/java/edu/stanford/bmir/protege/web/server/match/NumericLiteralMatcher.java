package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.function.DoublePredicate;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
public class NumericLiteralMatcher implements Matcher<OWLLiteral> {

    private final DoublePredicate doublePredicate;

    public NumericLiteralMatcher(DoublePredicate doublePredicate) {
        this.doublePredicate = doublePredicate;
    }

    @Override
    public boolean matches(@Nonnull OWLLiteral value) {
        try {
            double d = Double.parseDouble(value.getLiteral().trim());
            return doublePredicate.test(d);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Matcher<OWLLiteral> forEqualTo(double value) {
        return new NumericLiteralMatcher(d -> d == value);
    }

    public static Matcher<OWLLiteral> forLessThan(double value) {
        return new NumericLiteralMatcher(d -> d < value);
    }

    public static Matcher<OWLLiteral> forLessThanOrEqualTo(double value) {
        return new NumericLiteralMatcher(d -> d <= value);
    }

    public static Matcher<OWLLiteral> forGreaterThan(double value) {
        return new NumericLiteralMatcher(d -> d > value);
    }

    public static Matcher<OWLLiteral> forGreaterThanOrEqualTo(double value) {
        return new NumericLiteralMatcher(d -> d >= value);
    }
}
