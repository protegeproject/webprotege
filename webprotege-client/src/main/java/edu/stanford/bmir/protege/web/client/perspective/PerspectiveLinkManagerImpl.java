package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

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
            ImmutableList<PerspectiveDescriptor> perspectiveIds = result.getPerspectives();
            callback.handlePerspectives(perspectiveIds);
        });
    }

    @Override
    public void getBookmarkedPerspectives(Callback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), result -> {
            ImmutableList<PerspectiveDescriptor> perspectiveIds = result.getPerspectives();
            GWT.log("[PerspectiveLinkManager] Linked perspectives: " + perspectiveIds);
            callback.handlePerspectives(perspectiveIds);
        });
    }

    public void removeLinkedPerspective(final PerspectiveId perspectiveId, final Callback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), result -> {
            final List<PerspectiveDescriptor> ids = result.getPerspectives()
                    .stream()
                    .filter(perspectiveDescriptor -> !perspectiveDescriptor.getPerspectiveId().equals(perspectiveId))
                    .collect(toImmutableList());
            dispatchServiceManager.execute(new SetPerspectivesAction(projectId, userId, ImmutableList.copyOf(ids)), new DispatchServiceCallback<SetPerspectivesResult>(errorDisplay) {
                @Override
                public void handleSuccess(SetPerspectivesResult setPerspectivesResult) {
                    callback.handlePerspectives(ids);
                }
            });
        });
    }

    public void addLinkedPerspective(final PerspectiveDescriptor perspectiveDescriptor, final Callback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), result -> {
            List<PerspectiveDescriptor> ids = result.getPerspectives()
                                                    .stream()
                                                    .filter(PerspectiveDescriptor::isFavorite)
                                                    .collect(Collectors.toList());
            ids.add(perspectiveDescriptor);
            final ImmutableList<PerspectiveDescriptor> perspectiveIds = ImmutableList.copyOf(ids);
            dispatchServiceManager.execute(new SetPerspectivesAction(projectId, userId, perspectiveIds), new DispatchServiceCallback<SetPerspectivesResult>(errorDisplay) {
                @Override
                public void handleSuccess(SetPerspectivesResult setPerspectivesResult) {
                    callback.handlePerspectives(perspectiveIds);
                }
            });
        });
    }
}
