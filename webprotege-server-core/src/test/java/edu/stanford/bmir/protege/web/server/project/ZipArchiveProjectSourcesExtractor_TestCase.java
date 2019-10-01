package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.util.TempFileFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;

import java.io.*;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static edu.stanford.bmir.protege.web.server.project.FileDocumentSourceMatcher.isFileDocumentSourceForFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class ZipArchiveProjectSourcesExtractor_TestCase {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private TempFileFactory tempFileFactory;

    @Mock
    private RootOntologyDocumentFileMatcher rootOntologyDocumentFileMatcher;

    private File outputFolder;

    @Before
    public void setUp() throws IOException {
        outputFolder = temporaryFolder.newFolder();
        when(tempFileFactory.createTempDirectory()).thenReturn(outputFolder);

    }

    @Test
    public void shouldExtractZipFile() throws IOException {
        String document = "/ontologies/root-ontology.owl";
        File expectedDocumentFile =  new File(outputFolder, document);
        when(rootOntologyDocumentFileMatcher.isRootOntologyDocument(expectedDocumentFile)).thenReturn(true);
        File zipFile = createZipFile(document);
        ZipArchiveProjectSourcesExtractor extractor = new ZipArchiveProjectSourcesExtractor(tempFileFactory, rootOntologyDocumentFileMatcher);
        RawProjectSources projectSources = extractor.extractProjectSources(zipFile);
        Collection<OWLOntologyDocumentSource> documentSources = projectSources.getDocumentSources();
        assertThat(documentSources, hasSize(1));
        assertThat(documentSources, hasItem(isFileDocumentSourceForFile(expectedDocumentFile)));
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundExceptionForMissingRootOntology() throws IOException {
        String document = "/ontologies/ont.owl";
        File zipFile = createZipFile(document);
        ZipArchiveProjectSourcesExtractor extractor = new ZipArchiveProjectSourcesExtractor(tempFileFactory, rootOntologyDocumentFileMatcher);
        extractor.extractProjectSources(zipFile);
    }


    public File createZipFile(String document) throws IOException {
        File zipFile = temporaryFolder.newFile();
        OutputStream out = new FileOutputStream(zipFile);
        ZipOutputStream zipOutputStream = new ZipOutputStream(out);
        ZipEntry entryA = new ZipEntry(document);
        entryA.setSize(1);
        zipOutputStream.putNextEntry(entryA);
        zipOutputStream.write(1);
        zipOutputStream.close();
        return zipFile;
    }

}
