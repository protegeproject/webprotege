package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.server.pagination.PageCollector;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class LuceneIndexImpl implements LuceneIndex {

    private static final Logger logger = LoggerFactory.getLogger(LuceneIndexWriterImpl.class);

    public static final int ENTITY_TYPE_COUNT = EntityType.values().size();


    @Nonnull
    private final LuceneEntityDocumentTranslator luceneEntityDocumentTranslator;

    @Nonnull
    private final SearcherManager searcherManager;

    @Nonnull
    private final LuceneQueryParserFactory queryParserFactory;

    @Inject
    public LuceneIndexImpl(@Nonnull LuceneEntityDocumentTranslator luceneEntityDocumentTranslator,
                           @Nonnull SearcherManager searcherManager,
                           @Nonnull LuceneQueryParserFactory queryParserFactory) {
        this.luceneEntityDocumentTranslator = luceneEntityDocumentTranslator;
        this.searcherManager = searcherManager;
        this.queryParserFactory = queryParserFactory;
    }

    @Nonnull
    @Override
    public Stream<EntityShortForms> find(@Nonnull OWLEntity entity,
                                         @Nonnull List<DictionaryLanguage> languages) throws IOException {
        var iriTerm = new Term(EntityDocumentFieldNames.IRI, entity.getIRI().toString());
        var iriTermQuery = new TermQuery(iriTerm);
        var entityTypeTerm = new Term(EntityDocumentFieldNames.ENTITY_TYPE, entity.getEntityType().getName());
        var entityTypeQuery = new TermQuery(entityTypeTerm);
        var query = new BooleanQuery.Builder().add(iriTermQuery, BooleanClause.Occur.MUST)
                                              .add(entityTypeQuery, BooleanClause.Occur.MUST)
                                              .build();
        var indexSearcher = searcherManager.acquire();
        try {
            var topDocs = indexSearcher.search(query, ENTITY_TYPE_COUNT);
            return getEntityShortFormsStream(languages, indexSearcher, topDocs);
        } finally {
            searcherManager.release(indexSearcher);
        }
    }

    public Query getQuery(@Nonnull String queryString,
                          @Nonnull List<DictionaryLanguage> languages) throws ParseException {
        var queryParser = queryParserFactory.createQueryParser(languages);
        var query = queryParser.parse(queryString);
        logger.info(query.toString());
        return query;
    }

    @Override
    @Nonnull
    public Optional<Page<EntityShortForms>> search(@Nonnull String queryString,
                                                   @Nonnull List<DictionaryLanguage> dictionaryLanguages,
                                                   @Nonnull PageRequest pageRequest) throws IOException, ParseException {
        var indexSearcher = searcherManager.acquire();
        try {
            var query = getQuery(queryString, dictionaryLanguages);
            var topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
            return getEntityShortFormsStream(dictionaryLanguages, indexSearcher, topDocs).collect(PageCollector.toPage(
                    pageRequest.getPageNumber(),
                    pageRequest.getPageSize()));
        } finally {
            searcherManager.release(indexSearcher);
        }
    }

    private Stream<EntityShortForms> getEntityShortFormsStream(@Nonnull List<DictionaryLanguage> dictionaryLanguages,
                                                               IndexSearcher indexSearcher,
                                                               TopDocs topDocs) {
        return Arrays.stream(topDocs.scoreDocs)
                     .map(scoreDoc -> scoreDoc.doc)
                     .map(docId -> getDoc(indexSearcher, docId))
                     .map(doc -> luceneEntityDocumentTranslator.getEntityShortForms(doc, dictionaryLanguages));
    }

    public Document getDoc(IndexSearcher indexSearcher, Integer docId) {
        try {
            return indexSearcher.doc(docId);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


}
