package edu.stanford.bmir.protege.web.client.signup;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.ForbiddenView;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.login.LoginPlace;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.verification.HumanVerificationHandler;
import edu.stanford.bmir.protege.web.client.verification.HumanVerificationServiceProvider;
import edu.stanford.bmir.protege.web.client.verification.NullHumanVerificationServiceProvider;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.auth.Md5DigestAlgorithmProvider;
import edu.stanford.bmir.protege.web.shared.auth.PasswordDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.SaltProvider;
import edu.stanford.bmir.protege.web.shared.user.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_ACCOUNT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public class SignUpPresenter implements Presenter {


    private final DispatchServiceManager dispatchServiceManager;

    private final SignUpView view;

    private final ForbiddenView forbiddenView;

    private final PlaceController placeController;

    private final LoggedInUserManager permissionChecker;

    private Optional<Place> continueTo = Optional.empty();

    private Optional<Place> backTo = Optional.empty();

    @Inject
    public SignUpPresenter(@Nonnull DispatchServiceManager dispatchServiceManager,
                           @Nonnull LoggedInUserManager permissionChecker,
                           @Nonnull SignUpView view,
                           @Nonnull ForbiddenView forbiddenView,
                           @Nonnull PlaceController placeController) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.view = checkNotNull(view);
        this.forbiddenView = checkNotNull(forbiddenView);
        this.placeController = checkNotNull(placeController);
        this.permissionChecker = checkNotNull(permissionChecker);
        view.setCancelHandler(event -> handleCancel());
        view.setSignUpHandler(event -> handleSignUp());
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        if(!permissionChecker.isAllowedApplicationAction(CREATE_ACCOUNT)) {
            container.setWidget(forbiddenView);
        }
        else {
            view.clear();
            container.setWidget(view);
        }
    }

    public void setBackTo(Place backTo) {
        this.backTo = Optional.of(backTo);
    }

    public void setContinueTo(Place continueTo) {
        this.continueTo = Optional.of(continueTo);
    }

    private void handleSignUp() {
        view.clearErrorMessages();

        String userName = view.getUserName();
        if(userName.isEmpty()) {
            view.displayEnterUserNameErrorMessage();
            return;
        }

        String emailAddress = view.getEmailAddress();
        if(emailAddress.isEmpty()) {
            view.displayEnterEmailAddressErrorMessage();
            return;
        }

        String password = view.getPassword();
        if(password.isEmpty()) {
            view.displayEnterPasswordErrorMessage();
            return;
        }
        String confirmPassword = view.getConfirmPassword();
        if(confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
            view.displayConfirmPasswordDoesNotMatchErrorMessage();
            return;
        }
        final SignupInfo data = new SignupInfo(
                new EmailAddress(emailAddress),
                userName,
                password,
                new NullHumanVerificationServiceProvider()
        );

        HumanVerificationServiceProvider verificationServiceProvider = data.getVerificationServiceProvider();
        verificationServiceProvider.runVerification(new HumanVerificationHandler() {
            @Override
            public void handleVerificationSuccess() {
                handleSuccess(data);
            }

            @Override
            public void handleVerificationFailure(String errorMessage) {
                MessageBox.showAlert(errorMessage);
            }
        });
    }


    private void handleSuccess(final SignupInfo data) {
        CreateUserAccountExecutor executor = new CreateUserAccountExecutor(
                dispatchServiceManager,
                new PasswordDigestAlgorithm(new Md5DigestAlgorithmProvider()),
                new SaltProvider()
        );

        UserId userId = UserId.getUserId(data.getUserName());
        executor.execute(userId, data.getEmailAddress(), data.getPassword(), new DispatchServiceCallback<CreateUserAccountResult>() {
            @Override
            public void handleSuccess(CreateUserAccountResult createUserAccountResult) {
                MessageBox.showMessage("Registration complete",
                        "You have successfully registered.  " +
                                "Please sign in on the next page");
                goToNextPlace();
            }

            @Override
            public void handleExecutionException(Throwable cause) {
                if (cause instanceof UserNameAlreadyExistsException) {
                    String username = ((UserNameAlreadyExistsException) cause).getUsername();
                    MessageBox.showAlert("User name already taken", "A user named "
                            + username
                            + " is already registered.  Please choose another name.");
                }
                else if (cause instanceof UserEmailAlreadyExistsException) {
                    String email = ((UserEmailAlreadyExistsException) cause).getEmailAddress();
                    MessageBox.showAlert("Email address already taken", "The email address "
                            + email
                            + " is already taken.  Please choose a different email address.");
                }
                else if (cause instanceof UserRegistrationException) {
                    MessageBox.showAlert(cause.getMessage());
                }
                else {
                    MessageBox.showAlert("Error registering account",
                            "There was a problem registering the specified user account.  " +
                                    "Please contact administrator.");
                }
            }
        });
    }

    private void goToNextPlace() {
        if(continueTo.isPresent()) {
            placeController.goTo(continueTo.get());
        }
        else {
            placeController.goTo(new LoginPlace());
        }
    }


    private void handleCancel() {
        if(backTo.isPresent()) {
            placeController.goTo(backTo.get());
        }
        else {
            placeController.goTo(new LoginPlace());
        }
    }
}
