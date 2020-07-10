package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class DictionaryLanguage2FieldNameTranslatorImpl implements DictionaryLanguage2FieldNameTranslator {

    private static final String LOCAL_NAME = "localName";

    @Inject
    public DictionaryLanguage2FieldNameTranslatorImpl() {
    }

    @Nonnull
    @Override
    public String getLocalNameFieldName() {
        return LOCAL_NAME;
    }

    @Nonnull
    @Override
    public String getValueFieldName(@Nonnull DictionaryLanguage language) {
        return "value." + getFieldNameSuffix(language);
    }

    @Nonnull
    @Override
    public String getWordFieldName(@Nonnull DictionaryLanguage language) {
        return "word." + getFieldNameSuffix(language);
    }

    @Nonnull
    public String getEdgeNGramFieldName(@Nonnull DictionaryLanguage language) {
        return "ngram." + getFieldNameSuffix(language);
    }

    @Nonnull
    private String getFieldNameSuffix(@Nonnull DictionaryLanguage language) {
        if (language.isAnnotationBased()) {
            return language.getLang() + "@" + language.getAnnotationPropertyIri();
        }
        else {
            return getLocalNameFieldName();
        }
    }
}
