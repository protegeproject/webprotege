package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 *
 * Translates a {@link DictionaryLanguage} to a lucene document field name.
 */
public interface FieldNameTranslator {

    @Nonnull
    String getOriginalValueFieldName(@Nonnull DictionaryLanguage language);

    @Nonnull
    String getAnalyzedValueFieldName(@Nonnull DictionaryLanguage language);
}
