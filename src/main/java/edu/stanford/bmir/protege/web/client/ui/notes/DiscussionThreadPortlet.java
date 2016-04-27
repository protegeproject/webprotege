package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
@Portlet(id = "portlets.Discussions", title = "Discussions")
public class DiscussionThreadPortlet extends AbstractWebProtegePortlet {

    private DiscussionThreadPresenter presenter;

    @Inject
    public DiscussionThreadPortlet(ProjectId projectId, EventBus eventBus, SelectionModel selectionModel, LoggedInUserProvider loggedInUserProvider, DiscussionThreadPresenter discussionThreadPresenter) {
        super(selectionModel, eventBus, loggedInUserProvider, projectId);
        presenter = discussionThreadPresenter;
        presenter.installActions(this);
        setWidget(presenter.getWidget());
        setTitle("Discussions");
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        if(entity.isPresent()) {
            presenter.setTarget(entity.get());
        }
        else {
            presenter.clearTarget();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        presenter.dispose();
    }
}
