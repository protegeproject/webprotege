package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 *
 * Translates a {@link DictionaryLanguage} to a lucene document field name.  Two types of
 * field name can be produced for a given dictionary language: one that represents a field
 * that WILL NOT be tokenized during analysis and one that represents a field that WILL be
 * tokenized during analysis.
 */
public interface FieldNameTranslator {

    /**
     * Gets the field name for the specified dictionary language where the value of the
     * field is not tokenized (i.e. left intact) by Lucene.  This field can be used for
     * exact matches, or to boost the score for an exact match, for example.
     * @param language The dictionary language to translate to a field name
     */
    @Nonnull
    String getNonTokenizedFieldName(@Nonnull DictionaryLanguage language);

    /**
     * Gets the field name for the specified dictionary language where the value of the
     * field is tokenized by Lucene.
     * @param language The dictionary language to translate to a field name
     */
    @Nonnull
    String getTokenizedFieldName(@Nonnull DictionaryLanguage language);
}
