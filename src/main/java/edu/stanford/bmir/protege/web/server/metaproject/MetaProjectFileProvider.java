package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.server.inject.DataDirectory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class MetaProjectFileProvider implements Provider<File> {

    private static final String METAPROJECT_DIRECTORY = "metaproject";

    private static final String METAPROJECT_PPRJ_FILE_NAME = "metaproject.pprj";

    private final File dataDirectory;

    @Inject
    public MetaProjectFileProvider(@DataDirectory File dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    public File get() {
        return getMetaProjectFile();
    }


    private File getMetaProjectFile() {
        File metaProjectDirectory = new File(dataDirectory, METAPROJECT_DIRECTORY);
        return new File(metaProjectDirectory, METAPROJECT_PPRJ_FILE_NAME);
    }

}
