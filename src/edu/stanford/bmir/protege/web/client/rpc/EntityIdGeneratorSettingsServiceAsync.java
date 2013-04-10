package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityIdGeneratorSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public interface EntityIdGeneratorSettingsServiceAsync {

    void getSettings(ProjectId projectId, AsyncCallback<EntityIdGeneratorSettings> async);

    void setSettings(ProjectId projectId, EntityIdGeneratorSettings settings, AsyncCallback<Void> async);
}
