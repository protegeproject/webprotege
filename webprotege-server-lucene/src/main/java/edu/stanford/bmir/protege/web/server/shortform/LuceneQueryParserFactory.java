package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-10
 */
public class LuceneQueryParserFactory {

    @Nonnull
    private final DictionaryLanguage2FieldNameTranslator fieldNameTranslator;

    @Nonnull
    private final QueryAnalyzerFactory queryAnalyzerFactory;

    @Inject
    public LuceneQueryParserFactory(@Nonnull DictionaryLanguage2FieldNameTranslator fieldNameTranslator,
                                    @Nonnull QueryAnalyzerFactory queryAnalyzerFactory) {
        this.fieldNameTranslator = checkNotNull(fieldNameTranslator);
        this.queryAnalyzerFactory = checkNotNull(queryAnalyzerFactory);
    }

    @Nonnull
    public QueryParser createQueryParser(List<DictionaryLanguage> languages) {
        String[] fields = languages.stream()
                                   .flatMap(dl -> Stream.of(fieldNameTranslator.getWordFieldName(dl),
                                                            fieldNameTranslator.getEdgeNGramFieldName(dl)))
                                   .toArray(String[]::new);
        return new MultiFieldQueryParser(fields, queryAnalyzerFactory.get());
    }

    @Nonnull
    public QueryParser createQueryParserForExactShortFormMatch(List<DictionaryLanguage> languages) {
        String[] fields = languages.stream()
                                   .flatMap(dl -> Stream.of(fieldNameTranslator.getValueFieldName(dl)))
                                   .toArray(String[]::new);
        return new MultiFieldQueryParser(fields, queryAnalyzerFactory.get());
    }

}
