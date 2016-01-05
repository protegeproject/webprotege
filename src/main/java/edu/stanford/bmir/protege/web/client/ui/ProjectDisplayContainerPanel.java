package edu.stanford.bmir.protege.web.client.ui;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.event.TabPanelListenerAdapter;
import edu.stanford.bmir.protege.web.client.inject.ProjectIdProvider;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.HasClientApplicationProperties;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.place.ProjectListPlace;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.ProjectManagerPresenter;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedEvent;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedHandler;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;

import edu.stanford.bmir.protege.web.client.ui.ontology.home.MyWebProtegeTab;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.shared.place.TabName;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsChangedEvent;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsChangedHandler;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Class that holds all the tabs corresponding to ontologies. It also contains
 * that MyWebProtege Tab. This class manages the loading of projects and their
 * configurations.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class ProjectDisplayContainerPanel extends TabPanel {

    private final LinkedHashMap<ProjectId, ProjectDisplayImpl> projectId2ProjectPanelMap = new LinkedHashMap<>();

    private final Set<ProjectId> currentlyLoadingProjects = new HashSet<>();

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    private final ActiveProjectManager activeProjectManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final PlaceController placeController;

    private final ProjectManager projectManager;

    private final HasClientApplicationProperties hasClientApplicationProperties;

    private final Provider<ProjectDisplayImpl> projectDisplayProvider;

    @Inject
    public ProjectDisplayContainerPanel(EventBus eventBus, DispatchServiceManager dispatchServiceManager, ActiveProjectManager activeProjectManager, ProjectManager projectManager, PlaceController placeController, LoggedInUserProvider loggedInUserProvider, HasClientApplicationProperties hasClientApplicationProperties, Provider<ProjectDisplayImpl> projectDisplayProvider) {
        super();
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectManager = projectManager;
        this.activeProjectManager = activeProjectManager;
        this.placeController = placeController;
        this.loggedInUserProvider = loggedInUserProvider;
        this.hasClientApplicationProperties = hasClientApplicationProperties;
        this.projectDisplayProvider = projectDisplayProvider;
        buildUI();

        Place currentPlace = placeController.getWhere();
        displayCurrentPlace(currentPlace);


        this.addListener(new TabPanelListenerAdapter() {
            @Override
            public void onTabChange(TabPanel source, Panel tab) {
                transmitActiveProject();
            }
        });


        eventBus.addHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                // TODO: This is a bit extreme.  We only need to remove the tabs for which the user had no access rights
                removeAllTabs();
            }
        });

        eventBus.addHandler(ActiveProjectChangedEvent.TYPE, new ActiveProjectChangedHandler() {
            @Override
            public void handleActiveProjectChanged(ActiveProjectChangedEvent event) {
                respondToActiveProjectChangedEvent(event);
            }
        });

        eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
            @Override
            public void onPlaceChange(PlaceChangeEvent event) {
                GWT.log("[ProjectDisplayContainerPanel] Place has changed.  New place: " + event.getNewPlace());
                displayCurrentPlace(event.getNewPlace());
            }
        });

        eventBus.addHandler(ProjectSettingsChangedEvent.getType(), new ProjectSettingsChangedHandler() {
            @Override
            public void handleProjectSettingsChanged(ProjectSettingsChangedEvent event) {
                ProjectSettings projectSettings = event.getProjectSettings();
                ProjectDisplayImpl projectDisplay = projectId2ProjectPanelMap.get(projectSettings.getProjectId());
                projectDisplay.setTitle(projectSettings.getProjectDisplayName());
            }
        });

    }


    private Optional<ProjectId> getProjectIdForActiveTab() {
        Panel activeTab = getActiveTab();
        final Optional<ProjectId> projectId;
        if (activeTab instanceof ProjectDisplay) {
            projectId = Optional.of(((ProjectDisplay) activeTab).getProjectId());
        }
        else {
            projectId = Optional.absent();
        }
        return projectId;
    }


    private void transmitActiveProject() {
        final Optional<ProjectId> projectId = getProjectIdForActiveTab();
        activeProjectManager.setActiveProject(projectId);
        if (projectId.isPresent()) {
            Place place = placeController.getWhere();
            Optional<OWLEntity> entity;
            Optional<TabName> tabId;
            if(place instanceof ProjectViewPlace) {
                ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
                entity = projectViewPlace.getEntity();
                tabId = projectViewPlace.getTabId();
            }
            else {
                entity = Optional.absent();
                tabId = Optional.absent();
            }
            placeController.goTo(new ProjectViewPlace(projectId.get(), tabId, entity));
        }
        else {
            placeController.goTo(ProjectListPlace.DEFAULT_PLACE);
        }
    }


    private void respondToActiveProjectChangedEvent(final ActiveProjectChangedEvent event) {
        Optional<ProjectId> projectId = event.getProjectId();
        if (!projectId.isPresent()) {
            // Go home
            setActiveTab(0);
        }
        else {
            ProjectDisplayImpl display = projectId2ProjectPanelMap.get(projectId.get());
            if (display != null) {
                setActiveTab(display.getLabel());
            }
        }
    }

    private void displayCurrentPlace(Place place) {
        GWT.log("[ProjectDisplayContainerPanel] Request to display current place: " + place);
        if (place instanceof ProjectListPlace) {
            setActiveTab(0);
        }
        else if (place instanceof ProjectViewPlace) {
            final ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
            loadProject(projectViewPlace.getProjectId());
        }
    }

    private void buildUI() {
        setLayoutOnTabChange(true); // TODO: check if necessary
        createAndAddHomeTab();
    }

    private void removeAllTabs() {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                for (ProjectDisplayImpl ontologyTabPanel : projectId2ProjectPanelMap.values()) {
                    Project project = ontologyTabPanel.getProject();
                    projectManager.unloadProject(project.getProjectId());
                    hideTabStripItem(ontologyTabPanel);
                    ontologyTabPanel.hide();
                    ontologyTabPanel.destroy();
                }
                projectId2ProjectPanelMap.clear();
                activate(0);
            }
        });

    }


    private void createAndAddHomeTab() {

        LoadProjectRequestHandler loadProjectRequestHandler = new LoadProjectRequestHandler() {
            @Override
            public void handleProjectLoadRequest(final ProjectId projectId) {
                loadProject(projectId);
            }
        };

        MyWebProtegeTab myWebProTab = new MyWebProtegeTab(new ProjectManagerPresenter(
                loadProjectRequestHandler,
                eventBus,
                dispatchServiceManager,
                loggedInUserProvider), hasClientApplicationProperties);
//        myWebProTab.setTitle(myWebProTab.getLabel());
        add(myWebProTab);


    }

    private void loadProject(final ProjectId projectId) {
        GWT.log("[ProjectDisplayContainerPanel] Received a request to load " + projectId);
        ProjectDisplayImpl ontTab = projectId2ProjectPanelMap.get(projectId);
        if (ontTab != null) {
            GWT.log("[ProjectDisplayContainerPanel] " + projectId + " is already loaded.  Switching to tab.");
            activate(ontTab.getId());
            return;
        }

        if (currentlyLoadingProjects.contains(projectId)) {
            GWT.log("[ProjectDisplayContainerPanel] " + projectId + " is already being loaded");
            return;
        }
        currentlyLoadingProjects.add(projectId);
        GWT.log("[ProjectDisplayContainerPanel] Loading project " + projectId);
        projectManager.loadProject(projectId, new DispatchServiceCallbackWithProgressDisplay<Project>() {
            @Override
            public String getProgressDisplayTitle() {
                return "Loading project";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Please wait.";
            }

            @Override
            public void handleSuccess(Project project) {
                addProjectDisplay(projectId);
            }

            @Override
            protected String getErrorMessageTitle() {
                return "Error";
            }

            @Override
            protected String getErrorMessage(Throwable throwable) {
                return "There was an error whilst loading the project.  Please try again.";
            }

            @Override
            public void handleFinally() {
                currentlyLoadingProjects.remove(projectId);
            }
        });

    }

    private void addProjectDisplay(final ProjectId projectId) {
        if(projectId2ProjectPanelMap.containsKey(projectId)) {
            GWT.log("[ProjectDisplayContainerPanel] Ignoring request to add project display as it has already been made for project " + projectId);
            return;
        }

        // TODO: FIX!!!
        ProjectIdProvider.setProjectId(projectId);
        ProjectDisplayImpl projectPanel = projectDisplayProvider.get();//new ProjectDisplayImpl(projectId, eventBus, dispatchServiceManager, projectManager, loggedInUserProvider, placeController, selectionModel);
        projectPanel.setClosable(true);
        projectId2ProjectPanelMap.put(projectId, projectPanel);

        projectPanel.addListener(new PanelListenerAdapter() {
            @Override
            public boolean doBeforeDestroy(Component component) {
                if (component instanceof ProjectDisplayImpl) {
                    ProjectDisplayImpl o = (ProjectDisplayImpl) component;
                    ProjectId projectId = o.getProjectId();
                    projectId2ProjectPanelMap.remove(projectId);
                    projectManager.unloadProject(projectId);
                    hideTabStripItem(o);
                    o.hide();
                    activate(0);
                }
                activeProjectManager.setActiveProject(Optional.<ProjectId>absent());
                return true;
            }
        });

        add(projectPanel);
        activate(projectPanel.getId());
        setActiveTab(projectPanel.getId());
        projectPanel.loadProjectDisplay();
    }
}
