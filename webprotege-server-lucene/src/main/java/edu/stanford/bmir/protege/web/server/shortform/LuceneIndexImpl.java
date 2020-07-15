package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableSet;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dagger.internal.codegen.DaggerStreams.toImmutableList;
import static java.util.stream.Collectors.joining;

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

    @Nonnull
    private final LuceneShortFormMatcher luceneShortFormMatcher;

    @Inject
    public LuceneIndexImpl(@Nonnull LuceneEntityDocumentTranslator luceneEntityDocumentTranslator,
                           @Nonnull SearcherManager searcherManager,
                           @Nonnull LuceneQueryParserFactory queryParserFactory,
                           @Nonnull LuceneShortFormMatcher luceneShortFormMatcher) {
        this.luceneEntityDocumentTranslator = luceneEntityDocumentTranslator;
        this.searcherManager = searcherManager;
        this.queryParserFactory = queryParserFactory;
        this.luceneShortFormMatcher = luceneShortFormMatcher;
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
                          @Nonnull List<DictionaryLanguage> languages,
                          boolean exact) throws ParseException {
        if (!exact) {
            var queryParser = queryParserFactory.createQueryParser(languages);
            return queryParser.parse(queryString);
        }
        else {
            var queryParser = queryParserFactory.createQueryParserForExactShortFormMatch(languages);
            return queryParser.parse(queryString);
        }
    }

    @Override
    @Nonnull
    public Optional<Page<EntityShortFormMatches>> search(@Nonnull List<SearchString> searchStrings,
                                                   @Nonnull List<DictionaryLanguage> dictionaryLanguages,
                                                   @Nonnull PageRequest pageRequest) throws IOException, ParseException {
        var indexSearcher = searcherManager.acquire();
        try {
            var queryString = searchStrings
                    .stream()
                    .map(SearchString::getSearchString)
                    .collect(joining(" AND "));

            var query = getQuery(queryString, dictionaryLanguages, false);
            var topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
            var languagesSet = ImmutableSet.copyOf(dictionaryLanguages);
            var page = getEntityShortFormsStream(dictionaryLanguages, indexSearcher, topDocs)
                    .collect(PageCollector.toPage(pageRequest.getPageNumber(), pageRequest.getPageSize()));
            return page.map(pg -> pg.transform(entityShortForms -> {
                var matches = luceneShortFormMatcher.getShortFormMatches(entityShortForms, languagesSet, searchStrings).collect(toImmutableList());
                return EntityShortFormMatches.get(entityShortForms.getEntity(), matches);
            }));
        } finally {
            searcherManager.release(indexSearcher);
        }
    }

    @Override
    public Stream<OWLEntity> findEntities(String shortForm, List<DictionaryLanguage> languages) throws ParseException, IOException {
        var query = getQuery(shortForm, languages, true);
        var indexSearcher = searcherManager.acquire();
        try {
            var topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
            return getEntityShortFormsStream(languages, indexSearcher, topDocs)
                    .filter(entityShortForms -> {
                        var shortForms = entityShortForms.getShortForms();
                        return languages.stream().map(shortForms::get).anyMatch(sf -> sf.equals(shortForm));
                    })
                    .map(EntityShortForms::getEntity)
                    .collect(Collectors.toList()).stream();

        }
        finally {
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
