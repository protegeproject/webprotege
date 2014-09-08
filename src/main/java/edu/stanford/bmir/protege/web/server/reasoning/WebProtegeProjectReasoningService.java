package edu.stanford.bmir.protege.web.server.reasoning;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.reasoning.Action;
import edu.stanford.protege.reasoning.KbId;
import edu.stanford.protege.reasoning.Response;
import edu.stanford.protege.reasoning.action.ActionHandler;
import edu.stanford.protege.reasoning.action.KbAction;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 04/09/2014
 */
public class WebProtegeProjectReasoningService {

    private ProjectId projectId;

    private KbId kbId;

    public WebProtegeProjectReasoningService(ProjectId projectId) {
        this.projectId = projectId;
        this.kbId = new KbId(projectId.getId());
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public KbId getKbId() {
        return kbId;
    }

    public <A extends Action<R, H>, R extends Response, H extends ActionHandler<A, R>> void execute(KbAction<R, H> action) {
        if(!action.getKbId().equals(kbId)) {
            throw new RuntimeException("Invalid KbId for project (" + kbId + " for " + projectId + ")");
        }

    }
}
