package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;

public interface SharingSettingsServiceAsync {

    void getProjectSharingSettings(ProjectId projectId, AsyncCallback<ProjectSharingSettings> async);

    void updateSharingSettings(ProjectSharingSettings projectSharingSettings, AsyncCallback<Void> async);
}