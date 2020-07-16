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

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 *
 * Provides analyzers appropriate to the fields used
 */
public class IndexingAnalyzerWrapper extends DelegatingAnalyzerWrapper {

    private final Analyzer analyzer;

    @Inject
    public IndexingAnalyzerWrapper() {
        super(PER_FIELD_REUSE_STRATEGY);
        analyzer = createAnalyzer();
    }

    @Override
    protected Analyzer getWrappedAnalyzer(String fieldName) {
        return analyzer;
    }


    private Analyzer createAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                                 .withTokenizer(StandardTokenizerFactory.NAME)
                                 .addTokenFilter(ASCIIFoldingFilterFactory.NAME)
                                 .addTokenFilter(LowerCaseFilterFactory.NAME)
                                 .addTokenFilter(EdgeNGramFilterFactory.NAME,
                                                 "minGramSize", "2",
                                                 "maxGramSize", "10",
                                                 "preserveOriginal", "true")
                                 .build();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
