package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.pagination.PageCollector;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilterId;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dagger.internal.codegen.DaggerStreams.toImmutableList;
import static java.util.stream.Collectors.*;

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
    private final LuceneQueryFactory queryFactory;

    @Nonnull
    private final LuceneDictionaryLanguageValuesMatcher luceneDictionaryLanguageValuesMatcher;

    @Nonnull
    private final QueryAnalyzerFactory queryAnalyzerFactory;

    @Inject
    public LuceneIndexImpl(@Nonnull LuceneEntityDocumentTranslator luceneEntityDocumentTranslator,
                           @Nonnull SearcherManager searcherManager,
                           @Nonnull LuceneQueryFactory queryFactory,
                           @Nonnull LuceneDictionaryLanguageValuesMatcher luceneDictionaryLanguageValuesMatcher,
                           @Nonnull QueryAnalyzerFactory queryAnalyzerFactory) {
        this.luceneEntityDocumentTranslator = luceneEntityDocumentTranslator;
        this.searcherManager = searcherManager;
        this.queryFactory = queryFactory;
        this.luceneDictionaryLanguageValuesMatcher = luceneDictionaryLanguageValuesMatcher;
        this.queryAnalyzerFactory = queryAnalyzerFactory;
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
            return getDictionaryLanguageValues(languages,
                                               indexSearcher,
                                               topDocs).map(EntityDictionaryLanguageValues::reduceToEntityShortForms);
        } finally {
            searcherManager.release(indexSearcher);
        }
    }

    public Query getQuery(@Nonnull List<SearchString> searchStrings,
                          @Nonnull List<DictionaryLanguage> languages,
                          boolean exact) throws ParseException {

        if (!exact) {
            return queryFactory.createQuery(searchStrings, languages);
        }
        else {
            return queryFactory.createQueryForExactOriginalMatch(searchStrings, languages);
        }
    }

    @Override
    @Nonnull
    public Optional<Page<EntityShortFormMatches>> search(@Nonnull List<SearchString> searchStrings,
                                                         @Nonnull List<DictionaryLanguage> dictionaryLanguages,
                                                         @Nonnull List<EntitySearchFilter> searchFilters,
                                                         @Nonnull Set<EntityType<?>> entityTypes,
                                                         @Nonnull PageRequest pageRequest) throws IOException, ParseException {
        var indexSearcher = searcherManager.acquire();
        //        indexSearcher.setSimilarity(new EntityBasedSimilarity());
        try {


            var q = getQuery(searchStrings, dictionaryLanguages, false);
            var queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(q, BooleanClause.Occur.MUST);

            var entityTypeQueries = entityTypes.stream()
                                               .map(EntityType::getName)
                                               .map(typeName -> new TermQuery(new Term(EntityDocumentFieldNames.ENTITY_TYPE,
                                                                                       typeName)))
                                               .collect(toList());
            if (!entityTypeQueries.isEmpty()) {
                var entityTypesBuilder = new BooleanQuery.Builder();
                entityTypeQueries.forEach(typeQuery -> entityTypesBuilder.add(typeQuery, BooleanClause.Occur.SHOULD));
                var typeQuery = entityTypesBuilder.build();
                queryBuilder.add(typeQuery, BooleanClause.Occur.MUST);
            }
            if(!searchFilters.isEmpty()) {
                var searchFiltersQueryBuilder = new BooleanQuery.Builder();
                searchFilters.stream()
                             .map(EntitySearchFilter::getId)
                             .map(EntitySearchFilterId::getId)
                             .map(id -> new TermQuery(new Term(EntityDocumentFieldNames.SEARCH_FILTER_MATCHES, id)))
                             .forEach(query -> searchFiltersQueryBuilder.add(query, BooleanClause.Occur.SHOULD));
                queryBuilder.add(searchFiltersQueryBuilder.build(), BooleanClause.Occur.MUST);
            }
            var query = queryBuilder.build();

            var topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
            explain(query, topDocs, indexSearcher);
            var languagesSet = ImmutableSet.copyOf(dictionaryLanguages);
            var page = getDictionaryLanguageValues(dictionaryLanguages,
                                                   indexSearcher,
                                                   topDocs).collect(PageCollector.toPage(pageRequest.getPageNumber(),
                                                                                         pageRequest.getPageSize()));
            return page.map(pg -> pg.transform(entityShortForms -> {
                var matches = luceneDictionaryLanguageValuesMatcher.getShortFormMatches(entityShortForms,
                                                                                        languagesSet,
                                                                                        searchStrings)
                                                                   .collect(toImmutableList());
                return EntityShortFormMatches.get(entityShortForms.getEntity(), matches);
            }));
        } finally {
            searcherManager.release(indexSearcher);
        }
    }

    private void explain(Query query, TopDocs topDocs, IndexSearcher indexSearcher) {
        if(!logger.isDebugEnabled()) {
            return;
        }
        try {
            var maxExplanations = 10;
            for (int i = 0; i < maxExplanations && i < topDocs.scoreDocs.length; i++) {
                ScoreDoc scoreDoc = topDocs.scoreDocs[i];
                logger.debug("");
                var document = indexSearcher.doc(scoreDoc.doc);
                document.getFields()
                        .stream()
                        .map(IndexableField::toString)
                        .forEach(logger::debug);
                var explanation = indexSearcher.explain(query, scoreDoc.doc);
                logger.info(explanation.toString());

            }
        } catch (IOException e) {
            logger.debug("Error when generating explanation", e);
        }
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> findEntities(String shortForm,
                                          List<DictionaryLanguage> languages) throws ParseException, IOException {
        var searchStrings = SearchString.parseSearchString(shortForm);
        var query = getQuery(Collections.singletonList(searchStrings), languages, true);
        var indexSearcher = searcherManager.acquire();
        try {
            var topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
            return getDictionaryLanguageValues(languages, indexSearcher, topDocs).filter(values -> {
                var shortForms = values.getValues();
                return languages.stream().map(shortForms::get).anyMatch(sf -> sf.contains(shortForm));
            }).map(EntityDictionaryLanguageValues::getEntity).collect(Collectors.toList()).stream();

        } finally {
            searcherManager.release(indexSearcher);
        }
    }

    private Stream<EntityDictionaryLanguageValues> getDictionaryLanguageValues(@Nonnull List<DictionaryLanguage> dictionaryLanguages,
                                                                               @Nonnull IndexSearcher indexSearcher,
                                                                               @Nonnull TopDocs topDocs) {
        return Arrays.stream(topDocs.scoreDocs)
                     .map(scoreDoc -> scoreDoc.doc)
                     .map(docId -> getDoc(indexSearcher, docId))
                     .map(doc -> luceneEntityDocumentTranslator.getDictionaryLanguageValues(doc, dictionaryLanguages));
    }

    public Document getDoc(IndexSearcher indexSearcher, Integer docId) {
        try {
            return indexSearcher.doc(docId);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
