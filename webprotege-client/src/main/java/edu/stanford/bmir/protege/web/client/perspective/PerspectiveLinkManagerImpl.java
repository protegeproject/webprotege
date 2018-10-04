package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public class PerspectiveLinkManagerImpl implements PerspectiveLinkManager {

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Inject
    public PerspectiveLinkManagerImpl(@Nonnull ProjectId projectId,
                                      @Nonnull DispatchServiceManager dispatchServiceManager,
                                      @Nonnull LoggedInUserProvider loggedInUserProvider,
                                      @Nonnull DispatchErrorMessageDisplay errorDisplay) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.loggedInUserProvider = checkNotNull(loggedInUserProvider);
        this.projectId = checkNotNull(projectId);
        this.errorDisplay = checkNotNull(errorDisplay);
    }

    public void getLinkedPerspectives(final Callback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), result -> {
            callback.handlePerspectives(result.getPerspectives());
        });
    }

    @Override
    public void getBookmarkedPerspectives(Callback callback) {
        callback.handlePerspectives(
                Arrays.asList(
                        PerspectiveId.get("Classes"),
                        PerspectiveId.get("OWL Classes"),
                        PerspectiveId.get("Properties"),
                        PerspectiveId.get("OWL Properties"),
                        PerspectiveId.get("Individuals"),
                        PerspectiveId.get("Comments"),
                        PerspectiveId.get("Changes by Entity"),
                        PerspectiveId.get("History"),
                        PerspectiveId.get("Query")
                )
        );
    }

    public void removeLinkedPerspective(final PerspectiveId perspectiveId, final Callback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), result -> {
            final List<PerspectiveId> ids = new ArrayList<>(result.getPerspectives());
            ids.remove(perspectiveId);
            dispatchServiceManager.execute(new SetPerspectivesAction(projectId, userId, ImmutableList.copyOf(ids)), new DispatchServiceCallback<SetPerspectivesResult>(errorDisplay) {
                @Override
                public void handleSuccess(SetPerspectivesResult setPerspectivesResult) {
                    callback.handlePerspectives(ids);
                }
            });
        });
    }

    public void addLinkedPerspective(final PerspectiveId perspectiveId, final Callback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), result -> {
            List<PerspectiveId> ids = new ArrayList<>(result.getPerspectives());
            ids.add(perspectiveId);
            final ImmutableList<PerspectiveId> perspectiveIds = ImmutableList.copyOf(ids);
            dispatchServiceManager.execute(new SetPerspectivesAction(projectId, userId, perspectiveIds), new DispatchServiceCallback<SetPerspectivesResult>(errorDisplay) {
                @Override
                public void handleSuccess(SetPerspectivesResult setPerspectivesResult) {
                    callback.handlePerspectives(perspectiveIds);
                }
            });
        });
    }
}
