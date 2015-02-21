package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

@RemoteServiceRelativePath("projectconfig")
public interface ProjectConfigurationService extends RemoteService {

	void saveProjectLayoutConfiguration(ProjectId projectId, UserId userId, ProjectLayoutConfiguration config);
}
