package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class DataDirectoryProvider implements Provider<File> {

    private WebProtegeProperties webProtegeProperties;

    @Inject
    public DataDirectoryProvider(WebProtegeProperties webProtegeProperties) {
        this.webProtegeProperties = webProtegeProperties;
    }

    @Override
    public File get() {
        return webProtegeProperties.getDataDirectory();
    }
}
