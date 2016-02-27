package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBoxHandler;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public class PerspectiveLinkManagerImpl implements PerspectiveLinkManager {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final ProjectId projectId;

    private final List<PerspectiveId> perspectives = new ArrayList<>();

    @Inject
    public PerspectiveLinkManagerImpl(DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider, ProjectId projectId) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        this.projectId = projectId;
    }

    public void getLinkedPerspectives(final Callback callback) {
        UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectivesAction(projectId, userId), new DispatchServiceCallback<GetPerspectivesResult>() {
            @Override
            public void handleSuccess(GetPerspectivesResult result) {
                perspectives.clear();
                perspectives.addAll(result.getPerspectives());
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
                        new PerspectiveId("Individuals"),
                        new PerspectiveId("Discussions"),
                        new PerspectiveId("Changes by Entity"),
                        new PerspectiveId("History")
                )
        );
    }

    public void removeLinkedPerspective(PerspectiveId perspectiveId, Callback callback) {
        perspectives.remove(perspectiveId);
        setPerspectives();
        callback.handlePerspectives(new ArrayList<>(perspectives));
    }

    public void addLinkedPerspective(PerspectiveId perspectiveId, Callback callback) {
        perspectives.add(perspectiveId);
        setPerspectives();
        callback.handlePerspectives(new ArrayList<>(perspectives));
    }

    private void setPerspectives() {
        GWT.log("[PerspectiveLinkManager] setPerspectives: " + perspectives);
        UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new SetPerspectivesAction(projectId, userId, ImmutableList.copyOf(perspectives)),
                new DispatchServiceCallback<SetPerspectivesResult>() {
                    @Override
                    public void handleSuccess(SetPerspectivesResult setPerspectivesResult) {

                    }
                });
    }
}
