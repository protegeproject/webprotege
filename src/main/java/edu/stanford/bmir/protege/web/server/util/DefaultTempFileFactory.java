package edu.stanford.bmir.protege.web.server.util;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public class DefaultTempFileFactory implements TempFileFactory {

    @Override
    public File createTempDirectory() throws IOException {
        File systemTempDirectory = FileUtils.getTempDirectory();
        File result = new File(systemTempDirectory, "tmp-" + UUID.randomUUID().toString());
        result.mkdirs();
        return result;
    }
}
