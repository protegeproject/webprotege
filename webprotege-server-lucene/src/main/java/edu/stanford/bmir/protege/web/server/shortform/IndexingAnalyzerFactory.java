package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.apache.lucene.analysis.Analyzer;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-08
 */
@ProjectSingleton
public class IndexingAnalyzerFactory {

    private final Provider<IndexingAnalyzerWrapper> indexingAnalyzerWrapperProvider;

    @Inject
    public IndexingAnalyzerFactory(Provider<IndexingAnalyzerWrapper> indexingAnalyzerWrapperProvider) {
        this.indexingAnalyzerWrapperProvider = checkNotNull(indexingAnalyzerWrapperProvider);
    }

    @Nonnull
    public Analyzer get() {
        return indexingAnalyzerWrapperProvider.get();
    }
}
