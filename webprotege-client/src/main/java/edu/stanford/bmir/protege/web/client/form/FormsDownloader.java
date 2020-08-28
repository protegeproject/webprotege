package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
public class FormsDownloader {

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public FormsDownloader(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public void download() {
        String baseURL = GWT.getHostPageBaseURL() + "data/";
        String downloadURL = baseURL + "projects/" + projectId.getId() + "/forms";
        Window.open(downloadURL, "Download forms", "");
    }
}
