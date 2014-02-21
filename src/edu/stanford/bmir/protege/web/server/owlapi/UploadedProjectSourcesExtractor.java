package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.util.ZipInputStreamChecker;

import java.io.File;
import java.io.IOException;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class UploadedProjectSourcesExtractor implements RawProjectSourcesExtractor {

    private ZipInputStreamChecker zipInputStreamChecker;

    private ZipArchiveProjectSourcesExtractor zipArchiveProjectSourcesExtractor;

    private SingleDocumentProjectSourcesExtractor singleDocumentProjectSourcesExtractor;

    public UploadedProjectSourcesExtractor(ZipInputStreamChecker zipInputStreamChecker,
                                           ZipArchiveProjectSourcesExtractor zipArchiveProjectSourcesExtractor,
                                           SingleDocumentProjectSourcesExtractor
                                                   singleDocumentProjectSourcesExtractor) {
        this.zipInputStreamChecker = zipInputStreamChecker;
        this.zipArchiveProjectSourcesExtractor = zipArchiveProjectSourcesExtractor;
        this.singleDocumentProjectSourcesExtractor = singleDocumentProjectSourcesExtractor;
    }

    @Override
    public RawProjectSources extractProjectSources(File inputFile) throws IOException {
        if (zipInputStreamChecker.isZipFile(inputFile)) {
            return zipArchiveProjectSourcesExtractor.extractProjectSources(inputFile);
        }
        else {
            return singleDocumentProjectSourcesExtractor.extractProjectSources(inputFile);
        }
    }
}
