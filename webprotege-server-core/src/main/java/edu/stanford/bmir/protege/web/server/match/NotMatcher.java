package edu.stanford.bmir.protege.web.server.match;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class NotMatcher<T> implements Matcher<T> {

    @Nonnull
    private final Matcher<T> matcher;

    public NotMatcher(@Nonnull Matcher<T> matcher) {
        this.matcher = checkNotNull(matcher);
    }

    @Override
    public boolean matches(@Nonnull T value) {
        return !matcher.matches(checkNotNull(value));
    }
}
