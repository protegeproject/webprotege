package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public class ProjectSettingsDownloader {

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public ProjectSettingsDownloader(@Nonnull ProjectId projectId) {
        this.projectId = projectId;
    }

    public void download() {
        String baseURL = GWT.getHostPageBaseURL() + "data/";
        String downloadURL = baseURL + "projects/" + projectId.getId() + "/settings";
        Window.open(downloadURL, "Project settings", "");
    }
}
