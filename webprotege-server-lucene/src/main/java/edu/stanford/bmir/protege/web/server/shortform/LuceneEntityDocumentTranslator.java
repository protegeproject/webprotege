package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 * <p>
 * Translates between an entity and a Lucene document representation of the entity.
 */
public interface LuceneEntityDocumentTranslator {

    /**
     * Extracts an {@link OWLEntity} from a Lucene document
     *
     * @param document The Lucene document
     * @return The entity that the Lucene document contains/represents
     */
    @Nonnull
    OWLEntity getEntity(@Nonnull Document document);

    /**
     * Extracts the values of the fields that represent the specified dictionary languages from
     * the specified Lucene document.
     *
     * @param document            The Lucene document
     * @param dictionaryLanguages The list of dictionary languages for which values should be retrieved
     * @return A map containing the values for the specified dictionary languages and the entity that
     * the document represents
     */
    @Nonnull
    EntityDictionaryLanguageValues getDictionaryLanguageValues(@Nonnull Document document,
                                                               @Nonnull List<DictionaryLanguage> dictionaryLanguages);

    /**
     * Gets a Lucene document for the specified entity
     *
     * @param entity The entity
     * @return A Lucene document that encodes/represents the specified entity.
     */
    @Nonnull
    Document getLuceneDocument(@Nonnull OWLEntity entity);

    @Nonnull
    Query getEntityDocumentQuery(@Nonnull OWLEntity entity);

    @Nonnull
    Query getEntityTypeDocumentQuery(@Nonnull EntityType<?> entityType);
}
