package edu.stanford.bmir.protege.web.server.shortform;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectSignatureIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.semanticweb.owlapi.model.OWLDataFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-08
 */
@Module
public class LuceneModule {

    private ProjectOntologiesIndex projectOntologiesIndex;

    private AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex;

    private ProjectSignatureIndex provideProjectSignatureIndex;

    public LuceneModule(ProjectOntologiesIndex projectOntologiesIndex,
                        AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex,
                        ProjectSignatureIndex provideProjectSignatureIndex) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.annotationAssertionAxiomsBySubjectIndex = annotationAssertionAxiomsBySubjectIndex;
        this.provideProjectSignatureIndex = provideProjectSignatureIndex;
    }

    @Provides
    @ProjectSingleton
    public ProjectOntologiesIndex provideProjectOntologiesIndex() {
        return projectOntologiesIndex;
    }

    @Provides
    @ProjectSingleton
    AnnotationAssertionAxiomsBySubjectIndex provideAnnotationAssertionAxiomsBySubjectIndex() {
        return annotationAssertionAxiomsBySubjectIndex;
    }

    @Provides
    @ProjectSingleton
    public ProjectSignatureIndex getProvideProjectSignatureIndex() {
        return provideProjectSignatureIndex;
    }

    @ProjectSingleton
    @Provides
    public OWLDataFactory provideOWLDataFactory() {
        return new OWLDataFactoryImpl();
    }

    @Provides
    @ProjectSingleton
    public DictionaryLanguage2FieldNameTranslator provideDictionaryLanguage2FieldNameTranslator(DictionaryLanguage2FieldNameTranslatorImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    LuceneEntityDocumentTranslator provideLuceneEntityDocumentTranslator(LuceneEntityDocumentTranslatorImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    LuceneIndex provideLuceneIndex(LuceneIndexImpl impl,
                                   LuceneIndexWriter writer) {
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
        return new IndexWriterConfig(analyzer);
    }

    @ProjectSingleton
    @Provides
    IndexWriter provideIndexWriter(Directory directory,
                                   IndexWriterConfig indexWriterConfig) {
        try {
            return new IndexWriter(directory, indexWriterConfig);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @ProjectSingleton
    @Provides
    SearcherManager provideSearcherManager(IndexWriter indexWriter, SearcherFactory searcherFactory) {
        try {
            return new SearcherManager(indexWriter, searcherFactory);
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
}
