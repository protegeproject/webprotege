package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;

public interface SharingSettingsServiceAsync {

    void getProjectSharingSettings(ProjectId projectId, AsyncCallback<ProjectSharingSettings> async);

    void updateSharingSettings(ProjectSharingSettings projectSharingSettings, AsyncCallback<Void> async);
}