package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-08
 */
public class IndexingAnalyzerFactory {

    private final DictionaryLanguage2FieldNameTranslator fieldNameTranslator;

    @Inject
    public IndexingAnalyzerFactory(DictionaryLanguage2FieldNameTranslator fieldNameTranslator) {
        this.fieldNameTranslator = checkNotNull(fieldNameTranslator);
    }

    @Nonnull
    public Analyzer get(List<DictionaryLanguage> dictionaryLanguages) {
        // We have a pair of fields for each language and for all languages
        // One field is for word based tokens and the other field is for
        // ngram based tokens
        var mapBuilder = ImmutableMap.<String, Analyzer>builder();
        // By language, for language specific searching
        dictionaryLanguages.forEach(dl -> {
            var wordFieldName = fieldNameTranslator.getWordFieldName(dl);
            var ngramFieldName = fieldNameTranslator.getEdgeNGramFieldName(dl);
            mapBuilder.put(wordFieldName, createWordAnalyzer());
            mapBuilder.put(ngramFieldName, createEdgeNGramAnalyzer());
        });
        // Catch all, for all languages
        mapBuilder.put("word", createWordAnalyzer());
        mapBuilder.put("ngram", createEdgeNGramAnalyzer());
        return new PerFieldAnalyzerWrapper(createWordAnalyzer(), mapBuilder.build());
    }

    private Analyzer createWordAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                                 .withTokenizer(StandardTokenizerFactory.NAME)
                                 .addTokenFilter(ASCIIFoldingFilterFactory.NAME)
                                 .addTokenFilter(LowerCaseFilterFactory.NAME)
                                 //                                             .addTokenFilter(StopFilterFactory.NAME)
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
                                 .addTokenFilter(EdgeNGramFilterFactory.NAME, "minGramSize", "2", "maxGramSize", "10")
                                 .addTokenFilter(LowerCaseFilterFactory.NAME)
                                 //                                 .addTokenFilter(StopFilterFactory.NAME)
                                 .build();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
