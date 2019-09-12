package edu.stanford.bmir.protege.web.server.inject;

import dagger.Subcomponent;
import edu.stanford.bmir.protege.web.server.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.index.IndexModule;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectModule;
import edu.stanford.bmir.protege.web.server.project.ProjectDisposablesManager;
import edu.stanford.bmir.protege.web.server.project.chg.OntologyStoreModule;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Oct 2016
 */
@Subcomponent(
        modules = {
                ProjectModule.class,
                OntologyStoreModule.class
        }
)
@ProjectSingleton
public interface ProjectComponent {

    EagerProjectSingletons init();

    ProjectId getProjectId();

    EventManager<ProjectEvent<?>> getEventManager();

    ProjectDisposablesManager getDisposablesManager();

    ProjectActionHandlerRegistry getActionHandlerRegistry();

    RevisionManager getRevisionManager();
}

