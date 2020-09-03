package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
public class PerspectivesManagerService {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Inject
    public PerspectivesManagerService(@Nonnull ProjectId projectId,
                                      @Nonnull LoggedInUserProvider loggedInUserProvider,
                                      DispatchServiceManager dispatch) {
        this.projectId = projectId;
        this.loggedInUserProvider = loggedInUserProvider;
        this.dispatch = checkNotNull(dispatch);
    }

    public void getPerspectiveDetails(Consumer<List<PerspectiveDetails>> details) {
        dispatch.execute(new GetPerspectiveDetailsAction(projectId),
                         result -> {
                             ImmutableList<PerspectiveDetails> perspectiveDetails = result.getPerspectiveDetails();
                             details.accept(perspectiveDetails);
                         });
    }

    public void savePerspectives(@Nonnull ImmutableList<PerspectiveDescriptor> descriptors,
                                 @Nonnull Runnable completeHandler) {
        UserId currentUserId = loggedInUserProvider.getCurrentUserId();
        dispatch.execute(new SetPerspectivesAction(projectId, currentUserId, descriptors),
                         result -> completeHandler.run());
    }

    public void savePerspectivesAsProjectDefaults(@Nonnull ImmutableList<PerspectiveDescriptor> descriptors,
                                                  @Nonnull Runnable completeHandler) {
        UserId currentUserId = loggedInUserProvider.getCurrentUserId();
        dispatch.execute(new SetPerspectivesAction(projectId, descriptors),
                         result -> completeHandler.run());
    }

    public void resetPerspectives(@Nonnull Runnable completionHandler) {
        dispatch.execute(new ResetPerspectivesAction(projectId),
                         result -> completionHandler.run());
    }
}
