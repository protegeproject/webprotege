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
public class ProjectPerspectivesServiceImpl implements ProjectPerspectivesService {

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Inject
    public ProjectPerspectivesServiceImpl(@Nonnull ProjectId projectId,
                                          @Nonnull DispatchServiceManager dispatchServiceManager,
                                          @Nonnull LoggedInUserProvider loggedInUserProvider,
                                          @Nonnull DispatchErrorMessageDisplay errorDisplay) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.loggedInUserProvider = checkNotNull(loggedInUserProvider);
        this.projectId = checkNotNull(projectId);
        this.errorDisplay = checkNotNull(errorDisplay);
    }

    public void getPerspectives(final PerspectiveServiceCallback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), result -> {
            ImmutableList<PerspectiveDescriptor> perspectiveIds = result.getPerspectives();
            callback.handlePerspectives(perspectiveIds);
        });
    }

    @Override
    public void getFavoritePerspectives(PerspectiveServiceCallback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), result -> {
            ImmutableList<PerspectiveDescriptor> perspectiveIds = result.getPerspectives();
            GWT.log("[ProjectPerspectivesService] Linked perspectives: " + perspectiveIds);
            callback.handlePerspectives(perspectiveIds);
        });
    }

    public void removeLinkedPerspective(final PerspectiveId perspectiveId, final PerspectiveServiceCallback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), result -> {
            final List<PerspectiveDescriptor> ids = result.getPerspectives()
                    .stream()
                    .map(perspectiveDescriptor -> {
                        if(perspectiveDescriptor.getPerspectiveId().equals(perspectiveId)) {
                            return perspectiveDescriptor.withFavorite(false);
                        }
                        else {
                            return perspectiveDescriptor;
                        }
                    })
                    .collect(toImmutableList());
            dispatchServiceManager.execute(new SetPerspectivesAction(projectId, userId, ImmutableList.copyOf(ids)), new DispatchServiceCallback<SetPerspectivesResult>(errorDisplay) {
                @Override
                public void handleSuccess(SetPerspectivesResult setPerspectivesResult) {
                    callback.handlePerspectives(ids);
                }
            });
        });
    }

    public void addFavoritePerspective(final PerspectiveDescriptor perspectiveDescriptor, final PerspectiveServiceCallback callback) {
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
