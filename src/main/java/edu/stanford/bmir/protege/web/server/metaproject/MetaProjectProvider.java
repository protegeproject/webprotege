package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.inject.Provider;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Singleton
public class MetaProjectProvider implements Provider<MetaProject> {

    private MetaProjectImpl metaProject;

    @Inject
    public MetaProjectProvider(@MetaProjectURI URI metaProjectURI) {
        metaProject = new MetaProjectImpl(metaProjectURI);
    }

    @Override
    public MetaProject get() {
        return metaProject;
    }
}
