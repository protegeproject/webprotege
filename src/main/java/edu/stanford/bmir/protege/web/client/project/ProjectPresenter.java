package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.logout.LogoutPresenter;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveSwitcherPresenter;
import edu.stanford.bmir.protege.web.client.perspective.PerspectivePresenter;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;


import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/02/16
 */
public class ProjectPresenter implements HasDispose, HasProjectId {

    private final ProjectId projectId;

    private final ProjectView view;

    private final PerspectiveSwitcherPresenter linkBarPresenter;

    private final PerspectivePresenter perspectivePresenter;

    private final LogoutPresenter logoutPresenter;

    private final EventBus eventBus;

    @Inject
    public ProjectPresenter(ProjectId projectId, ProjectView view,
                            LogoutPresenter logoutPresenter,
                            PerspectiveSwitcherPresenter linkBarPresenter,
                            PerspectivePresenter perspectivePresenter,
                            EventBus eventBus) {
        this.projectId = projectId;
        this.view = view;
        this.logoutPresenter = logoutPresenter;
        this.linkBarPresenter = linkBarPresenter;
        this.perspectivePresenter = perspectivePresenter;
        this.eventBus = eventBus;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public void dispose() {
        linkBarPresenter.dispose();
        perspectivePresenter.dispose();
    }

    //    /**
//     * Gets the project for this tab.
//     *
//     * @return The {@link edu.stanford.bmir.protege.web.client.project.Project}.  Not {@code null}.
//     */
//    public Project getProject() {
//        Optional<Project> project = projectManager.getProject(projectId);
//        if (!project.isPresent()) {
//            throw new IllegalStateException("Unknown project: " + project);
//        }
//        return project.get();
//    }

//    public void displayPlace(Place place) {
//        if(place instanceof ProjectViewPlace) {
//            GWT.log("[ProjectPresenter] Displaying place: " + place);
//            ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
//            PerspectiveId perspectiveId = projectViewPlace.getPerspectiveId();
//            Optional<Perspective> selectedTab = view.getSelectedTab();
//            if (!selectedTab.isPresent() || !selectedTab.get().getLabel().equals(perspectiveId.getId())) {
//                view.setSelectedTab(perspectiveId.getId());
//            }
////            Optional<Item<?>> firstSel = projectViewPlace.getItemSelection().getFirst();
////            if(firstSel.isPresent()) {
////                Item<?> item = firstSel.get();
////            }
////            Optional<OWLEntity> selection = projectViewPlace.getItemSelection();
////            if(selection.isPresent()) {
////                selectionModel.setSelection(DataFactory.getOWLEntityData(selection.get(), "Selection"));
////            }
//        }
//    }

    public void start(AcceptsOneWidget container, ProjectViewPlace place) {
        container.setWidget(view);
        view.getTopBarContainer().setWidget(logoutPresenter.getView());
        linkBarPresenter.start(view.getPerspectiveLinkBarViewContainer(), place);
        perspectivePresenter.start(view.getPerspectiveViewContainer(), place);
    }
}
