package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import javax.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.PortletChooserPresenter;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.PortletId;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.widgetmap.client.RootNodeChangedEvent;
import edu.stanford.protege.widgetmap.client.RootNodeChangedHandler;
import edu.stanford.protege.widgetmap.shared.node.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 16/05/2014
 */
public class PerspectivePresenter implements HasDispose {

    private final ProjectId projectId;

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final PerspectiveView perspectiveView;

    private final Map<PerspectiveId, Perspective> perspectiveCache = new HashMap<>();

    private final Map<PerspectiveId, Node> originalRootNodeMap = new HashMap<>();

    private final Map<PerspectiveId, Node> previousRootNodeMap = new HashMap<>();

    private final PerspectiveFactory perspectiveFactory;

    private final EmptyPerspectivePresenterFactory emptyPerspectivePresenterFactory;

    private final PortletChooserPresenter portletChooserPresenter;

    private Optional<PerspectiveId> currentPerspective = Optional.absent();


    @Inject
    public PerspectivePresenter(final PerspectiveView perspectiveView,
                                final LoggedInUserProvider loggedInUserProvider,
                                ProjectId projectId,
                                DispatchServiceManager dispatchServiceManager,
                                PerspectiveFactory perspectiveFactory,
                                EventBus eventBus,
                                EmptyPerspectivePresenterFactory emptyPerspectivePresenterFactory,
                                PortletChooserPresenter portletChooserPresenter) {
        this.perspectiveView = perspectiveView;
        this.loggedInUserProvider = loggedInUserProvider;
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.perspectiveFactory = perspectiveFactory;
        this.emptyPerspectivePresenterFactory = emptyPerspectivePresenterFactory;
        this.portletChooserPresenter = portletChooserPresenter;

        eventBus.addHandler(PlaceChangeEvent.TYPE, event -> {
            if(event.getNewPlace() instanceof ProjectViewPlace) {
                displayPerspective(((ProjectViewPlace) event.getNewPlace()).getPerspectiveId());
            }
        });

        eventBus.addHandler(ResetPerspectiveEvent.getType(), event -> resetPerspective(event.getPerspectiveId()));

        eventBus.addHandler(AddViewToPerspectiveEvent.getType(), perspectiveId -> addViewToPerspective(perspectiveId));
    }

    public void start(AcceptsOneWidget container, ProjectViewPlace place) {
        GWT.log("[PerspectivePresenter] Starting at place " + place);
        container.setWidget(perspectiveView);
        displayPerspective(place.getPerspectiveId());
    }

    private void resetPerspective(PerspectiveId perspectiveId) {
        GWT.log("[PerspectivePresenter] Reset Perspective: " + perspectiveId);

        Perspective perspective = perspectiveCache.get(perspectiveId);
        if(perspective == null) {
            return;
        }
        Node originalRootNode = originalRootNodeMap.get(perspectiveId);
        if(originalRootNode == null) {
            perspective.setRootNode(Optional.<Node>absent());
        }
        else {
            perspective.setRootNode(Optional.<Node>of(originalRootNode.duplicate()));
        }
    }

    private void addViewToPerspective(final PerspectiveId perspectiveId) {
        final Perspective perspective = perspectiveCache.get(perspectiveId);
        if(perspective == null) {
            return;
        }
        portletChooserPresenter.show(portletId -> perspective.dropView(portletId.getPortletId()));
    }

    private void displayPerspective(final PerspectiveId perspectiveId) {
        if(currentPerspective.equals(Optional.of(perspectiveId))) {
            return;
        }
        GWT.log("[PerspectivePresenter] Display Perspective: " + perspectiveId);
        currentPerspective = Optional.of(perspectiveId);
        retrieveAndSetPerspective(perspectiveId);
    }

    public void removePerspective(PerspectiveId perspectiveId) {
        Perspective perspective = perspectiveCache.remove(perspectiveId);
        if(perspective != null) {
            perspective.dispose();
            if(currentPerspective.equals(Optional.of(perspective.getPerspectiveId()))) {
                perspectiveView.setWidget(new Label("Nothing Here"));
            }
        }
    }

    private void retrieveAndSetPerspective(final  PerspectiveId perspectiveId) {
        GWT.log("[PerspectivePresenter] Retrive and set perspective for " + perspectiveId);
        Perspective p = perspectiveCache.get(perspectiveId);
        if(p != null) {
            GWT.log("[PerspectivePresenter] Using cached perspective for " + perspectiveId);
            perspectiveView.setWidget(p);
            return;
        }
        perspectiveView.setWidget(new Label("Loading..."));
        GWT.log("[PerspectivePresenter] Loading perspective for project " + projectId);
        UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectiveLayoutAction(projectId, userId, perspectiveId),
                new DispatchServiceCallback<GetPerspectiveLayoutResult>() {
                    @Override
                    public void handleSuccess(GetPerspectiveLayoutResult result) {
                        GWT.log("[PerspectivePresenter] Retrieved layout: " + result.getPerspectiveLayout());
                        Perspective perspective = perspectiveFactory.createPerspective(perspectiveId);
                        EmptyPerspectivePresenter emptyPerspectivePresenter = emptyPerspectivePresenterFactory.createEmptyPerspectivePresenter(perspectiveId);
                        perspective.setEmptyPerspectiveWidget(emptyPerspectivePresenter.getView());
                        Optional<Node> rootNode = result.getPerspectiveLayout().getRootNode();
                        perspective.setRootNode(rootNode);
                        perspective.setRootNodeChangedHandler(rootNodeChangedEvent -> {
                            savePerspectiveLayout(perspectiveId, rootNodeChangedEvent.getTo());
                        });
                        perspectiveCache.put(perspectiveId, perspective);
                        perspectiveView.setWidget(perspective);
                        if (rootNode.isPresent()) {
                            originalRootNodeMap.put(perspectiveId, rootNode.get().duplicate());
                        }
                    }
                });
    }

    private final Map<PerspectiveId, SavePerspectiveRunner> perspectivesToSave = new HashMap<>();

    private final Timer perspectiveSaveTimer = new Timer() {
        @Override
        public void run() {
            for(SavePerspectiveRunner runner : perspectivesToSave.values()) {
                runner.savePerspective();
            }
            perspectivesToSave.clear();
        }
    };

    private void savePerspectiveLayout(PerspectiveId perspectiveId, Optional<Node> node) {
        perspectivesToSave.put(perspectiveId, new SavePerspectiveRunner(projectId, perspectiveId, node, dispatchServiceManager, loggedInUserProvider));
        perspectiveSaveTimer.cancel();
        perspectiveSaveTimer.schedule(1000);
    }


    @Override
    public void dispose() {
        for(Perspective tab : perspectiveCache.values()) {
            tab.dispose();
        }
    }


    private static class SavePerspectiveRunner {

        private final ProjectId projectId;

        private final PerspectiveId perspectiveId;

        private final Optional<Node> node;

        private final DispatchServiceManager dispatchServiceManager;

        private final LoggedInUserProvider loggedInUserProvider;



        public SavePerspectiveRunner(ProjectId projectId, PerspectiveId perspectiveId, Optional<Node> node, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
            this.projectId = projectId;
            this.perspectiveId = perspectiveId;
            this.node = node;
            this.dispatchServiceManager = dispatchServiceManager;
            this.loggedInUserProvider = loggedInUserProvider;
        }

        public void savePerspective() {
            GWT.log("[PerspectivePresenter] Saving perspective: " + perspectiveId);
            UserId currentUserId = loggedInUserProvider.getCurrentUserId();
            if(currentUserId.isGuest()) {
                return;
            }
            PerspectiveLayout layout = new PerspectiveLayout(perspectiveId, node);
            dispatchServiceManager.execute(new SetPerspectiveLayoutAction(projectId, currentUserId, layout), new DispatchServiceCallback<SetPerspectiveLayoutResult>() {

            });
        }
    }
}