package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.util.ZipInputStreamChecker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class UploadedProjectSourcesExtractor_TestCase {

    @Mock
    private ZipArchiveProjectSourcesExtractor zipArchiveProjectSourcesExtractor;

    @Mock
    private SingleDocumentProjectSourcesExtractor singleDocumentProjectSourcesExtractor;

    @Mock
    private ZipInputStreamChecker zipInputStreamChecker;

    @Mock
    private File inputFile;


    @Test
    public void shouldExtractSourcesFromZipFile() throws IOException {
        // Given
        when(zipInputStreamChecker.isZipFile(inputFile)).thenReturn(true);
        UploadedProjectSourcesExtractor extractor = new UploadedProjectSourcesExtractor(
                zipInputStreamChecker,
                zipArchiveProjectSourcesExtractor,
                singleDocumentProjectSourcesExtractor
        );
        // When
        extractor.extractProjectSources(inputFile);
        // Then
        verify(zipArchiveProjectSourcesExtractor, times(1)).extractProjectSources(inputFile);
    }

    @Test
    public void shouldExtractNonZipFileUsingSingleDocumentExtractor() throws IOException {
        // Given
        when(zipInputStreamChecker.isZipFile(inputFile)).thenReturn(false);
        UploadedProjectSourcesExtractor extractor = new UploadedProjectSourcesExtractor(
                zipInputStreamChecker,
                zipArchiveProjectSourcesExtractor,
                singleDocumentProjectSourcesExtractor
        );
        // When
        extractor.extractProjectSources(inputFile);
        // Then
        verify(singleDocumentProjectSourcesExtractor, times(1)).extractProjectSources(inputFile);
    }


}
