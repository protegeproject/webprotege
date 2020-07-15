package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public interface LuceneEntityDocumentTranslator {

    @Nonnull
    OWLEntity getEntity(@Nonnull Document document);

    @Nonnull
    EntityShortForms getEntityShortForms(@Nonnull Document document,
                                         @Nonnull List<DictionaryLanguage> dictionaryLanguages);

    @Nonnull
    Document getLuceneDocument(@Nonnull OWLEntity entity);

    @Nonnull
    Query getEntityDocumentQuery(@Nonnull OWLEntity entity);
}
