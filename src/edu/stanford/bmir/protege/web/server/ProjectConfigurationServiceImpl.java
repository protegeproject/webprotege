package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.stanford.bmir.protege.web.client.rpc.ProjectConfigurationService;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class ProjectConfigurationServiceImpl extends RemoteServiceServlet implements ProjectConfigurationService {

    public ProjectLayoutConfiguration getProjectLayoutConfiguration(ProjectId projectId, UserId userId) {
        UIConfigurationManager configurationManager = UIConfigurationManager.get();
        return configurationManager.getProjectLayoutConfiguration(projectId, userId);
    }


    public void saveProjectLayoutConfiguration(ProjectId projectId, UserId userId, ProjectLayoutConfiguration config) {
        UIConfigurationManager.get().saveProjectLayoutConfiguration(projectId, userId, config);
    }
}
