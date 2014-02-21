package edu.stanford.bmir.protege.web.shared.watches;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class AddWatchAction implements HasProjectAction<AddWatchResult>, HasUserId {

    private Watch watch;

    private ProjectId projectId;

    private UserId userId;

    public AddWatchAction(Watch watch, ProjectId projectId, UserId userId) {
        this.watch = watch;
        this.projectId = projectId;
        this.userId = userId;
    }

    private AddWatchAction() {
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public UserId getUserId() {
        return userId;
    }

    public Watch getWatch() {
        return watch;
    }
}
