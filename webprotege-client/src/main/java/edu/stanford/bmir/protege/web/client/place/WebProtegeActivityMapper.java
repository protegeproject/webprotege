package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.ApplicationSettingsPresenter;
import edu.stanford.bmir.protege.web.client.collection.CollectionPresenter;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.inject.ClientApplicationComponent;
import edu.stanford.bmir.protege.web.client.inject.ClientProjectComponent;
import edu.stanford.bmir.protege.web.client.inject.ClientProjectModule;
import edu.stanford.bmir.protege.web.client.inject.ProjectIdProvider;
import edu.stanford.bmir.protege.web.client.tag.ProjectTagsActivity;
import edu.stanford.bmir.protege.web.client.project.ProjectPresenter;
import edu.stanford.bmir.protege.web.client.projectmanager.ProjectManagerPresenter;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsPresenter;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.place.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsActivity;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsActivity;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSettingsPlace;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class WebProtegeActivityMapper implements ActivityMapper {

    private final ClientApplicationComponent applicationComponent;

    private final ProjectManagerPresenter projectManagerPresenter;

    private final ApplicationSettingsPresenter applicationSettingsPresenter;

    private final LoggedInUserProvider loggedInUserProvider;

    private final PlaceController placeController;

    private final EventBus eventBus;

    private Optional<ProjectPresenter> lastProjectPresenter = Optional.empty();

    private Optional<CollectionPresenter> lastCollectionPresenter = Optional.empty();

    private Optional<UserId> lastUser = Optional.empty();

    @Inject
    public WebProtegeActivityMapper(LoggedInUserProvider loggedInUserProvider,
                                    ClientApplicationComponent applicationComponent,
                                    ProjectManagerPresenter projectListPresenter,
                                    ApplicationSettingsPresenter applicationSettingsPresenter,
                                    PlaceController placeController,
                                    EventBus eventBus) {
        this.applicationComponent = applicationComponent;
        this.loggedInUserProvider = loggedInUserProvider;
        this.projectManagerPresenter = projectListPresenter;
        this.applicationSettingsPresenter = applicationSettingsPresenter;
        this.placeController = placeController;
        this.eventBus = eventBus;
    }

    public void start() {
        GWT.log("[WebProtegeActivityMapper] Started activity mapper.");
        eventBus.addHandler(UserLoggedOutEvent.ON_USER_LOGGED_OUT, event -> {
            // Reload window.  This will take us back to the keycloak login screen
            Window.Location.reload();
        });
    }

    public Activity getActivity(final Place place) {
        GWT.log("[WebProtegeActivityMapper] Map place: " + place);
        if(place instanceof ApplicationSettingsPlace) {
            return new AdminActivity(applicationSettingsPresenter);
        }
        if(place instanceof ProjectSettingsPlace) {
            ProjectSettingsPlace projectSettingsPlace = (ProjectSettingsPlace) place;
            ClientProjectComponent projectComponent = applicationComponent.getClientProjectComponent(
                    new ClientProjectModule(projectSettingsPlace.getProjectId()));
            return new ProjectSettingsActivity(projectComponent.getProjectSettingsPresenter(),
                                               projectSettingsPlace.getNextPlace());
        }
        if(place instanceof LanguageSettingsPlace) {
            LanguageSettingsPlace languageSettingsPlace = (LanguageSettingsPlace) place;
            ClientProjectComponent projectComponent = applicationComponent.getClientProjectComponent(
                    new ClientProjectModule(languageSettingsPlace.getProjectId()));
            return LanguageSettingsActivity.get(languageSettingsPlace,
                                                projectComponent.getLanguageSettingsPresenter());
        }
        if(place instanceof ProjectPrefixDeclarationsPlace) {
            ProjectPrefixDeclarationsPlace projectPrefixDeclarationsPlace = (ProjectPrefixDeclarationsPlace) place;
            ClientProjectComponent projectComponent = applicationComponent.getClientProjectComponent(
                    new ClientProjectModule(projectPrefixDeclarationsPlace.getProjectId())
            );
            return new ProjectPrefixDeclarationsActivity(projectPrefixDeclarationsPlace.getProjectId(),
                                                         projectComponent.getProjectPrefixesPresenter());
        }
        if(place instanceof ProjectTagsPlace) {
            ProjectTagsPlace projectTagsPlace = (ProjectTagsPlace) place;
            ClientProjectComponent projectComponent = applicationComponent.getClientProjectComponent(
                    new ClientProjectModule(projectTagsPlace.getProjectId())
            );
            return new ProjectTagsActivity(projectTagsPlace.getProjectId(),
                                           projectComponent.getProjectTagsPresenter(),
                                           projectTagsPlace.getNextPlace());
        }

        if(place instanceof ProjectListPlace) {
            return new ProjectListActivity(projectManagerPresenter);
        }

        if(place instanceof ProjectViewPlace) {
            ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
            ProjectPresenter presenter = getProjectPresenter(projectViewPlace);
            lastProjectPresenter = Optional.of(presenter);
            lastUser = Optional.of(loggedInUserProvider.getCurrentUserId());
            return new ProjectViewActivity(presenter, projectViewPlace);
        }

        if(place instanceof SharingSettingsPlace) {
            SharingSettingsPlace sharingSettingsPlace = (SharingSettingsPlace) place;
            ProjectId projectId = sharingSettingsPlace.getProjectId();
            ClientProjectComponent projectComponent = applicationComponent.getClientProjectComponent(new ClientProjectModule(projectId));
            SharingSettingsPresenter presenter = projectComponent.getSharingSettingsPresenter();
            return new SharingSettingsActivity(presenter, sharingSettingsPlace);
        }

        if(place instanceof CollectionViewPlace) {
            CollectionViewPlace collectionViewPlace = (CollectionViewPlace) place;
            CollectionPresenter collectionPresenter = getCollectionPresenter(collectionViewPlace);
            return new CollectionViewActivity(collectionPresenter, collectionViewPlace);
        }

        return null;
    }

    private ProjectPresenter getProjectPresenter(ProjectViewPlace projectViewPlace) {
        if(lastProjectPresenter.isPresent()) {
            ProjectId lastProjectId = lastProjectPresenter.get().getProjectId();
            if(lastProjectId.equals(projectViewPlace.getProjectId()) && lastUser.equals(Optional.of(loggedInUserProvider.getCurrentUserId()))) {
                GWT.log("[WebProtegeActivityMapper] Presenter for place is already being displayed for current user");
                return lastProjectPresenter.get();
            }
            else {
                GWT.log("[WebProtegeActivityMapper] Different place.  Disposing of previous place.");
                lastProjectPresenter.get().dispose();
            }
        }
        ProjectIdProvider.setProjectId(projectViewPlace.getProjectId());
        GWT.log("[WebProtegeActivityMapper] Instantiating project presenter for " + projectViewPlace.getProjectId());
        ClientProjectComponent projectComponent = applicationComponent.getClientProjectComponent(
                new ClientProjectModule(projectViewPlace.getProjectId()));
        return projectComponent.getProjectPresenter();
    }

    private CollectionPresenter getCollectionPresenter(CollectionViewPlace place) {
        if(lastCollectionPresenter.isPresent()) {
            GWT.log("[WebProtegeActivityMapper] Reusing collection presenter");
            return lastCollectionPresenter.get();
        }
        else {
            GWT.log("[WebProtegeActivityMapper] Instantiating collection presenter");
            ClientProjectComponent projectComponent = applicationComponent.getClientProjectComponent(new ClientProjectModule(place.getProjectId()));
            CollectionPresenter collectionPresenter = projectComponent.getCollectionPresenter();
            lastCollectionPresenter = Optional.of(collectionPresenter);
            return collectionPresenter;
        }
    }


}
