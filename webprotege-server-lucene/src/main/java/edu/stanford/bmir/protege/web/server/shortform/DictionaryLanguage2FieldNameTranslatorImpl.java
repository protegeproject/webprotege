package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.server.shortform.EntityDocumentFieldNames.*;

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
    private String getLocalNameFieldName() {
        return LOCAL_NAME;
    }

    @Nonnull
    @Override
    public String getOriginalValueFieldName(@Nonnull DictionaryLanguage language) {
        return VALUE_FIELD_PREFIX + getFieldNameSuffix(language);
    }

    @Nonnull
    @Override
    public String getAnalyzedValueFieldName(@Nonnull DictionaryLanguage language) {
        return ANALYZED_FIELD_PREFIX + getFieldNameSuffix(language);
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
