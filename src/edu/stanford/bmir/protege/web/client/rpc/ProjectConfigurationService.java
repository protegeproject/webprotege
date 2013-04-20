package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

@RemoteServiceRelativePath("projectconfig")
public interface ProjectConfigurationService extends RemoteService {

	ProjectLayoutConfiguration getProjectLayoutConfiguration(ProjectId projectId, UserId userId);

	void saveProjectLayoutConfiguration(ProjectId projectId, UserId userId, ProjectLayoutConfiguration config);
}
