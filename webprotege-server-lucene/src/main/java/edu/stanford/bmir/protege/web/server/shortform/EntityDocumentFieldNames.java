package edu.stanford.bmir.protege.web.server.shortform;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-09
 */
public interface EntityDocumentFieldNames {

    @Nonnull
    String ENTITY_TYPE = "entityType";

    @Nonnull
    String IRI = "iri";

    String VALUE_FIELD_PREFIX = "value.";

    String WORD_FIELD_PREFIX = "word.";

    String NGRAM_FIELD_PREFIX = "ngram.";

}
