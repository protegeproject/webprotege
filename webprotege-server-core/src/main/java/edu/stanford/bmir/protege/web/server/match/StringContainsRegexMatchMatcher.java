package edu.stanford.bmir.protege.web.server.match;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class StringContainsRegexMatchMatcher implements Matcher<String> {

    @Nonnull
    private final Pattern pattern;

    public StringContainsRegexMatchMatcher(@Nonnull Pattern pattern) {
        this.pattern = checkNotNull(pattern);
    }

    @Override
    public boolean matches(@Nonnull String value) {
        return pattern.matcher(value).find();
    }
}
