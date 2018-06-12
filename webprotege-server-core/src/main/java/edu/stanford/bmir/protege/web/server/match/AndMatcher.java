package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class AndMatcher<T> implements Matcher<T> {

    @Nonnull
    private final ImmutableList<Matcher<T>> matchers;

    public AndMatcher(@Nonnull ImmutableList<Matcher<T>> matchers) {
        this.matchers = checkNotNull(matchers);
    }

    @SafeVarargs
    public static <T> AndMatcher<T> get(Matcher<T> ... matchers) {
        return new AndMatcher<>(ImmutableList.copyOf(matchers));
    }

    @Override
    public boolean matches(@Nonnull T value) {
        return matchers.stream()
                       .allMatch(m -> m.matches(value));
    }
}
