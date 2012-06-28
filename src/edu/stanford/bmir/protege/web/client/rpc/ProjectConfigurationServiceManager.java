package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectConfiguration;

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
		proxy = (ProjectConfigurationServiceAsync) GWT.create(ProjectConfigurationService.class);
	}

	public void getProjectConfiguration(String projectName, String userName, AsyncCallback<ProjectConfiguration> cb) {
		proxy.getProjectConfiguration(projectName, userName, cb);
	}

	public void saveProjectConfiguration(String projectName, String userName, ProjectConfiguration config, AsyncCallback<Void> cb) {
		proxy.saveProjectConfiguration(projectName, userName, config, cb);
	}
}
