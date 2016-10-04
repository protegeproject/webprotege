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
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSettingsPlace;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class SharingButtonPresenter {

    public static final Messages MESSAGES = GWT.create(Messages.class);

    private final ProjectId projectId;

    private final PlaceController placeController;

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final Button button;

    @Inject
    public SharingButtonPresenter(ProjectId projectId, PlaceController placeController, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
        this.projectId = projectId;
        this.placeController = placeController;
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
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
        dispatchServiceManager.execute(new GetPermissionsAction(projectId, loggedInUserProvider.getCurrentUserId()), new DispatchServiceCallback<GetPermissionsResult>() {
            @Override
            public void handleSuccess(GetPermissionsResult result) {
                if(result.getPermissionsSet().contains(Permission.getAdminPermission())) {
                    displayButton(container);
                }
                else {
                    button.removeFromParent();
                }
            }
        });

    }

    private void displayButton(AcceptsOneWidget container) {
        container.setWidget(button);
    }

    private void goToSharingSettingsPlace() {
        SharingSettingsPlace newPlace = new SharingSettingsPlace(projectId);
        newPlace.setContinueTo(Optional.<Place>of(placeController.getWhere()));
        placeController.goTo(newPlace);
    }
}
