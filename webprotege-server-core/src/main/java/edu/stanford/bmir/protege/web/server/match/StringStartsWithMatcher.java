package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.StringStartsWithCriteria;
import org.apache.commons.lang.StringUtils;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-19
 */
public class StringStartsWithMatcher implements Matcher<String> {

    @Nonnull
    private final StringStartsWithCriteria criteria;

    public StringStartsWithMatcher(@Nonnull StringStartsWithCriteria criteria) {
        this.criteria = checkNotNull(criteria);
    }

    @Override
    public boolean matches(@Nonnull String value) {
        if(criteria.isIgnoreCase()) {
            return StringUtils.startsWithIgnoreCase(value, criteria.getValue());
        }
        else {
            return value.startsWith(criteria.getValue());
        }
    }
}
