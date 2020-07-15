package edu.stanford.bmir.protege.web.server.shortform;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.DelegatingAnalyzerWrapper;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;

import static edu.stanford.bmir.protege.web.server.shortform.EntityDocumentFieldNames.NGRAM_FIELD_PREFIX;
import static edu.stanford.bmir.protege.web.server.shortform.EntityDocumentFieldNames.WORD_FIELD_PREFIX;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 *
 * Provides analyzers appropriate to the fields used
 */
public class IndexingAnalyzerWrapper extends DelegatingAnalyzerWrapper {

    private final Analyzer wordAnalyzer;
    private final Analyzer edgeNGramAnalyzer;

    @Inject
    public IndexingAnalyzerWrapper() {
        super(PER_FIELD_REUSE_STRATEGY);
        wordAnalyzer = createWordAnalyzer();
        edgeNGramAnalyzer = createEdgeNGramAnalyzer();
    }

    @Override
    protected Analyzer getWrappedAnalyzer(String fieldName) {
        if(fieldName.startsWith(WORD_FIELD_PREFIX)) {
            return wordAnalyzer;
        }
        else if(fieldName.startsWith(NGRAM_FIELD_PREFIX)) {
            return edgeNGramAnalyzer;
        }
        else {
            return wordAnalyzer;
        }
    }

    private Analyzer createWordAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                                 .withTokenizer(StandardTokenizerFactory.NAME)
                                 .addTokenFilter(ASCIIFoldingFilterFactory.NAME)
                                 .addTokenFilter(LowerCaseFilterFactory.NAME)
                                 .build();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Analyzer createEdgeNGramAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                                 .withTokenizer(StandardTokenizerFactory.NAME)
                                 .addTokenFilter(ASCIIFoldingFilterFactory.NAME)
                                 .addTokenFilter(LowerCaseFilterFactory.NAME)
                                 .addTokenFilter(EdgeNGramFilterFactory.NAME, "minGramSize", "2", "maxGramSize", "10")
                                 .build();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
