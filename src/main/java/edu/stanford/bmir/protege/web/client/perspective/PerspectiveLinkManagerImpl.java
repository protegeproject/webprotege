package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public class PerspectiveLinkManagerImpl implements PerspectiveLinkManager {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final ProjectId projectId;

    @Inject
    public PerspectiveLinkManagerImpl(ProjectId projectId,
                                      DispatchServiceManager dispatchServiceManager,
                                      LoggedInUserProvider loggedInUserProvider) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        this.projectId = projectId;
    }

    public void getLinkedPerspectives(final Callback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), new DispatchServiceCallback<GetPerspectivesResult>() {
            @Override
            public void handleSuccess(GetPerspectivesResult result) {
                callback.handlePerspectives(result.getPerspectives());
            }
        });
    }

    @Override
    public void getBookmarkedPerspectives(Callback callback) {
        callback.handlePerspectives(
                Arrays.asList(
                        new PerspectiveId("Classes"),
                        new PerspectiveId("OWL Classes"),
                        new PerspectiveId("Properties"),
                        new PerspectiveId("OWL Properties"),
                        new PerspectiveId("Individuals"),
                        new PerspectiveId("Comments"),
                        new PerspectiveId("Changes by Entity"),
                        new PerspectiveId("History")
                )
        );
    }

    public void removeLinkedPerspective(final PerspectiveId perspectiveId, final Callback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), new DispatchServiceCallback<GetPerspectivesResult>() {
            @Override
            public void handleSuccess(GetPerspectivesResult result) {
                final List<PerspectiveId> ids = new ArrayList<>(result.getPerspectives());
                ids.remove(perspectiveId);
                dispatchServiceManager.execute(new SetPerspectivesAction(projectId, userId, ImmutableList.copyOf(ids)), new DispatchServiceCallback<SetPerspectivesResult>() {
                    @Override
                    public void handleSuccess(SetPerspectivesResult setPerspectivesResult) {
                        callback.handlePerspectives(ids);
                    }
                });
            }
        });
    }

    public void addLinkedPerspective(final PerspectiveId perspectiveId, final Callback callback) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), new DispatchServiceCallback<GetPerspectivesResult>() {
            @Override
            public void handleSuccess(GetPerspectivesResult result) {
                List<PerspectiveId> ids = new ArrayList<>(result.getPerspectives());
                ids.add(perspectiveId);
                final ImmutableList<PerspectiveId> perspectiveIds = ImmutableList.copyOf(ids);
                dispatchServiceManager.execute(new SetPerspectivesAction(projectId, userId, perspectiveIds), new DispatchServiceCallback<SetPerspectivesResult>() {
                    @Override
                    public void handleSuccess(SetPerspectivesResult setPerspectivesResult) {
                        callback.handlePerspectives(perspectiveIds);
                    }
                });
            }
        });
    }
}
