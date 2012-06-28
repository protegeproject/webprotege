package edu.stanford.bmir.protege.web.client.model.event;

public class UpdateShareLinkEvent implements SystemEvent {
    private boolean showShareLink;

    private String currentSelectedProject;

    public UpdateShareLinkEvent(boolean showShareLink, String currentSelectedProject) {
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
    public String getCurrentSelectedProject() {
        return currentSelectedProject;
    }
}

