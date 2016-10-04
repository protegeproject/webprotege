package edu.stanford.bmir.protege.web.shared.watches;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class RemoveWatchesAction extends AbstractHasProjectAction<RemoveWatchesResult> implements HasUserId {

    private Set<Watch<?>> watches;

    private UserId userId;

    public RemoveWatchesAction(Set<Watch<?>> watches, ProjectId projectId, UserId userId) {
        super(projectId);
        this.watches = new HashSet<Watch<?>>(checkNotNull(watches));
        this.userId = checkNotNull(userId);
    }

    /**
     * For serialization only
     */
    private RemoveWatchesAction() {
    }

    public Set<Watch<?>> getWatches() {
        return new HashSet<Watch<?>>(watches);
    }

    @Override
    public UserId getUserId() {
        return userId;
    }
}
