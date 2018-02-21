package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.inject.ProjectComponent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
public interface ProjectComponentFactory {

    @Nonnull
    ProjectComponent createProjectComponent(@Nonnull ProjectId projectId);

}
