package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public interface DictionaryLanguage2FieldNameTranslator {

    @Nonnull
    String getValueFieldName(@Nonnull DictionaryLanguage language);

    @Nonnull
    String getWordFieldName(@Nonnull DictionaryLanguage language);

    @Nonnull
    String getEdgeNGramFieldName(@Nonnull DictionaryLanguage language);

    @Nonnull
    String getLocalNameFieldName();
}
