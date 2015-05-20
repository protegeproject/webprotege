package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class DiscussionThreadPortlet extends AbstractOWLEntityPortlet {

    private DiscussionThreadPresenter presenter;

    public DiscussionThreadPortlet(Project project, SelectionModel selectionModel) {
        super(selectionModel, project);
    }

    @Override
    public void initialize() {
        presenter = new DiscussionThreadPresenter(getProjectId());
        add(presenter.getWidget());
        setHeight(500);
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

    @Override
    protected void onDestroy() {
        presenter.dispose();
        super.onDestroy();
    }
}
