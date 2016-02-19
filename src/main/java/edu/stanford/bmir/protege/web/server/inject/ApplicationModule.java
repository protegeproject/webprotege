package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveLayoutManager;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveLayoutManagerImpl;
import edu.stanford.bmir.protege.web.server.perspective.PerspectivesManager;
import edu.stanford.bmir.protege.web.server.perspective.PerspectivesManagerImpl;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PerspectiveLayoutManager.class).to(PerspectiveLayoutManagerImpl.class).asEagerSingleton();
        bind(PerspectivesManager.class).to(PerspectivesManagerImpl.class).asEagerSingleton();
    }
}
