package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.notes.*;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class DiscussionThreadPortlet extends AbstractOWLEntityPortlet {

    private DiscussionThreadPresenter presenter;

    public DiscussionThreadPortlet(Project project) {
        super(project);
    }

    public DiscussionThreadPortlet(Project project, boolean initialize) {
        super(project, initialize);
    }

    @Override
    public void reload() {
        Optional<OWLEntity> selectedEntity = getSelectedEntity();
        if(selectedEntity.isPresent()) {
            presenter.setTarget(selectedEntity.get());
        }
        else {
            presenter.clearTarget();

        }
    }

    @Override
    public void initialize() {
        presenter = new DiscussionThreadPresenter(getProjectId());
        add(new ScrollPanel(presenter.getWidget()));
    }

    @Override
    public Collection<EntityData> getSelection() {
        return Collections.emptyList();
    }
}
