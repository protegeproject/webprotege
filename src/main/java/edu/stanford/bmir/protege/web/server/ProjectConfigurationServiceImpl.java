package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.ProjectConfigurationService;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.project.UIConfigurationManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

public class ProjectConfigurationServiceImpl extends WebProtegeRemoteServiceServlet implements ProjectConfigurationService {

    private final UIConfigurationManager configurationManager = WebProtegeInjector.get().getInstance(UIConfigurationManager.class);

    public void saveProjectLayoutConfiguration(ProjectId projectId, UserId userId, ProjectLayoutConfiguration config) {
        configurationManager.saveProjectLayoutConfiguration(projectId, userId, config);
    }
}
