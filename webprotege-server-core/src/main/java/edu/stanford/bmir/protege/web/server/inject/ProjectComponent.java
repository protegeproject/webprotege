package edu.stanford.bmir.protege.web.server.inject;

import dagger.Subcomponent;
import edu.stanford.bmir.protege.web.server.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.form.EntityFrameFormDataComponent;
import edu.stanford.bmir.protege.web.server.form.EntityFrameFormDataModule;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectModule;
import edu.stanford.bmir.protege.web.server.project.ProjectDisposablesManager;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Oct 2016
 */
@Subcomponent(
        modules = {
                ProjectModule.class,
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

    EntityFrameFormDataComponent getEntityFrameFormDataComponentBuilder(EntityFrameFormDataModule module);

}

