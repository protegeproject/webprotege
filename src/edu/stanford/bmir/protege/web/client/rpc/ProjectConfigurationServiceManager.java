package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;

import java.util.List;

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

	public void getProjectConfiguration(String projectName, String userName, AsyncCallback<ProjectLayoutConfiguration> cb) {
		proxy.getProjectLayoutConfiguration(projectName, userName, cb);
	}

	public void saveProjectConfiguration(String projectName, String userName, ProjectLayoutConfiguration config, AsyncCallback<Void> cb) {
		proxy.saveProjectLayoutConfiguration(projectName, userName, config, cb);
	}

}
