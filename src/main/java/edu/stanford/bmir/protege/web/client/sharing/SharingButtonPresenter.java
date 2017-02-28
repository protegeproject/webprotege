package edu.stanford.bmir.protege.web.client.sharing;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSettingsPlace;

import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_SHARING_SETTINGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class SharingButtonPresenter {

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
        button = new Button(MESSAGES.share(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                goToSharingSettingsPlace();
            }
        });
        button.asWidget().addStyleName(WebProtegeClientBundle.BUNDLE.buttons().btn());
        button.asWidget().addStyleName(WebProtegeClientBundle.BUNDLE.buttons().topBarButton());
    }

    public void start(final AcceptsOneWidget container) {
        permissionChecker.hasPermission(EDIT_SHARING_SETTINGS, canEdit -> {
            if(canEdit) {
                displayButton(container);
            }
            else {
                button.removeFromParent();
            }
        });
    }

    private void displayButton(AcceptsOneWidget container) {
        container.setWidget(button);
    }

    private void goToSharingSettingsPlace() {
        SharingSettingsPlace newPlace = new SharingSettingsPlace(projectId);
        newPlace.setContinueTo(Optional.of(placeController.getWhere()));
        placeController.goTo(newPlace);
    }
}
