package edu.stanford.bmir.protege.web.server.shortform;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.FlattenGraphFilterFactory;
import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-08
 */
public class QueryAnalyzerFactory {

    @Inject
    public QueryAnalyzerFactory() {
    }

    @Nonnull
    public Analyzer getKeywordQueryAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                    .withTokenizer(KeywordTokenizerFactory.NAME)
                    .addTokenFilter(ASCIIFoldingFilterFactory.NAME)
                    .addTokenFilter(LowerCaseFilterFactory.NAME)
                    .build();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Nonnull
    public Analyzer getTokenizedQueryAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                                 .withTokenizer(WhitespaceTokenizerFactory.NAME)
                                 .addTokenFilter(WordDelimiterGraphFilterFactory.NAME,
                                                 "catenateWords", "1")
                                 .addTokenFilter(FlattenGraphFilterFactory.NAME)
                                 .addTokenFilter(ASCIIFoldingFilterFactory.NAME)
                                 .addTokenFilter(LowerCaseFilterFactory.NAME)
                                 .build();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }
}
