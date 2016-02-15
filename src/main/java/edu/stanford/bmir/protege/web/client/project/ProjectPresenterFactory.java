package edu.stanford.bmir.protege.web.client.project;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/02/16
 */
public interface ProjectPresenterFactory {

    ProjectPresenter createProjectPresenter(ProjectId projectId);
}
