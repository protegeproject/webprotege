package edu.stanford.bmir.protege.web.client.ui.projectlist;

import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/16
 */
public interface ProjectDetailPresenterFactory {

    ProjectDetailsPresenter createPresenter(ProjectDetails projectDetails);
}
