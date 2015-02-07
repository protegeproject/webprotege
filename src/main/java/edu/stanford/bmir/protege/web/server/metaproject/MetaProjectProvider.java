package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.inject.Provider;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;

import javax.inject.Inject;
import java.net.URI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class MetaProjectProvider implements Provider<MetaProject> {

    private URI metaProjectURI;

    @Inject
    public MetaProjectProvider(@MetaProjectURI URI metaProjectURI) {
        this.metaProjectURI = metaProjectURI;
    }

    @Override
    public MetaProject get() {
        return new MetaProjectImpl(metaProjectURI);
    }
}
