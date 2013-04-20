package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public interface ProjectConfigurationServiceAsync {

	void getProjectLayoutConfiguration(ProjectId projectId, UserId userId, AsyncCallback<ProjectLayoutConfiguration> cb);

	void saveProjectLayoutConfiguration(ProjectId projectId, UserId userId, ProjectLayoutConfiguration config, AsyncCallback<Void> cb);

}
