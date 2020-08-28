package edu.stanford.bmir.protege.web.server.shortform;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherManager;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
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
    private final Provider<LuceneEntityDocumentTranslator> documentTranslatorProvider;

    @Nonnull
    private final SearcherManager searcherManager;

    @Inject
    public LuceneIndexUpdaterImpl(@Nonnull IndexWriter indexWriter,
                                  @Nonnull Provider<LuceneEntityDocumentTranslator> documentTranslatorProvider,
                                  @Nonnull SearcherManager searcherManager) {
        this.indexWriter = checkNotNull(indexWriter);
        this.documentTranslatorProvider = checkNotNull(documentTranslatorProvider);
        this.searcherManager = checkNotNull(searcherManager);
    }

    @Override
    public void updateIndexForEntities(@Nonnull Collection<OWLEntity> entities) {
        try {
            var documentTranslator = documentTranslatorProvider.get();
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
