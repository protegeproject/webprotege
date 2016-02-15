package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.client.ui.LayoutUtil;
import edu.stanford.bmir.protege.web.client.ui.tab.PerspectiveFactory;
import edu.stanford.bmir.protege.web.client.ui.tab.TabBuilder;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.GetUIConfigurationAction;
import edu.stanford.bmir.protege.web.shared.project.GetUIConfigurationResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 16/05/2014
 */
public class PerspectivePresenter implements HasDispose {

    private final ProjectId projectId;

    private final DispatchServiceManager dispatchServiceManager;

    private final PerspectiveView perspectiveView;

    private final Map<PerspectiveId, Perspective> tabMap = new HashMap<>();

    private final PerspectiveFactory perspectiveFactory;

    private Optional<PerspectiveId> currentPerspective = Optional.absent();



    @Inject
    public PerspectivePresenter(PerspectiveView perspectiveView, ProjectId projectId, DispatchServiceManager dispatchServiceManager, PerspectiveFactory perspectiveFactory, EventBus eventBus) {
        this.perspectiveView = perspectiveView;
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.perspectiveFactory = perspectiveFactory;
        eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
            @Override
            public void onPlaceChange(PlaceChangeEvent event) {
                if(event.getNewPlace() instanceof ProjectViewPlace) {
                    displayPerspective(((ProjectViewPlace) event.getNewPlace()).getPerspectiveId());
                }
            }
        });
    }



    public void start(AcceptsOneWidget container, ProjectViewPlace place) {
        container.setWidget(perspectiveView);
        displayPerspective(place.getPerspectiveId());
    }


    private void displayPerspective(final PerspectiveId perspectiveId) {
        GWT.log("[PerspectivePresenter] displayPerspective: " + perspectiveId);
        currentPerspective = Optional.of(perspectiveId);
        retrievePerspective(perspectiveId, new DispatchServiceCallback<Perspective>() {
            @Override
            public void handleSuccess(Perspective perspective) {
                // Check still valid
                perspectiveView.setWidget(perspective);
            }
        });



    }

    public void removePerspective(PerspectiveId perspectiveId) {
        Perspective perspective = tabMap.remove(perspectiveId);
        if(perspective != null) {
            perspective.dispose();
            if(currentPerspective.equals(Optional.of(perspective))) {
                perspectiveView.setWidget(new Label("Nothing Here"));
            }
        }
    }

    private boolean loaded = false;


    private void retrievePerspective(PerspectiveId perspectiveId, DispatchServiceCallback<Perspective> callback) {
        loadProjectDisplay(perspectiveId, callback);
    }

    private void loadProjectDisplay(final PerspectiveId perspectiveId, final DispatchServiceCallback<Perspective> cb) {
        if(loaded) {
            Perspective p = tabMap.get(perspectiveId);
            if (p == null) {
                p = new Perspective(perspectiveId);
                tabMap.put(perspectiveId, p);
            }
            cb.onSuccess(p);
            return;
        }
        GWT.log("[PerspectivePresenter] Loading perspectives for project " + projectId);
        dispatchServiceManager.execute(new GetUIConfigurationAction(projectId),
                new DispatchServiceCallbackWithProgressDisplay<GetUIConfigurationResult>() {
                    @Override
                    public void handleSuccess(GetUIConfigurationResult result) {
                        setupUserInterface(result.getConfiguration());
                        Perspective p = tabMap.get(perspectiveId);
                        if (p == null) {
                            p = new Perspective(perspectiveId);
                            tabMap.put(perspectiveId, p);
                        }
                        cb.onSuccess(p);
                        loaded = true;
                    }

                    @Override
                    public String getProgressDisplayTitle() {
                        return "Loading project";
                    }

                    @Override
                    public String getProgressDisplayMessage() {
                        return "Loading user interface configuration";
                    }

                    @Override
                    protected String getErrorMessage(Throwable throwable) {
                        return "There was an error loading the UI configuration";
                    }
                });
    }

    private void setupUserInterface(ProjectLayoutConfiguration projectLayoutConfiguration) {
        for(TabConfiguration tabConf : projectLayoutConfiguration.getTabs()) {
            PerspectiveId perspectiveId = new PerspectiveId(tabConf.getLabel());
            Perspective tab = perspectiveFactory.createPerspective(perspectiveId);
            TabBuilder tabBuilder = new TabBuilder(projectId, tab, tabConf);
            tabBuilder.build();
            PerspectiveId key = new PerspectiveId(tab.getLabel());
            GWT.log("[PerspectivePresenter] Creating perspective: " + key);
            tabMap.put(key, tab);
        }
    }

    @Override
    public void dispose() {
        for(Perspective tab : tabMap.values()) {
            tab.dispose();
        }
    }

}