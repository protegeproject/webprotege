package edu.stanford.bmir.protege.web.server.shortform;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.DelegatingAnalyzerWrapper;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 *
 * Provides analyzers appropriate to the fields used
 */
public class IndexingAnalyzerWrapper extends DelegatingAnalyzerWrapper {

    private final Analyzer analyzer;

    private final Analyzer keywordAnalyzer;

    private final int minGramSize;

    private final int maxGramSize;

    @Inject
    public IndexingAnalyzerWrapper(@MinGramSize int minGramSize,
                                   @MaxGramSize int maxGramSize) {
        super(PER_FIELD_REUSE_STRATEGY);
        this.minGramSize = minGramSize;
        this.maxGramSize = maxGramSize;
        analyzer = createAnalyzer();
        keywordAnalyzer = createKeywordAnalyzer();
    }

    @Override
    protected Analyzer getWrappedAnalyzer(String fieldName) {
        if(fieldName.startsWith(EntityDocumentFieldNames.KEYWORD_FIELD_PREFIX)) {
            return keywordAnalyzer;
        }
        return analyzer;
    }

    private Analyzer createKeywordAnalyzer() {
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

    private Analyzer createAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                                 /// Use whitespace tokenizer here because we use the
                                 // @{WordDelimiterGraphFilterFactory}.  See the javadoc
                                 // for this class
                                 .withTokenizer(WhitespaceTokenizerFactory.NAME)
                                 .addTokenFilter(WordDelimiterGraphFilterFactory.NAME,
                                                 "catenateWords", "1")
                                 .addTokenFilter(FlattenGraphFilterFactory.class)
                                 .addTokenFilter(ASCIIFoldingFilterFactory.NAME)
                                 .addTokenFilter(LowerCaseFilterFactory.NAME)
                                 .addTokenFilter(EdgeNGramFilterFactory.NAME,
                                                 "minGramSize", Integer.toString(minGramSize),
                                                 "maxGramSize", Integer.toString(maxGramSize),
                                                 "preserveOriginal", "true")
                                 .build();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
