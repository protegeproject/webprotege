package edu.stanford.bmir.protege.web.client.issues;

import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_OBJECT_COMMENTS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@Portlet(id = "portlets.Comments", title = "Comments", tooltip = "Displays comments for the selected entity")
public class EntityDiscussionThreadPortletPresenter extends AbstractWebProtegePortletPresenter {

    @Nonnull
    private final DiscussionThreadListPresenter presenter;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private Optional<PortletUi> portletUi = Optional.empty();

    @Inject
    public EntityDiscussionThreadPortletPresenter(SelectionModel selectionModel,
                                                  @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                                  @Nonnull ProjectId projectId,
                                                  @Nonnull DiscussionThreadListPresenter presenter) {
        super(selectionModel, projectId);
        this.presenter = presenter;
        this.permissionChecker = permissionChecker;
    }

    @Override
    public void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        this.portletUi = Optional.of(portletUi);
        portletUi.setWidget(presenter.getView());
        this.presenter.installActions(portletUi);
        portletUi.setFilterView(presenter.getFilterView());
        presenter.start(eventBus);
        updatePresenter(getSelectedEntity());
        eventBus.addProjectEventHandler(getProjectId(),
                                        PermissionsChangedEvent.ON_PERMISSIONS_CHANGED,
                                        this::handlePemissionsChange);
        portletUi.setForbiddenMessage("You do not have permission to view comments in this project");
    }

    private void handlePemissionsChange(PermissionsChangedEvent event) {
        handleAfterSetEntity(Optional.empty());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        updatePresenter(entity);
    }

    private void updatePresenter(Optional<OWLEntity> entity) {
        portletUi.ifPresent(portletUi -> {
            permissionChecker.hasPermission(VIEW_OBJECT_COMMENTS, canViewComments -> {
                if(canViewComments) {
                    portletUi.setForbiddenVisible(false);
                    if(entity.isPresent()) {
                        presenter.setEntity(entity.get());
                    }
                    else {
                        presenter.clear();
                    }
                }
                else {
                    portletUi.setForbiddenVisible(true);
                }
            });
        });

    }
}
