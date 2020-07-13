package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.ImmutableIntArray;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.QueryBuilder;
import org.semanticweb.owlapi.model.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class SearchableMultiLingualShortFormDictionaryLuceneImpl implements SearchableMultiLingualShortFormDictionary {

    private static final Logger logger = LoggerFactory.getLogger(SearchableMultiLingualShortFormDictionaryLuceneImpl.class);

    @Nonnull
    private final LuceneIndex luceneIndex;

    @Inject
    public SearchableMultiLingualShortFormDictionaryLuceneImpl(@Nonnull LuceneIndex luceneIndex) {
        this.luceneIndex = luceneIndex;
    }

    @Nonnull
    @Override
    public Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                                          @Nonnull Set<EntityType<?>> entityTypes,
                                                          @Nonnull List<DictionaryLanguage> languages) {
        var queryString = searchStrings
                .stream()
                .map(SearchString::getSearchString)
                .collect(joining(" AND "));
        try {
            var entities = luceneIndex.search(queryString, languages, PageRequest.requestFirstPage());;
            if(entities.isPresent()) {
                var resultsPage = entities.get();
                logger.info("Found " + resultsPage.getTotalElements() + " result.  Retrieved " + resultsPage.getPageSize());
            }
            return entities.map(Page::getPageElements).orElse(ImmutableList.of())
                           .stream()
                           .flatMap(sf -> {
                        return sf.getShortForms()
                          .entrySet()
                          .stream()
                          .map(entry -> {
                              var shortFormMatch = ShortFormMatch.get(sf.getEntity(),
                                                                                 entry.getValue(),
                                                                                 entry.getKey(),
                                                                                 1,
                                                                                 ImmutableIntArray.of());

                              return shortFormMatch;
                          });
                    });
        } catch (IOException | ParseException e) {
            logger.error("Error performing search", e);
            return Stream.empty();
        }
    }
}
