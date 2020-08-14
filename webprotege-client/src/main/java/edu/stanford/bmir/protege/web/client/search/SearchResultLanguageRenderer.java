package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.lang.LanguageTagFormatter;
import edu.stanford.bmir.protege.web.shared.search.SearchResultMatch;
import edu.stanford.bmir.protege.web.shared.shortform.*;
import org.eclipse.jdt.core.search.SearchMatch;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-12
 */
public class SearchResultLanguageRenderer {

    @Nonnull
    private final Messages messages;

    @Inject
    public SearchResultLanguageRenderer(@Nonnull Messages messages) {
        this.messages = checkNotNull(messages);
    }

    @Nonnull
    public String getRendering(@Nonnull SearchResultMatch match) {
        return match.getLanguage().accept(new RenderingVisitor(match));
    }

    private String getAnnotationBasedLanguageRendering(@Nonnull SearchResultMatch match) {
        ImmutableMap<DictionaryLanguage, String> rendering = match.getLanguageRendering();
        String languageRendering = rendering
                 .values()
                 .stream()
                 .findFirst()
                 .orElse("");
        if (match.getLanguage().isAnnotationBased()) {
            String langTag = match.getLanguage().getLang();
            if (!langTag.isEmpty()) {
                String formattedLanguageTag = LanguageTagFormatter.format(langTag);
                languageRendering = languageRendering + " @" + formattedLanguageTag;
            }
        }
        return languageRendering;
    }


    private final class RenderingVisitor implements edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageVisitor<String> {

        private SearchResultMatch match;

        public RenderingVisitor(SearchResultMatch match) {
            this.match = match;
        }

        @Override
        public String visit(@Nonnull LocalNameDictionaryLanguage language) {
            return messages.dicationaryLanguage_localname();
        }

        @Override
        public String visit(@Nonnull OboIdDictionaryLanguage language) {
            return messages.dictionaryLanguage_oboId();
        }

        @Override
        public String visit(@Nonnull AnnotationAssertionDictionaryLanguage language) {
            return getAnnotationBasedLanguageRendering(match);
        }

        @Override
        public String visit(@Nonnull AnnotationAssertionPathDictionaryLanguage language) {
            return getAnnotationBasedLanguageRendering(match);
        }

        @Override
        public String visit(@Nonnull PrefixedNameDictionaryLanguage language) {
            return messages.dictionaryLanguage_prefixedName();
        }
    }
}
