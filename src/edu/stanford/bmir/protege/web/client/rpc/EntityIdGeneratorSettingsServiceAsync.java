package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityIdGeneratorSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;

public interface EntityIdGeneratorSettingsServiceAsync {

    void getSettings(ProjectId projectId, AsyncCallback<EntityIdGeneratorSettings> async);

    void setSettings(ProjectId projectId, EntityIdGeneratorSettings settings, AsyncCallback<Void> async);
}
