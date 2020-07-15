package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.index.ProjectSignatureIndex;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.io.IOException;
import java.io.UncheckedIOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class LuceneIndexWriterImpl implements LuceneIndexWriter, HasDispose {

    private static final Logger logger = LoggerFactory.getLogger(LuceneIndexWriterImpl.class);

    @Nonnull
    private final Directory luceneDirectory;

    @Nonnull
    private final LuceneEntityDocumentTranslator luceneEntityDocumentTranslator;

    @Nonnull
    private final ProjectSignatureIndex projectSignatureIndex;

    @Nonnull
    private final IndexWriter indexWriter;

    @Nonnull
    private final SearcherManager searcherManager;


    @Inject
    public LuceneIndexWriterImpl(@Nonnull Directory luceneDirectory,
                                 @Nonnull LuceneEntityDocumentTranslator luceneEntityDocumentTranslator,
                                 @Nonnull ProjectSignatureIndex projectSignatureIndex,
                                 @Nonnull IndexWriter indexWriter,
                                 @Nonnull SearcherManager searcherManager) {
        this.luceneDirectory = luceneDirectory;
        this.luceneEntityDocumentTranslator = luceneEntityDocumentTranslator;
        this.projectSignatureIndex = checkNotNull(projectSignatureIndex);
        this.indexWriter = indexWriter;
        this.searcherManager = searcherManager;
    }

    @Override
    public void writeIndex() throws IOException {

        if(DirectoryReader.indexExists(luceneDirectory)) {
            logger.info("Lucene directory already exists");
            return;
        }
        var stopwatch = Stopwatch.createStarted();

        projectSignatureIndex.getSignature()
                             .map(luceneEntityDocumentTranslator::getLuceneDocument)
                             .forEach(this::addDocumentToIndex);
        indexWriter.commit();
        searcherManager.maybeRefreshBlocking();
        logger.info("Built lucene based dictionary in {} ms", stopwatch.elapsed().toMillis());
    }

    public void addDocumentToIndex(Document doc) {
        try {
            indexWriter.addDocument(doc);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void dispose() {
        try {
            indexWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
