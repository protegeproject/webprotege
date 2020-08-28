package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.server.pagination.PageCollector;
import edu.stanford.bmir.protege.web.server.shortform.EntityDocumentFieldNames;
import edu.stanford.bmir.protege.web.server.shortform.LuceneEntityDocumentTranslator;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-04
 */
public class DeprecatedEntitiesIndexLuceneImpl implements DeprecatedEntitiesIndex {

    @Nonnull
    private final SearcherManager searcherManager;

    @Nonnull
    private final LuceneEntityDocumentTranslator documentTranslator;

    @Inject
    public DeprecatedEntitiesIndexLuceneImpl(@Nonnull SearcherManager searcherManager,
                                             @Nonnull LuceneEntityDocumentTranslator documentTranslator) {
        this.searcherManager = checkNotNull(searcherManager);
        this.documentTranslator = checkNotNull(documentTranslator);
    }

    @Nonnull
    @Override
    public Page<OWLEntity> getDeprecatedEntities(@Nonnull Set<EntityType<?>> entityTypes,
                                                 @Nonnull PageRequest pageRequest) {
        try {
            return getEntities(entityTypes, pageRequest);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Page<OWLEntity> getEntities(@Nonnull Set<EntityType<?>> entityTypes,
                                        @Nonnull PageRequest pageRequest) throws IOException {
        var deprecatedTrueQuery = new TermQuery(new Term(EntityDocumentFieldNames.DEPRECATED, EntityDocumentFieldNames.DEPRECATED_TRUE));
        var deprecatedEntitiesQuery = getQueryForEntityTypes(entityTypes, deprecatedTrueQuery);
        var indexSearcher = searcherManager.acquire();
        try {
            TopDocs topDocs = indexSearcher.search(deprecatedEntitiesQuery, Integer.MAX_VALUE);
            var docIdsPage = Stream.of(topDocs.scoreDocs)
                  .map(scoreDoc -> scoreDoc.doc)
                  .collect(PageCollector.toPage(pageRequest.getPageNumber(),
                                                pageRequest.getPageSize()));
            return docIdsPage.map(pg -> pg.transform(docId -> toEntity(indexSearcher, docId)))
                      .orElse(Page.emptyPage());
        }
        finally {
            searcherManager.release(indexSearcher);
        }
    }

    private Query getQueryForEntityTypes(@Nonnull Set<EntityType<?>> entityTypes,
                                         @Nonnull Query deprecatedTrueQuery) {
        if (entityTypes.isEmpty()) {
            return deprecatedTrueQuery;
        }
        else {
            var builder = new BooleanQuery.Builder();
            entityTypes.forEach(entityType -> {
                var query = documentTranslator.getEntityTypeDocumentQuery(entityType);
                builder.add(query, BooleanClause.Occur.SHOULD);
            });
            var entityTypeQuery = builder.build();
            return new BooleanQuery.Builder()
                    .add(deprecatedTrueQuery, BooleanClause.Occur.MUST)
                    .add(entityTypeQuery, BooleanClause.Occur.MUST)
                    .build();
        }
    }

    private OWLEntity toEntity(IndexSearcher indexSearcher, Integer docId) {
        try {
            var doc = indexSearcher.doc(docId);
            return documentTranslator.getEntity(doc);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
