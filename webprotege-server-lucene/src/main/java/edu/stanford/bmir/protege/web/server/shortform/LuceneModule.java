package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.project.ProjectDisposablesManager;
import edu.stanford.bmir.protege.web.server.util.DisposableObjectManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Min;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-08
 */
@Module
public class LuceneModule {

    private static final Logger logger = LoggerFactory.getLogger(LuceneModule.class);

    public static final int MIN_GRAM_SIZE = 2;

    public static final int MAX_GRAM_SIZE = 11;

    @Provides
    @ProjectSingleton
    public FieldNameTranslator provideDictionaryLanguage2FieldNameTranslator(FieldNameTranslatorImpl impl) {
        return impl;
    }

    @Provides
    LuceneEntityDocumentTranslator provideLuceneEntityDocumentTranslator(LuceneEntityDocumentTranslatorImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    LuceneIndex provideLuceneIndex(LuceneIndexImpl impl,
                                   // Not this is needed here to force an initial write of the index
                                   LuceneIndexWriter indexWriter) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    LuceneIndexWriter provideLuceneIndexWriter(LuceneIndexWriterImpl impl) {
        try {
            impl.writeIndex();
            return impl;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @ProjectSingleton
    @Provides
    MultiLingualShortFormDictionary provideMultiLingualShortFormDictionary(MultiLingualShortFormDictionaryLuceneImpl impl) {
        return impl;
    }

    @ProjectSingleton
    @Provides
    SearchableMultiLingualShortFormDictionary provideSearchableMultiLingualShortFormDictionary(SearchableMultiLingualShortFormDictionaryLuceneImpl impl) {
        return impl;
    }

    @ProjectSingleton
    @Provides
    MultiLingualDictionary provideMultiLingualDictionary(MultiLingualDictionaryLuceneImpl impl) {
        return impl;
    }

    @ProjectSingleton
    @Provides
    Directory provideDirectory(ProjectLuceneDirectoryPathSupplier pathSupplier) {
        try {
            // FSDirectory.open chooses the best implementation for the platform
            return FSDirectory.open(pathSupplier.get());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Provides
    IndexWriterConfig provideIndexWriterConfig(IndexingAnalyzerFactory analyzerFactory) {
        var analyzer = analyzerFactory.get();
        var config = new IndexWriterConfig(analyzer);
        return config.setSimilarity(new EntityBasedSimilarity());
    }

    @Provides
    @MinGramSize
    int provideMinGramSize() {
        return MIN_GRAM_SIZE;
    }

    @Provides
    @MaxGramSize
    int provideMaxGramSize() {
        return MAX_GRAM_SIZE;
    }

    @ProjectSingleton
    @Provides
    IndexWriter provideIndexWriter(Directory directory,
                                   IndexWriterConfig indexWriterConfig,
                                   ProjectDisposablesManager projectDisposablesManager,
                                   ProjectId projectId) {
        try {
            var indexWriter = new IndexWriter(directory, indexWriterConfig);
            projectDisposablesManager.register(() -> {
                try {
                    indexWriter.close();
                    logger.info("{} Closed lucene index writer", projectId);
                } catch (IOException e) {
                    logger.error("Error when disposing of Project Lucene IndexWriter", e);
                }
            });

            return indexWriter;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @ProjectSingleton
    @Provides
    SearcherManager provideSearcherManager(IndexWriter indexWriter,
                                           SearcherFactory searcherFactory,
                                           DisposableObjectManager disposableObjectManager) {
        try {
            var searchManager = new SearcherManager(indexWriter, searcherFactory);
            disposableObjectManager.register(() -> {
                try {
                    searchManager.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return searchManager;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Provides
    SearcherFactory provideSearcherFactory() {
        return new SearcherFactory();
    }

    @Provides
    LuceneIndexUpdater provideLuceneIndexUpdater(LuceneIndexUpdaterImpl impl) {
        return impl;
    }

    @Provides
    MultilingualDictionaryUpdater provideMultilingualDictionaryUpdater(LuceneMultiLingualDictionaryUpdater impl) {
        return impl;
    }

    @Provides
    MultiLingualShortFormIndex provideMultiLingualShortFormIndex(MultiLingualShortFormIndexLuceneImpl impl) {
        return impl;
    }

    @Provides
    ImmutableList<EntitySearchFilterMatcher> provideSearchFilterMatchers(EntitySearchFilterMatchersFactory factory) {
        return factory.getSearchFilterMatchers();
    }
}
