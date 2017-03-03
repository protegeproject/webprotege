package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectAction;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class LoadProjectActionHandler implements ActionHandler<LoadProjectAction, LoadProjectResult> {

    private final ProjectDetailsManager projectDetailsManager;

    private final OWLAPIProjectManager projectManager;

    private final AccessManager accessManager;

    private final WebProtegeLogger webProtegeLogger;

    @Inject
    public LoadProjectActionHandler(ProjectDetailsManager projectDetailsManager,
                                    OWLAPIProjectManager projectManager,
                                    WebProtegeLogger webProtegeLogger,
                                    AccessManager accessManager) {
        this.projectDetailsManager = projectDetailsManager;
        this.accessManager = accessManager;
        this.webProtegeLogger = webProtegeLogger;
        this.projectManager = projectManager;
    }

    @Override
    public Class<LoadProjectAction> getActionClass() {
        return LoadProjectAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(LoadProjectAction action, RequestContext requestContext) {
        return new ProjectPermissionValidator(accessManager, action.getProjectId(), requestContext.getUserId(), VIEW_PROJECT
                .getActionId());
    }

    @Override
    public LoadProjectResult execute(final LoadProjectAction action, ExecutionContext executionContext) {
        // Load project in parallel (as we don't return it, but want it ready for further calls).
        Stopwatch stopwatch = Stopwatch.createStarted();
        webProtegeLogger.info("Loading project: " + action.getProjectId());
        projectManager.getProject(action.getProjectId(), executionContext.getUserId());
        stopwatch.stop();
        webProtegeLogger.info(".... loaded project in %s ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        final ProjectId projectId = action.getProjectId();//project.getProjectId();

        ProjectDetails projectDetails = projectDetailsManager.getProjectDetails(projectId);

        return new LoadProjectResult(action.getProjectId(), executionContext.getUserId(), projectDetails);
    }
}
