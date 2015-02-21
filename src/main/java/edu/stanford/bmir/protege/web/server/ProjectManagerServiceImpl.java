package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.owlapi.*;
import edu.stanford.bmir.protege.web.shared.project.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public class ProjectManagerServiceImpl extends WebProtegeRemoteServiceServlet implements ProjectManagerService {


    private static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(ProjectManagerServiceImpl.class);

    private ProjectDetailsManager projectDetailsManager;

    public ProjectManagerServiceImpl() {
        projectDetailsManager = WebProtegeInjector.get().getInstance(ProjectDetailsManager.class);
    }

    public synchronized boolean isRegisteredProject(ProjectId projectId) {
        if (projectId == null) {
            throw new NullPointerException("projectId must not be null");
        }
        return MetaProjectManager.getManager().isExistingProject(projectId);
    }

    public synchronized ProjectDetails createNewProject(NewProjectSettings newProjectSettings) throws NotSignedInException, ProjectAlreadyRegisteredException, ProjectDocumentExistsException {
        checkNotNull(newProjectSettings);

        ensureSignedIn();

        OWLAPIProjectManager pm = OWLAPIProjectManager.getProjectManager();
        OWLAPIProject project = pm.createNewProject(newProjectSettings);
        ProjectId projectId = project.getProjectId();
        if (!isRegisteredProject(projectId)) {
            getMetaProjectManager().registerProject(projectId, newProjectSettings);
            applyDefaultSharingSettings(projectId);
            LOGGER.info("Created new project: %s", newProjectSettings.toString());
        }
        return getMetaProjectManager().getProjectDetails(projectId);
    }

    /**
     * Applies the default sharing setting to a project.  The default sharing settings are that the project is private,
     * but the signed in user is an editor.
     * @param projectId The project id that identifies the project to apply sharing settings to.
     */
    private void applyDefaultSharingSettings(ProjectId projectId) {
        List<UserSharingSetting> userSharingSettings = new ArrayList<UserSharingSetting>();
        UserId userInSession = getUserInSession();
        if (!userInSession.isGuest()) {
            userSharingSettings.add(new UserSharingSetting(userInSession, SharingSetting.EDIT));
        }
        ProjectSharingSettings sharingSettings = new ProjectSharingSettings(projectId, SharingSetting.NONE, userSharingSettings);
        getMetaProjectManager().setProjectSharingSettings(sharingSettings);
    }

}
