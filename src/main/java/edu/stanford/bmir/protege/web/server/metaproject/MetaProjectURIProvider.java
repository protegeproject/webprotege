package edu.stanford.bmir.protege.web.server.metaproject;

import javax.inject.Provider;

import javax.inject.Inject;
import java.io.File;
import java.net.URI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class MetaProjectURIProvider implements Provider<URI> {

    private final File metaProjectFile;

    @Inject
    public MetaProjectURIProvider(@MetaProjectFile File metaProjectFile) {
        this.metaProjectFile = metaProjectFile;
    }

    @Override
    public URI get() {
        return metaProjectFile.toURI();
    }

}
