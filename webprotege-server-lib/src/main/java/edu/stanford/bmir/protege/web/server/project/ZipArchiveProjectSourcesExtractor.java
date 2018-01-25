package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.util.OntologyDocumentFileFilter;
import edu.stanford.bmir.protege.web.server.util.TempFileFactory;
import edu.stanford.bmir.protege.web.server.util.ZipFileExtractor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class ZipArchiveProjectSourcesExtractor implements RawProjectSourcesExtractor {

    private TempFileFactory tempFileFactory;

    private RootOntologyDocumentFileMatcher rootOntologyDocumentFileMatcher;

    @Inject
    public ZipArchiveProjectSourcesExtractor(TempFileFactory tempFileFactory, RootOntologyDocumentFileMatcher rootOntologyDocumentFileMatcher) {
        this.tempFileFactory = tempFileFactory;
        this.rootOntologyDocumentFileMatcher = rootOntologyDocumentFileMatcher;
    }

    @Override
    public RawProjectSources extractProjectSources(File inputFile) throws IOException {
        return extractZipFile(inputFile);
    }


    private RawProjectSources extractZipFile(File zipFile) throws IOException {
        File tempDirectory = tempFileFactory.createTempDirectory();
        ZipFileExtractor extractor = new ZipFileExtractor();
        extractor.extractFileToDirectory(zipFile, tempDirectory);
        Optional<File> rootOntologyDocument = getRootOntologyDocumentFile(tempDirectory);
        if(!rootOntologyDocument.isPresent()) {
            throw new FileNotFoundException(rootOntologyDocumentFileMatcher.getErrorMessage());
        }
        return new ExtractedZipArchiveProjectSources(rootOntologyDocument.get(), tempDirectory);
    }

    @SuppressWarnings("unchecked")
    private Optional<File> getRootOntologyDocumentFile(File tempDirectory) {
        Collection<File> ontologyDocumentFiles = FileUtils.listFiles(tempDirectory,
                new OntologyDocumentFileFilter(),
                TrueFileFilter.INSTANCE);
        for(File file : ontologyDocumentFiles) {
            if(rootOntologyDocumentFileMatcher.isRootOntologyDocument(file)) {
                return Optional.of(file);
            }
        }
        return Optional.empty();
    }

    private static class ExtractedZipArchiveProjectSources implements RawProjectSources {

        private File rootOntologyDocument;

        private File baseDirectory;

        private ExtractedZipArchiveProjectSources(File rootOntologyDocument, File baseDirectory) {
            this.rootOntologyDocument = rootOntologyDocument;
            this.baseDirectory = baseDirectory;
        }

        @Override
        public Collection<OWLOntologyDocumentSource> getDocumentSources() {
            return Lists.newArrayList(new FileDocumentSource(rootOntologyDocument));
        }

        @Override
        public OWLOntologyIRIMapper getOntologyIRIMapper() {
            return new AutoIRIMapper(baseDirectory, true);
        }

        @Override
        public void cleanUpTemporaryFiles() throws IOException {
            FileUtils.deleteDirectory(baseDirectory);
        }
    }

}
