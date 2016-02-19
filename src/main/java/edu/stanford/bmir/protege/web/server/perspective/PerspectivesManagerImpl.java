package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class PerspectivesManagerImpl implements PerspectivesManager {

    private List<PerspectiveId> perspectiveIds;

    public PerspectivesManagerImpl() {
        perspectiveIds = new ArrayList<>(Arrays.asList(
                new PerspectiveId("Classes"),
                new PerspectiveId("Properties"),
                new PerspectiveId("Individuals"),
                new PerspectiveId("Notes and Discussions"),
                new PerspectiveId("Changes by Entity")
        ));
    }

    @Override
    public ImmutableList<PerspectiveId> getPerspectives(ProjectId projectId, UserId userId) {
        return ImmutableList.copyOf(perspectiveIds);
    }

    @Override
    public void setPerspectives(ProjectId projectId, UserId userId, List<PerspectiveId> perspectives) {
        this.perspectiveIds.clear();
        this.perspectiveIds.addAll(perspectives);
    }

    @Override
    public void setDefaultPerspectives(Project projectId) {

    }
}
