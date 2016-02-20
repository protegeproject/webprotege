package edu.stanford.bmir.protege.web.client.ui.projectlist;

import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.client.ui.UIAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public class ProjectDetailsPresenter {

    private final ProjectDetailsView view;

    private final ProjectDetails details;

    @Inject
    public ProjectDetailsPresenter(@Assisted ProjectDetails details, ProjectDetailsView view) {
        this.view = view;
        this.details = details;
        view.setProjectName(details.getDisplayName());
        view.setProjectOwner(details.getOwner());
        view.setDescription(details.getDescription());
    }

    public ProjectId getProjectId() {
        return details.getProjectId();
    }

    public UserId getOwner() {
        return details.getOwner();
    }

    public ProjectDetailsView getView() {
        return view;
    }

    public void addAction(UIAction action) {
        view.addAction(action);
    }


}
