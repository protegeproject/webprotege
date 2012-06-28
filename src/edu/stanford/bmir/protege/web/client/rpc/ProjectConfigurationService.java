package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectConfiguration;

@RemoteServiceRelativePath("projectconfig")
public interface ProjectConfigurationService extends RemoteService {

	public ProjectConfiguration getProjectConfiguration(String projectName, String userName);

	public void saveProjectConfiguration(String projectName, String userName, ProjectConfiguration config);

}
