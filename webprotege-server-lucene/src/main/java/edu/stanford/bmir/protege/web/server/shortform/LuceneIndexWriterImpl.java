package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.index.ProjectSignatureIndex;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.MMapDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class LuceneIndexWriterImpl implements LuceneIndexWriter {

    private static final Logger logger = LoggerFactory.getLogger(LuceneIndexWriterImpl.class);

    @Nonnull
    private final ProjectLuceneDirectoryPathSupplier projectLuceneDirectoryPathSupplier;

    @Nonnull
    private final LuceneEntityDocumentTranslator luceneEntityDocumentTranslator;

    @Nonnull
    private final ProjectSignatureIndex projectSignatureIndex;

    @Nonnull
    private final IndexingAnalyzerFactory indexingAnalyzerFactory;

    @Nonnull
    private final SearchableLanguagesManager searchableLanguagesManager;


    @Inject
    public LuceneIndexWriterImpl(@Nonnull ProjectLuceneDirectoryPathSupplier projectLuceneDirectoryPathSupplier,
                                 @Nonnull LuceneEntityDocumentTranslator luceneEntityDocumentTranslator,
                                 @Nonnull ProjectSignatureIndex projectSignatureIndex,
                                 @Nonnull IndexingAnalyzerFactory indexingAnalyzerFactory,
                                 @Nonnull SearchableLanguagesManager searchableLanguagesManager) {
        this.projectLuceneDirectoryPathSupplier = projectLuceneDirectoryPathSupplier;
        this.luceneEntityDocumentTranslator = luceneEntityDocumentTranslator;
        this.projectSignatureIndex = checkNotNull(projectSignatureIndex);
        this.indexingAnalyzerFactory = indexingAnalyzerFactory;
        this.searchableLanguagesManager = searchableLanguagesManager;
    }

    @Override
    public void writeIndex() throws IOException {

        var stopwatch = Stopwatch.createStarted();

        var directoryPath = projectLuceneDirectoryPathSupplier.get();

        if (Files.exists(directoryPath)) {
            logger.info("Lucene based dictionary index already exists");
//            FileUtils.deleteDirectory(directoryPath.toFile());
            return;
        }

        var directory = new MMapDirectory(directoryPath);

        var searchableLanguages = searchableLanguagesManager.getSearchableLanguages();
        var analyzer = indexingAnalyzerFactory.get(searchableLanguages);
        var indexWriterConfig = new IndexWriterConfig(analyzer);
        var indexWriter = new IndexWriter(directory, indexWriterConfig);
        try(indexWriter) {
            projectSignatureIndex.getSignature()
                                 .map(luceneEntityDocumentTranslator::getLuceneDocument)
                                 .forEach(doc -> addDocumentToIndex(indexWriter, doc));
        }
        logger.info("Built lucene based dictionary in {} ms", stopwatch.elapsed().toMillis());
    }

    public void addDocumentToIndex(IndexWriter indexWriter, Document doc) {
        try {
            indexWriter.addDocument(doc);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
