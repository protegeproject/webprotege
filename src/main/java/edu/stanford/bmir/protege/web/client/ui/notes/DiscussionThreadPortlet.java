package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class DiscussionThreadPortlet extends AbstractOWLEntityPortlet {

    private DiscussionThreadPresenter presenter;

    @Inject
    public DiscussionThreadPortlet(ProjectId projectId, EventBus eventBus, SelectionModel selectionModel, LoggedInUserProvider loggedInUserProvider, DiscussionThreadPresenter discussionThreadPresenter) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        presenter = discussionThreadPresenter;
        ScrollPanel sp = new ScrollPanel(presenter.getWidget());
        getContentHolder().setWidget(sp);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        if(entityData.isPresent()) {
            presenter.setTarget(entityData.get().getEntity());
            setTitle("Discussions for " + entityData.get().getBrowserText());
        }
        else {
            presenter.clearTarget();
            setTitle("Nothing selected");
        }
    }

//    @Override
    protected void onDestroy() {
        presenter.dispose();
//        super.onDestroy();
    }
}
