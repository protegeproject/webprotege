package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;

@RemoteServiceRelativePath("projectconfig")
public interface ProjectConfigurationService extends RemoteService {

	ProjectLayoutConfiguration getProjectLayoutConfiguration(String projectName, String userName);

	void saveProjectLayoutConfiguration(String projectName, String userName, ProjectLayoutConfiguration config);
}
