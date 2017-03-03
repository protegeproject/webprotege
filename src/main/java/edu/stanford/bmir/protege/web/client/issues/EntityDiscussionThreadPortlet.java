package edu.stanford.bmir.protege.web.client.issues;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.app.ForbiddenView;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_OBJECT_COMMENTS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@Portlet(id = "portlets.Comments", title = "Comments", tooltip = "Displays comments for the selected entity")
public class EntityDiscussionThreadPortlet extends AbstractWebProtegePortlet {

    @Nonnull
    private final DiscussionThreadListPresenter presenter;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final ForbiddenView forbiddenView;

    @Inject
    public EntityDiscussionThreadPortlet(SelectionModel selectionModel,
                                         EventBus eventBus,
                                         LoggedInUserProvider loggedInUserProvider,
                                         LoggedInUserProjectPermissionChecker permissionChecker,
                                         ProjectId projectId,
                                         @Nonnull DiscussionThreadListPresenter presenter,
                                         @Nonnull ForbiddenView forbiddenView) {
        super(selectionModel, eventBus, projectId);
        this.presenter = presenter;
        this.presenter.installActions(this);
        this.forbiddenView = forbiddenView;
        this.permissionChecker = permissionChecker;
        forbiddenView.setSubMessage("You do not have permission to view comments in this project");
        setFilter(presenter.getFilterView());
        setWidget(presenter.getView());
    }

    @Override
    public void handlePermissionsChanged() {

    }

    @Override
    public void handleActivated() {
        presenter.start();
        updatePresenter(getSelectedEntity());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        updatePresenter(entity);
    }

    private void updatePresenter(Optional<OWLEntity> entity) {
        permissionChecker.hasPermission(VIEW_OBJECT_COMMENTS, canViewComments -> {
            if(canViewComments) {
                setToolbarVisible(true);
                setWidget(presenter.getView());
                if(entity.isPresent()) {
                    presenter.setEntity(entity.get());
                }
                else {
                    presenter.clear();
                }
            }
            else {
                setToolbarVisible(false);
                setWidget(forbiddenView);
            }
        });

    }
}
