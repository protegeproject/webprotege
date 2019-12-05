package edu.stanford.bmir.protege.web.server.match;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public class MatcherAdapter<S, T> implements Matcher<S> {

    @Nonnull
    private final Class<T> targetCls;

    @Nonnull
    private final Matcher<T> targetMatcher;

    public MatcherAdapter(@Nonnull Class<T> targetCls,
                          @Nonnull Matcher<T> targetMatcher) {
        this.targetCls = checkNotNull(targetCls);
        this.targetMatcher = checkNotNull(targetMatcher);
    }

    @Override
    public boolean matches(@Nonnull S value) {
        if(!targetCls.isInstance(value)) {
            return false;
        }
        else {
            T target = targetCls.cast(value);
            return targetMatcher.matches(target);
        }
    }
}
