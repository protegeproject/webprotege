package edu.stanford.bmir.protege.web.server.shortform;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 */
public class LuceneSearchStringTokenizer {

    @Nonnull
    private final QueryAnalyzerFactory queryAnalyzerFactory;

    @Inject
    public LuceneSearchStringTokenizer(@Nonnull QueryAnalyzerFactory queryAnalyzerFactory) {
        this.queryAnalyzerFactory = queryAnalyzerFactory;
    }

    @Nonnull
    public Set<String> getTokenizedSearchStrings(List<SearchString> searchStrings) {
        return searchStrings.stream()
                            .map(SearchString::getSearchString)
                            .flatMap(this::tokenize)
                            .collect(toImmutableSet());

    }

    public Stream<String> tokenize(String searchString) {
        var queryAnalyzer = queryAnalyzerFactory.getTokenizedQueryAnalyzer();
        try (var queryTokenStream = queryAnalyzer.tokenStream("", searchString)) {
            var charTermAttribute = queryTokenStream.addAttribute(CharTermAttribute.class);
            queryTokenStream.reset();
            Set<String> tokens = new HashSet<>();
            while (queryTokenStream.incrementToken()) {
                var tokenImage = charTermAttribute.toString();
                tokens.add(tokenImage);
            }
            queryTokenStream.end();
            return tokens.stream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
