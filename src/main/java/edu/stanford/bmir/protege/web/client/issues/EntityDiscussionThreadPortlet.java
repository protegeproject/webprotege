package edu.stanford.bmir.protege.web.client.issues;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@Portlet(id = "portlets.Comments", title = "Comments", tooltip = "Displays comments for the selected entity")
public class EntityDiscussionThreadPortlet extends AbstractWebProtegePortlet {

    @Nonnull
    private final DiscussionThreadPresenter presenter;

    @Inject
    public EntityDiscussionThreadPortlet(SelectionModel selectionModel,
                                         EventBus eventBus,
                                         LoggedInUserProvider loggedInUserProvider,
                                         ProjectId projectId,
                                         @Nonnull DiscussionThreadPresenter presenter) {
        super(selectionModel, eventBus, loggedInUserProvider, projectId);
        this.presenter = presenter;
        this.presenter.installActions(this);
        setWidget(presenter.getView());
    }

    @Override
    public void handlePermissionsChanged() {
    }

    @Override
    public void handleActivated() {
        presenter.start();
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        // TODO: Set title
        if(entity.isPresent()) {
            presenter.setEntity(entity.get());
        }
        else {
            presenter.clear();
        }
    }
}
