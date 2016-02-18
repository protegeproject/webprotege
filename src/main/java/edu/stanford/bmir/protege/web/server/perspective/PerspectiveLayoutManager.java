package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public interface PerspectiveLayoutManager {

    PerspectiveLayout getPerspectiveLayout(ProjectId projectId, UserId userId, PerspectiveId perspectiveId);

    void setPerspectiveLayout(ProjectId projectId, UserId userId, PerspectiveLayout layout);

    void clearPerspectiveLayout(ProjectId projectId, UserId userId);

}
