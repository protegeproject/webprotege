package edu.stanford.bmir.protege.web.client.issues;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_OBJECT_COMMENT;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
@Portlet(id = "portlets.CommentedEntities",
         title = "Commented entities",
         tooltip = "Displays a list of commented entities")
public class CommentedEntitiesPortletPresenter extends AbstractWebProtegePortletPresenter {

    @Nonnull
    private final CommentedEntitiesPresenter presenter;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final Messages messages;

    private Optional<PortletUi> portletUi = Optional.empty();

    @Inject
    public CommentedEntitiesPortletPresenter(@Nonnull SelectionModel selectionModel,
                                             @Nonnull ProjectId projectId,
                                             @Nonnull CommentedEntitiesPresenter presenter,
                                             @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                             @Nonnull Messages messages) {
        super(selectionModel, projectId);
        this.presenter = presenter;
        this.permissionChecker = permissionChecker;
        this.messages = messages;
    }

    @Override
    public void startPortlet(final PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setForbiddenMessage(messages.discussionThread_ViewingForbidden());
        permissionChecker.hasPermission(VIEW_OBJECT_COMMENT,
                                        canView -> {
                                            if (canView) {
                                                presenter.start(portletUi, eventBus);
                                                portletUi.setForbiddenVisible(false);
                                            }
                                            else {
                                                portletUi.setForbiddenVisible(true);
                                            }
                                        });
        eventBus.addProjectEventHandler(getProjectId(),
                                        ON_PERMISSIONS_CHANGED,
                                        event -> permissionChecker.hasPermission(VIEW_OBJECT_COMMENT,
                                                                                 canView -> portletUi.setForbiddenVisible(!canView)));
    }
}
