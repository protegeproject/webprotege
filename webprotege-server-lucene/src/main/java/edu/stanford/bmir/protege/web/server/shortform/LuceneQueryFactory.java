package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-10
 */
public class LuceneQueryFactory {

    @Nonnull
    private final FieldNameTranslator fieldNameTranslator;

    @Nonnull
    private final QueryAnalyzerFactory queryAnalyzerFactory;

    @Inject
    public LuceneQueryFactory(@Nonnull FieldNameTranslator fieldNameTranslator,
                              @Nonnull QueryAnalyzerFactory queryAnalyzerFactory) {
        this.fieldNameTranslator = checkNotNull(fieldNameTranslator);
        this.queryAnalyzerFactory = checkNotNull(queryAnalyzerFactory);
    }

    private List<String> getAnalyzedSearchStrings(List<SearchString> searchStrings) {
        return searchStrings.stream()
                     .flatMap(this::getTokens)
                     .collect(Collectors.toList());
    }

    private Stream<String> getTokens(SearchString searchString) {
        try {
            if(isWildCardSearchString(searchString)) {
                return Stream.of(searchString.getRawSearchString());
            }
            else {
                var tokenStream = queryAnalyzerFactory.getTokenizedQueryAnalyzer()
                                                      // Field name is not relevant
                                                      .tokenStream("", searchString.getSearchString());
                tokenStream.addAttribute(CharTermAttribute.class);
                tokenStream.reset();
                var result = new ArrayList<String>();
                while(tokenStream.incrementToken()) {
                    var termAttr = tokenStream.getAttribute(CharTermAttribute.class);
                    if(termAttr != null) {
                        result.add(termAttr.toString());
                    }
                }
                return result.stream();
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static boolean isWildCardSearchString(@Nonnull SearchString searchString) {
        var raw = searchString.getRawSearchString();
        return raw.contains("*") || raw.contains("?");
    }

    @Nonnull
    public Query createQuery(List<SearchString> searchStrings,
                             List<DictionaryLanguage> languages) {
        // Create a disjunction of field queries where each field query is a conjunction
        // of term queries
        var outerBooleanQueryBuilder = new BooleanQuery.Builder();
        for(var lang : languages) {

            var tokenizedValueQuery = buildTokenizedFieldBooleanQuery(searchStrings, lang);

            var nonTokenizedValueQuery = buildNonTokenizedValueFieldQuery(searchStrings, lang);

            // Boost the score with the original field query
            var combinedQueries = new BooleanQuery.Builder();

            combinedQueries.add(tokenizedValueQuery, BooleanClause.Occur.MUST);
            combinedQueries.add(nonTokenizedValueQuery, BooleanClause.Occur.SHOULD);
            var combinedQuery = combinedQueries.build();


            // Combine for all languages using SHOULD
            outerBooleanQueryBuilder.add(combinedQuery, BooleanClause.Occur.SHOULD);
        }
        return outerBooleanQueryBuilder.build();
    }

    private Query buildTokenizedFieldBooleanQuery(List<SearchString> searchStrings, DictionaryLanguage lang) {
        var mustOccurBuilder = new BooleanQuery.Builder();
        for(var searchString : getAnalyzedSearchStrings(searchStrings)) {
            // Analyzed - MUST occur
            var analyzedFieldName = fieldNameTranslator.getTokenizedFieldName(lang);
            var searchStringTerm = new Term(analyzedFieldName, searchString);
            if(searchString.contains("*") || searchString.contains("?")) {
                mustOccurBuilder.add(new WildcardQuery(searchStringTerm), BooleanClause.Occur.MUST);
            }
            else {
                mustOccurBuilder.add(new TermQuery(searchStringTerm), BooleanClause.Occur.MUST);
            }
        }
        return mustOccurBuilder.build();
    }

    private Query buildNonTokenizedValueFieldQuery(List<SearchString> searchStrings, DictionaryLanguage lang) {
        var shouldOccurBuilder = new BooleanQuery.Builder();
        for(var searchString : searchStrings) {
            var nonTokenizedFieldName = fieldNameTranslator.getNonTokenizedFieldName(lang);
            var nonTokenizedQueryString = getNonTokenizedQueryString(lang, searchString);
            var searchStringTerm = new Term(nonTokenizedFieldName, nonTokenizedQueryString);
            shouldOccurBuilder.add(new TermQuery(searchStringTerm), BooleanClause.Occur.MUST);
        }
        var queryForAllTerms = shouldOccurBuilder.build();
        return new BoostQuery(queryForAllTerms, 100f);
    }

    private String getNonTokenizedQueryString(DictionaryLanguage lang, SearchString searchString) {
        try {
            var nonTokenizedFieldName = fieldNameTranslator.getNonTokenizedFieldName(lang);
            var tokenStream = queryAnalyzerFactory.getKeywordQueryAnalyzer()
                                .tokenStream(nonTokenizedFieldName, searchString.getRawSearchString());
            tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            // Should only be one token
            if(tokenStream.incrementToken()) {
                return tokenStream.getAttribute(CharTermAttribute.class).toString();
            }
            else {
                throw new RuntimeException("Could not get query token");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Nonnull
    public Query createQueryForExactOriginalMatch(List<SearchString> searchStrings,
                                                  List<DictionaryLanguage> languages) {
        var outerBooleanQueryBuilder = new BooleanQuery.Builder();
        for(var lang : languages) {
            var originalValueQuery = buildNonTokenizedValueFieldQuery(searchStrings, lang);
            // Combine for all languages using SHOULD. i.e. match any field
            outerBooleanQueryBuilder.add(originalValueQuery, BooleanClause.Occur.SHOULD);
        }
        return outerBooleanQueryBuilder.build();
    }



}
