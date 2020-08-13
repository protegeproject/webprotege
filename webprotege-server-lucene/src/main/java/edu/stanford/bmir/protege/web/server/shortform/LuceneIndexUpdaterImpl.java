package edu.stanford.bmir.protege.web.server.shortform;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherManager;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 */
public class LuceneIndexUpdaterImpl implements LuceneIndexUpdater {

    @Nonnull
    private final IndexWriter indexWriter;

    @Nonnull
    private final LuceneEntityDocumentTranslator documentTranslator;

    @Nonnull
    private final SearcherManager searcherManager;

    @Inject
    public LuceneIndexUpdaterImpl(@Nonnull IndexWriter indexWriter,
                                  @Nonnull LuceneEntityDocumentTranslator documentTranslator,
                                  @Nonnull SearcherManager searcherManager) {
        this.indexWriter = checkNotNull(indexWriter);
        this.documentTranslator = checkNotNull(documentTranslator);
        this.searcherManager = checkNotNull(searcherManager);
    }

    @Override
    public void updateIndexForEntity(@Nonnull Collection<OWLEntity> entities) {
        try {
            var deleteQueries = entities.stream()
                    .map(documentTranslator::getEntityDocumentQuery)
                    .toArray(Query[]::new);

            indexWriter.deleteDocuments(deleteQueries);

            entities.stream()
                    .map(documentTranslator::getLuceneDocument)
                    .forEach(this::addDocument);
            indexWriter.commit();
            searcherManager.maybeRefresh();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void addDocument(@Nonnull Document document) {
        try {
            indexWriter.addDocument(document);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
