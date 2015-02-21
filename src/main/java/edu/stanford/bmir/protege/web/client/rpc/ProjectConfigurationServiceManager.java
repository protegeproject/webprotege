package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

public class ProjectConfigurationServiceManager {

	private static ProjectConfigurationServiceAsync proxy;
	static ProjectConfigurationServiceManager instance;

	public static ProjectConfigurationServiceManager getInstance() {
		if (instance == null) {
			instance = new ProjectConfigurationServiceManager();
		}
		return instance;
	}

	private ProjectConfigurationServiceManager() {
		proxy = GWT.create(ProjectConfigurationService.class);
	}

	public void saveProjectConfiguration(ProjectId projectId, UserId userId,  ProjectLayoutConfiguration config, AsyncCallback<Void> cb) {
		proxy.saveProjectLayoutConfiguration(projectId, userId, config, cb);
	}

}
