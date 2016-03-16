package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public interface PerspectivesManager {

    ImmutableList<PerspectiveId> getPerspectives(ProjectId projectId, UserId userId);

    void setPerspectives(ProjectId projectId, UserId userId, List<PerspectiveId> perspectives);
}