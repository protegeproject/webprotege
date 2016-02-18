package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public interface PerspectiveLinkManager {

    List<PerspectiveId> getPerspectiveLinks(ProjectId projectId, UserId userId);

    boolean addPerspectiveLink(ProjectId projectId, UserId userId, PerspectiveId perspectiveId);

    boolean removePerspectiveLink(ProjectId projectId, UserId userId, PerspectiveId perspectiveId);
}
