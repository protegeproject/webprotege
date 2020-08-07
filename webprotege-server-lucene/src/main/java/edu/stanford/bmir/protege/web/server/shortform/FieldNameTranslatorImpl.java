package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.server.shortform.EntityDocumentFieldNames.*;
import static java.util.stream.Collectors.joining;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class FieldNameTranslatorImpl implements FieldNameTranslator {

    private static final DictionaryLanguageVisitor<String> languageVisitor = new DictionaryLanguageVisitor<>() {

        @Override
        public String visit(@Nonnull LocalNameDictionaryLanguage language) {
            return EntityDocumentFieldNames.LOCAL_NAME;
        }

        @Override
        public String visit(@Nonnull OboIdDictionaryLanguage language) {
            return EntityDocumentFieldNames.OBO_ID;
        }

        @Override
        public String visit(@Nonnull AnnotationAssertionDictionaryLanguage language) {
            return language.getLang() + "@" + language.getJsonAnnotationPropertyIri();
        }

        @Override
        public String visit(@Nonnull AnnotationAssertionPathDictionaryLanguage language) {
            var path = language.getAnnotationPropertyPath().stream().map(Object::toString).collect(joining(";"));
            return path + "@" + language.getLang();
        }

        @Override
        public String visit(@Nonnull PrefixedNameDictionaryLanguage language) {
            return PREFIXED_NAME;
        }
    };

    @Inject
    public FieldNameTranslatorImpl() {
    }

    @Nonnull
    @Override
    public String getNonTokenizedFieldName(@Nonnull DictionaryLanguage language) {
        return KEYWORD_FIELD_PREFIX + getFieldNameSuffix(language);
    }

    @Nonnull
    @Override
    public String getTokenizedFieldName(@Nonnull DictionaryLanguage language) {
        return TEXT_FIELD_PREFIX + getFieldNameSuffix(language);
    }

    @Nonnull
    private String getFieldNameSuffix(@Nonnull DictionaryLanguage language) {
        return language.accept(languageVisitor);
    }
}
