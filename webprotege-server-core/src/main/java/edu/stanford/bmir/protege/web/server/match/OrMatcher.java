package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class OrMatcher<T> implements Matcher<T> {

    @Nonnull
    private final ImmutableList<Matcher<T>> matchers;

    public OrMatcher(@Nonnull ImmutableList<Matcher<T>> matchers) {
        this.matchers = checkNotNull(matchers);
    }

    @Override
    public boolean matches(@Nonnull T value) {
        checkNotNull(value);
        return matchers.stream()
                       .anyMatch(m -> m.matches(value));
    }
}
