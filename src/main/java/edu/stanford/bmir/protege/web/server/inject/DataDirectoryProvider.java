package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.WebProtegeFileStore;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class DataDirectoryProvider implements Provider<File> {

    @Override
    public File get() {
        return WebProtegeFileStore.getInstance().getDataDirectory();
    }
}
