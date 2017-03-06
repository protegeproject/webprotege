package edu.stanford.bmir.protege.web.client.sharing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSettingsPlace;

import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_SHARING_SETTINGS;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class SharingButtonPresenter implements HasDispose {

    public static final Messages MESSAGES = GWT.create(Messages.class);

    private final ProjectId projectId;

    private final PlaceController placeController;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final Button button;

    @Inject
    public SharingButtonPresenter(ProjectId projectId,
                                  PlaceController placeController,
                                  LoggedInUserProjectPermissionChecker permissionChecker) {
        this.projectId = projectId;
        this.placeController = placeController;
        this.permissionChecker = permissionChecker;
        button = new Button(MESSAGES.share(), (ClickHandler) event -> goToSharingSettingsPlace());
        button.asWidget().addStyleName(BUNDLE.buttons().btn());
        button.asWidget().addStyleName(BUNDLE.buttons().topBarButton());
    }

    public void start(final AcceptsOneWidget container, EventBus eventBus) {
        eventBus.addHandlerToSource(ON_PERMISSIONS_CHANGED,
                                    projectId,
                                    event -> updateButtonState(container));
        eventBus.addHandler(UserLoggedOutEvent.TYPE,
                            event -> updateButtonState(container));
        eventBus.addHandler(UserLoggedInEvent.TYPE,
                            event -> updateButtonState(container));
        updateButtonState(container);
    }

    private void updateButtonState(AcceptsOneWidget container) {
        permissionChecker.hasPermission(EDIT_SHARING_SETTINGS, canEdit -> {
            GWT.log("[SharingButtonPresenter] Signed in user can edit sharing settings: " + canEdit);
            button.setVisible(canEdit);
            container.setWidget(button);
        });
    }

    private void goToSharingSettingsPlace() {
        SharingSettingsPlace newPlace = new SharingSettingsPlace(projectId);
        newPlace.setContinueTo(Optional.of(placeController.getWhere()));
        placeController.goTo(newPlace);
    }


    public void dispose() {
    }

}
