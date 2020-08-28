package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.http.client.*;
import com.google.gwt.resources.client.DataResource;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public class ProjectSettingsService {

    private final DispatchServiceManager dispatch;

    private final ProjectId projectId;

    @Inject
    public ProjectSettingsService(DispatchServiceManager dispatch, ProjectId projectId) {
        this.dispatch = dispatch;
        this.projectId = projectId;
    }


    public void importSettings(@Nonnull String settingsToImportJson,
                               @Nonnull Runnable importSuccessfulHandler,
                               @Nonnull Runnable importErrorHandler) {
        try {
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, "/data/projects/" + projectId.getId() + "/settings");

            requestBuilder.setRequestData(settingsToImportJson);
            requestBuilder.setHeader("Content-Type", "application/json");
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if(response.getStatusCode() == Response.SC_OK) {
                        importSuccessfulHandler.run();
                    }
                    else {
                        importErrorHandler.run();
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    importErrorHandler.run();
                }
            });
            requestBuilder.send();
        } catch (RequestException e) {
            importErrorHandler.run();
        }
    }

}
