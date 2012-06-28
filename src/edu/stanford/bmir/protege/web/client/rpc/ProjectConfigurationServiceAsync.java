package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectConfiguration;

public interface ProjectConfigurationServiceAsync {

	void getProjectConfiguration(String projectName, String userName, AsyncCallback<ProjectConfiguration> cb);

	void saveProjectConfiguration(String projectName, String userName, ProjectConfiguration config, AsyncCallback<Void> cb);

}
