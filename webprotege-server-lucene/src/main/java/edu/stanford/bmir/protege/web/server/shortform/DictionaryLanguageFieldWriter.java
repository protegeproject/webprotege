package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-06
 */
public class DictionaryLanguageFieldWriter {

    @Nonnull
    private final FieldNameTranslator fieldNameTranslator;

    @Inject
    public DictionaryLanguageFieldWriter(@Nonnull FieldNameTranslator fieldNameTranslator) {
        this.fieldNameTranslator = checkNotNull(fieldNameTranslator);
    }

    /**
     * Adds a field with the specified value to the specified document for the specified dictionary language
     * @param document The document
     * @param language The dictionary language
     * @param value The value for the field
     */
    public void addFieldForDictionaryLanguage(@Nonnull Document document,
                                              @Nonnull DictionaryLanguage language,
                                              @Nonnull String value) {
        // We don't need to store both, just one.  This is the value field.  Storing this
        // allows a read of the annotation value from the index
        var valueFieldName = fieldNameTranslator.getNonTokenizedFieldName(language);
        document.add(new TextField(valueFieldName, value, Field.Store.YES));
        var analyzedFieldName = fieldNameTranslator.getTokenizedFieldName(language);
        document.add(new TextField(analyzedFieldName, value, Field.Store.NO));

    }
}
