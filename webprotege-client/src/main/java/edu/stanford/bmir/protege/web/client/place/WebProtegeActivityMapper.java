package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.ApplicationSettingsPresenter;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.form.*;
import edu.stanford.bmir.protege.web.client.inject.ClientApplicationComponent;
import edu.stanford.bmir.protege.web.client.inject.ClientProjectComponent;
import edu.stanford.bmir.protege.web.client.inject.ClientProjectModule;
import edu.stanford.bmir.protege.web.client.perspective.PerspectivesManagerActivity;
import edu.stanford.bmir.protege.web.client.perspective.PerspectivesManagerPlace;
import edu.stanford.bmir.protege.web.client.perspective.PerspectivesManagerPresenter;
import edu.stanford.bmir.protege.web.client.search.EntitySearchSettingsActivity;
import edu.stanford.bmir.protege.web.client.search.EntitySearchSettingsPresenter;
import edu.stanford.bmir.protege.web.client.tag.ProjectTagsActivity;
import edu.stanford.bmir.protege.web.shared.login.LoginPlace;
import edu.stanford.bmir.protege.web.client.login.LoginPresenter;
import edu.stanford.bmir.protege.web.client.project.ProjectPresenter;
import edu.stanford.bmir.protege.web.client.projectmanager.ProjectManagerPresenter;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsPresenter;
import edu.stanford.bmir.protege.web.client.signup.SignUpPresenter;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.place.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsActivity;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsActivity;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSettingsPlace;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class WebProtegeActivityMapper implements ActivityMapper {

    private final ClientApplicationComponent applicationComponent;

    private Optional<ClientProjectComponent> currentClientProjectComponent = Optional.empty();

    private final ProjectManagerPresenter projectManagerPresenter;

    private final LoginPresenter loginPresenter;

    private final SignUpPresenter signUpPresenter;

    private final ApplicationSettingsPresenter applicationSettingsPresenter;

    private final LoggedInUserProvider loggedInUserProvider;

    private final PlaceController placeController;

    private final EventBus eventBus;

    private Optional<UserId> lastUser = Optional.empty();

    @Inject
    public WebProtegeActivityMapper(LoggedInUserProvider loggedInUserProvider,
                                    ClientApplicationComponent applicationComponent,
                                    ProjectManagerPresenter projectListPresenter,
                                    LoginPresenter loginPresenter,
                                    SignUpPresenter signUpPresenter,
                                    ApplicationSettingsPresenter applicationSettingsPresenter,
                                    PlaceController placeController,
                                    EventBus eventBus) {
        this.applicationComponent = applicationComponent;
        this.loggedInUserProvider = loggedInUserProvider;
        this.projectManagerPresenter = projectListPresenter;
        this.signUpPresenter = signUpPresenter;
        this.loginPresenter = loginPresenter;
        this.applicationSettingsPresenter = applicationSettingsPresenter;
        this.placeController = placeController;
        this.eventBus = eventBus;
    }

    public void start() {
        GWT.log("[WebProtegeActivityMapper] Started activity mapper.");
        eventBus.addHandler(UserLoggedOutEvent.ON_USER_LOGGED_OUT, event -> {
            GWT.log("[WebProtegeActivityMapper] User logged out.  Going to the Login Place.");
            LoginPlace loginPlace;
            Place currentPlace = placeController.getWhere();
            if (!(currentPlace instanceof LoginPlace)) {
                loginPlace = new LoginPlace(placeController.getWhere());
            }
            else {
                loginPlace = new LoginPlace();
            }
            placeController.goTo(loginPlace);
        });
    }

    private ClientProjectComponent getClientProjectComponentForProjectAndLoggedInUser(@Nonnull ProjectId projectId) {
        if(currentClientProjectComponent.isPresent()) {
            ClientProjectComponent projectComponent = currentClientProjectComponent.get();
            if(isProjectComponentForProject(projectComponent, projectId) && isLastUserSameAsLoggedInUser()) {
                return projectComponent;
            }
            projectComponent.getProjectPresenter().dispose();
        }
        ClientProjectComponent nextProjectComponent = instantiateClientProjectComponent(projectId);
        // Reset project component and user
        GWT.log("[WebProtegeActivityMapper] Instantiating new project component");
        lastUser = Optional.of(loggedInUserProvider.getCurrentUserId());
        currentClientProjectComponent = Optional.of(nextProjectComponent);
        return nextProjectComponent;
    }

    private boolean isProjectComponentForProject(ClientProjectComponent projectComponent,
                                                 @Nonnull ProjectId projectId) {
        return projectComponent.getProjectId().equals(projectId);
    }

    private boolean isLastUserSameAsLoggedInUser() {
        return lastUser.equals(Optional.of(loggedInUserProvider.getCurrentUserId()));
    }

    private ClientProjectComponent instantiateClientProjectComponent(@Nonnull ProjectId projectId) {
        return applicationComponent.getClientProjectComponent(new ClientProjectModule(projectId));
    }

    public Activity getActivity(final Place place) {
        GWT.log("[WebProtegeActivityMapper] Map place: " + place);
        if (shouldRedirectToLogin(place)) {
            GWT.log("[WebProtegeActivityMapper] User is not logged in.  Redirecting to login.");
            loginPresenter.setNextPlace(place);
            Scheduler.get().scheduleFinally(() -> placeController.goTo(new LoginPlace(place)));
            return new LoginActivity(loginPresenter);
        }
        if (place instanceof ApplicationSettingsPlace) {
            return new AdminActivity(applicationSettingsPresenter);
        }
        if (place instanceof ProjectSettingsPlace) {
            ProjectSettingsPlace projectSettingsPlace = (ProjectSettingsPlace) place;
            ClientProjectComponent projectComponent = getClientProjectComponentForProjectAndLoggedInUser(projectSettingsPlace.getProjectId());
            return new ProjectSettingsActivity(projectComponent.getProjectSettingsPresenter(),
                                               projectSettingsPlace.getNextPlace());
        }
        if (place instanceof LanguageSettingsPlace) {
            LanguageSettingsPlace languageSettingsPlace = (LanguageSettingsPlace) place;
            ClientProjectComponent projectComponent = getClientProjectComponentForProjectAndLoggedInUser(languageSettingsPlace.getProjectId());
            return LanguageSettingsActivity.get(languageSettingsPlace, projectComponent.getLanguageSettingsPresenter());
        }
        if (place instanceof ProjectPrefixDeclarationsPlace) {
            ProjectPrefixDeclarationsPlace projectPrefixDeclarationsPlace = (ProjectPrefixDeclarationsPlace) place;
            ClientProjectComponent projectComponent = getClientProjectComponentForProjectAndLoggedInUser(projectPrefixDeclarationsPlace.getProjectId());
            return new ProjectPrefixDeclarationsActivity(projectPrefixDeclarationsPlace.getProjectId(),
                                                         projectComponent.getProjectPrefixesPresenter());
        }
        if (place instanceof ProjectTagsPlace) {
            ProjectTagsPlace projectTagsPlace = (ProjectTagsPlace) place;
            ClientProjectComponent projectComponent = getClientProjectComponentForProjectAndLoggedInUser(projectTagsPlace.getProjectId());
            return new ProjectTagsActivity(projectTagsPlace.getProjectId(),
                                           projectComponent.getProjectTagsPresenter(),
                                           projectTagsPlace.getNextPlace());
        }
        if (place instanceof LoginPlace) {
            if (!loggedInUserProvider.getCurrentUserId().isGuest()) {
                Scheduler.get().scheduleFinally(() -> placeController.goTo(new ProjectListPlace()));
            }
            else {
                LoginPlace loginPlace = (LoginPlace) place;
                Optional<Place> continueTo = loginPlace.getContinueTo();
                if (continueTo.isPresent()) {
                    loginPresenter.setNextPlace(continueTo.get());
                }
                else {
                    loginPresenter.setNextPlace(new ProjectListPlace());
                }
                return new LoginActivity(loginPresenter);
            }
        }

        if (place instanceof SignUpPlace) {
            SignUpPlace signUpPlace = (SignUpPlace) place;
            Optional<Place> continueTo = signUpPlace.getContinueTo();
            continueTo.ifPresent(signUpPresenter::setContinueTo);
            return new SignUpActivity(signUpPresenter);
        }

        if (place instanceof ProjectListPlace) {
            return new ProjectListActivity(projectManagerPresenter);
        }

        if (place instanceof ProjectViewPlace) {
            ProjectViewPlace projectViewPlace = (ProjectViewPlace) place;
            ProjectPresenter presenter = getProjectPresenter(projectViewPlace);
            lastUser = Optional.of(loggedInUserProvider.getCurrentUserId());
            return new ProjectViewActivity(presenter, projectViewPlace);
        }

        if (place instanceof SharingSettingsPlace) {
            SharingSettingsPlace sharingSettingsPlace = (SharingSettingsPlace) place;
            ProjectId projectId = sharingSettingsPlace.getProjectId();
            ClientProjectComponent projectComponent = getClientProjectComponentForProjectAndLoggedInUser(projectId);
            SharingSettingsPresenter presenter = projectComponent.getSharingSettingsPresenter();
            return new SharingSettingsActivity(presenter, sharingSettingsPlace);
        }

        if (place instanceof FormsPlace) {
            FormsPlace formsPlace = (FormsPlace) place;
            FormsManagerPresenter formsManagerPresenter = getFormsPresenter(formsPlace);
            return new FormsActivity(formsManagerPresenter, formsPlace);
        }

        if (place instanceof EditFormPlace) {
            EditFormPlace editFormPlace = (EditFormPlace) place;
            FormEditorPresenter formEditorPresenter = getFormEditorPresenter(editFormPlace);
            return new EditFormActivity(formEditorPresenter, editFormPlace);
        }

        if (place instanceof EntitySearchSettingsPlace) {
            EntitySearchSettingsPlace searchSettingsPlace = (EntitySearchSettingsPlace) place;
            EntitySearchSettingsPresenter entitySearchSettingsPresenter = getEntitySearchSettingsPresenter(
                    searchSettingsPlace);
            return EntitySearchSettingsActivity.get(entitySearchSettingsPresenter, searchSettingsPlace.getNextPlace());
        }

        if(place instanceof PerspectivesManagerPlace) {
            PerspectivesManagerPlace perspectivesManagerPlace = (PerspectivesManagerPlace) place;
            PerspectivesManagerPresenter perspectivesManagerPresenter = getPerspectivesManagerPresenter(perspectivesManagerPlace);
            return PerspectivesManagerActivity.get(perspectivesManagerPlace.getProjectId(),
                                                   perspectivesManagerPlace.getNextPlace().orElse(null),
                                                   perspectivesManagerPresenter);
        }

        return null;
    }

    private boolean shouldRedirectToLogin(Place requestedPlace) {
        return !(requestedPlace instanceof LoginPlace) && !(requestedPlace instanceof SignUpPlace) && loggedInUserProvider
                .getCurrentUserId()
                .isGuest();
    }

    private ProjectPresenter getProjectPresenter(ProjectViewPlace projectViewPlace) {
        ClientProjectComponent projectComponent = getClientProjectComponentForProjectAndLoggedInUser(projectViewPlace.getProjectId());
        return projectComponent.getProjectPresenter();
    }

    private FormsManagerPresenter getFormsPresenter(FormsPlace place) {
        ClientProjectComponent projectComponent = getClientProjectComponentForProjectAndLoggedInUser(place.getProjectId());
        return projectComponent.getFormsPresenter();
    }

    private FormEditorPresenter getFormEditorPresenter(EditFormPlace place) {
        ClientProjectComponent projectComponent = getClientProjectComponentForProjectAndLoggedInUser(place.getProjectId());
        return projectComponent.getFormEditorPresenter();
    }

    private EntitySearchSettingsPresenter getEntitySearchSettingsPresenter(EntitySearchSettingsPlace place) {
        ClientProjectComponent projectComponent = getClientProjectComponentForProjectAndLoggedInUser(place.getProjectId());
        return projectComponent.getEntitySearchSettingsPresenter();
    }

    private PerspectivesManagerPresenter getPerspectivesManagerPresenter(PerspectivesManagerPlace place) {
        ClientProjectComponent projectComponent = getClientProjectComponentForProjectAndLoggedInUser(place.getProjectId());
        return projectComponent.getPerspectivesManagerPresenter();
    }

}
