package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.PortletChooserPresenter;
import edu.stanford.bmir.protege.web.client.progress.BusyViewImpl;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.widgetmap.shared.node.Node;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.ADD_OR_REMOVE_VIEW;
import static edu.stanford.bmir.protege.web.shared.perspective.ResetPerspectiveLayoutAction.resetPerspective;

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

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private java.util.Optional<PerspectiveId> currentPerspective = java.util.Optional.empty();


    @Inject
    public PerspectivePresenter(final PerspectiveView perspectiveView,
                                final LoggedInUserProvider loggedInUserProvider,
                                LoggedInUserProjectPermissionChecker permissionChecker,
                                ProjectId projectId,
                                DispatchServiceManager dispatchServiceManager,
                                PerspectiveFactory perspectiveFactory,
                                EmptyPerspectivePresenterFactory emptyPerspectivePresenterFactory,
                                PortletChooserPresenter portletChooserPresenter) {
        this.perspectiveView = perspectiveView;
        this.loggedInUserProvider = loggedInUserProvider;
        this.permissionChecker = permissionChecker;
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.perspectiveFactory = perspectiveFactory;
        this.emptyPerspectivePresenterFactory = emptyPerspectivePresenterFactory;
        this.portletChooserPresenter = portletChooserPresenter;
    }

    public void start(AcceptsOneWidget container, EventBus eventBus, ProjectViewPlace place) {
        GWT.log("[PerspectivePresenter] Starting at place " + place);
        eventBus.addHandler(PlaceChangeEvent.TYPE, event -> {
            if(event.getNewPlace() instanceof ProjectViewPlace) {
                displayPerspective(((ProjectViewPlace) event.getNewPlace()).getPerspectiveId());
            }
        });
        eventBus.addHandler(ResetPerspectiveEvent.getType(), this::handleResetPerspective);
        eventBus.addHandler(AddViewToPerspectiveEvent.getType(), this::addViewToPerspective);
        container.setWidget(perspectiveView);
        displayPerspective(place.getPerspectiveId());
    }

    private void handleResetPerspective(ResetPerspectiveEvent event) {
        PerspectiveId perspectiveId = event.getPerspectiveId();
        MessageBox.showYesNoConfirmBox("Reset tab?",
                                       "Are you sure you want to reset the <em>" +
                                               perspectiveId.getId() + "</em> tab to the default state?",
                                       () -> executeResetPerspective(perspectiveId));
    }

    private void executeResetPerspective(PerspectiveId perspectiveId) {
        GWT.log("[PerspectivePresenter] Reset Perspective: " + perspectiveId);
        dispatchServiceManager.execute(resetPerspective(projectId, perspectiveId),
                                       result -> {
                                           removePerspective(perspectiveId);
                                           installPerspective(perspectiveId, result.getResetLayout());
                                       });
    }

    private void addViewToPerspective(final PerspectiveId perspectiveId) {
        final Perspective perspective = perspectiveCache.get(perspectiveId);
        if(perspective == null) {
            return;
        }
        portletChooserPresenter.show(portletId -> perspective.dropView(portletId.getPortletId()));
    }

    private void displayPerspective(final PerspectiveId perspectiveId) {
        if(currentPerspective.equals(java.util.Optional.of(perspectiveId))) {
            return;
        }
        GWT.log("[PerspectivePresenter] Display Perspective: " + perspectiveId);
        currentPerspective = java.util.Optional.of(perspectiveId);
        retrieveAndSetPerspective(perspectiveId);
    }

    public void removePerspective(PerspectiveId perspectiveId) {
        Perspective perspective = perspectiveCache.remove(perspectiveId);
        if(perspective != null) {
            perspective.dispose();
            if(currentPerspective.equals(java.util.Optional.of(perspective.getPerspectiveId()))) {
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
        perspectiveView.setWidget(new BusyViewImpl());
        GWT.log("[PerspectivePresenter] Loading perspective for project " + projectId);
        UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetPerspectiveLayoutAction(projectId, userId, perspectiveId),
                result -> {
                    GWT.log("[PerspectivePresenter] Retrieved layout: " + result.getPerspectiveLayout());
                    installPerspective(perspectiveId, result.getPerspectiveLayout());
                });
    }

    private void installPerspective(PerspectiveId perspectiveId, PerspectiveLayout layout) {
        permissionChecker.hasPermission(ADD_OR_REMOVE_VIEW,
                                        canAddRemove -> {
                                            GWT.log("[PerspectivePresenter] Can close views: " + canAddRemove);
                                            installPerspective(perspectiveId, layout, canAddRemove);
                                        });
    }

    private void installPerspective(@Nonnull PerspectiveId perspectiveId,
                                    @Nonnull PerspectiveLayout layout,
                                    boolean viewsCloseable) {
        Perspective perspective = perspectiveFactory.createPerspective(perspectiveId);
        perspective.setViewsCloseable(viewsCloseable);
        EmptyPerspectivePresenter emptyPerspectivePresenter = emptyPerspectivePresenterFactory.createEmptyPerspectivePresenter(perspectiveId);
        perspective.setEmptyPerspectiveWidget(emptyPerspectivePresenter.getView());
        Optional<Node> rootNode = layout.getRootNode();
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