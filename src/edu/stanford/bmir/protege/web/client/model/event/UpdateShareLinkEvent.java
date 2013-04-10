package edu.stanford.bmir.protege.web.client.model.event;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class UpdateShareLinkEvent implements SystemEvent {
    private boolean showShareLink;

    private final ProjectId currentSelectedProject;

    public UpdateShareLinkEvent(boolean showShareLink, ProjectId currentSelectedProject) {
        this.showShareLink = showShareLink;
        this.currentSelectedProject = currentSelectedProject;
    }

    /**
     * @return the showShareLink
     */
    public boolean isShowShareLink() {
        return showShareLink;
    }

    /**
     * @return the currentSelectedProject
     */
    public ProjectId getCurrentSelectedProject() {
        return currentSelectedProject;
    }
}

