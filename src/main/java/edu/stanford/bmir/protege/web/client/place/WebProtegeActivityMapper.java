package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.client.admin.AdminPresenter;
import edu.stanford.bmir.protege.web.client.inject.ClientApplicationComponent;
import edu.stanford.bmir.protege.web.client.inject.ClientProjectComponent;
import edu.stanford.bmir.protege.web.client.inject.ClientProjectModule;
import edu.stanford.bmir.protege.web.client.inject.ProjectIdProvider;
import edu.stanford.bmir.protege.web.client.login.LoginPlace;
import edu.stanford.bmir.protege.web.client.login.LoginPresenter;
import edu.stanford.bmir.protege.web.client.project.ProjectPresenter;
import edu.stanford.bmir.protege.web.client.projectmanager.ProjectManagerPresenter;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsPresenter;
import edu.stanford.bmir.protege.web.client.signup.SignUpPresenter;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.place.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSettingsActivity;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSettingsPlace;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class WebProtegeActivityMapper implements ActivityMapper {

    private final ClientApplicationComponent applicationComponent;

    private final Provider<ProjectManagerPresenter> projectListPresenterProvider;

    private final Provider<LoginPresenter> loginPresenterProvider;

    private final Provider<SignUpPresenter> signUpPresenterProvider;

    private final AdminPresenter adminPresenter;

    private final LoggedInUserProvider loggedInUserProvider;

    private final PlaceController placeController;

    private Optional<ProjectPresenter> lastProjectPresenter = Optional.empty();

    private Optional<UserId> lastUser = Optional.empty();

    @Inject
    public WebProtegeActivityMapper(LoggedInUserProvider loggedInUserProvider,
                                    ClientApplicationComponent applicationComponent,
                                    Provider<ProjectManagerPresenter> projectListPresenterProvider,
                                    Provider<LoginPresenter> loginPresenterProvider,
                                    Provider<SignUpPresenter> signUpPresenterProvider,
                                    AdminPresenter adminPresenter,
                                    PlaceController placeController) {
        this.applicationComponent = applicationComponent;
        this.loggedInUserProvider = loggedInUserProvider;
        this.projectListPresenterProvider = projectListPresenterProvider;
        this.signUpPresenterProvider = signUpPresenterProvider;
        this.loginPresenterProvider = loginPresenterProvider;
        this.adminPresenter = adminPresenter;
        this.placeController = placeController;
    }

    public Activity getActivity(final Place place) {
        GWT.log("[WebProtegeActivityMapper] Map place: " + place);
        if(!(place instanceof LoginPlace) && !(place instanceof SignUpPlace) && loggedInUserProvider.getCurrentUserId().isGuest()) {
            GWT.log("[WebProtegeActivityMapper] User is not logged in.  Redirecting to login.");
            LoginPresenter presenter = loginPresenterProvider.get();
            presenter.setNextPlace(place);
            Scheduler.get().scheduleFinally(() -> placeController.goTo(new LoginPlace(place)));
            return new LoginActivity(presenter);
        }
        if(place instanceof AdminPlace) {
            return new AdminActivity(adminPresenter);
        }
        if(place instanceof LoginPlace) {
            LoginPresenter presenter = loginPresenterProvider.get();
            LoginPlace loginPlace = (LoginPlace) place;
            Optional<Place> continueTo = loginPlace.getContinueTo();
            if (continueTo.isPresent()) {
                presenter.setNextPlace(continueTo.get());
            }
            else {
                presenter.setNextPlace(new ProjectListPlace());
            }
            return new LoginActivity(presenter);
        }

        if(place instanceof SignUpPlace) {
            SignUpPresenter signUpPresenter = signUpPresenterProvider.get();
            SignUpPlace signUpPlace = (SignUpPlace) place;
            Optional<Place> continueTo = signUpPlace.getContinueTo();
            if(continueTo.isPresent()) {
                signUpPresenter.setContinueTo(continueTo.get());
            }
            return new SignUpActivity(signUpPresenter);
        }

        if(place instanceof ProjectListPlace) {
            return new ProjectListActivity(projectListPresenterProvider.get());
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

}
