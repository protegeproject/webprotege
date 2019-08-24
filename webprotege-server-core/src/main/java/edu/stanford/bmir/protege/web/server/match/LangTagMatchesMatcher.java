package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
public class LangTagMatchesMatcher implements Matcher<String> {

    @Nonnull
    private final ImmutableList<Locale.LanguageRange> languageRange;

    public LangTagMatchesMatcher(@Nonnull ImmutableList<Locale.LanguageRange> languageRange) {
        this.languageRange = checkNotNull(languageRange);
    }

    public static LangTagMatchesMatcher fromPattern(@Nonnull String pattern) {
        try {
            return new LangTagMatchesMatcher(ImmutableList.copyOf(Locale.LanguageRange.parse(pattern)));
        } catch (IllegalArgumentException e) {
            return new LangTagMatchesMatcher(ImmutableList.of());
        }
    }

    @Override
    public boolean matches(@Nonnull String value) {
        return !value.isEmpty()
                && !Locale.filterTags(languageRange, Collections.singleton(value)).isEmpty();
    }
}
