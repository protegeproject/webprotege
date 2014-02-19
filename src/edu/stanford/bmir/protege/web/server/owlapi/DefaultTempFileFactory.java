package edu.stanford.bmir.protege.web.server.owlapi;

import java.io.File;
import java.io.IOException;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public class DefaultTempFileFactory implements TempFileFactory {

    private static final String PREFIX = "webprotege";

    private static final String SUFFIX = "tempfile";

    @Override
    public File createTempDirectory() throws IOException {
        File file = File.createTempFile(PREFIX, SUFFIX);
        file.mkdirs();
        return file;
    }
}
