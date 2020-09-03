package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public class ProjectPerspectivesServiceImpl implements ProjectPerspectivesService {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public ProjectPerspectivesServiceImpl(@Nonnull ProjectId projectId,
                                          @Nonnull DispatchServiceManager dispatch,
                                          @Nonnull LoggedInUserProvider loggedInUserProvider) {
        this.dispatch = checkNotNull(dispatch);
        this.loggedInUserProvider = checkNotNull(loggedInUserProvider);
        this.projectId = checkNotNull(projectId);
    }

    public void getPerspectives(final PerspectiveServiceCallback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatch.execute(new GetPerspectivesAction(projectId, userId), result -> {
            ImmutableList<PerspectiveDescriptor> perspectiveIds = result.getPerspectives();
            callback.handlePerspectives(perspectiveIds, result.getResettablePerspectives());
        });
    }

    @Override
    public void setPerspectives(List<PerspectiveDescriptor> perspectives, PerspectiveServiceCallback callback) {
        UserId user = loggedInUserProvider.getCurrentUserId();
        dispatch.execute(new SetPerspectivesAction(projectId, user, ImmutableList.copyOf(perspectives)), result -> {
            callback.handlePerspectives(result.getPerspectives(), result.getResettablePerspectives());
        });
    }
}
