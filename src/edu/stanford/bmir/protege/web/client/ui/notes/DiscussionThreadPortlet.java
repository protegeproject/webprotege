package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;
import java.util.Collections;

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
        Optional<OWLEntityData> selectedEntity = getSelectedEntityData();
        if(selectedEntity.isPresent()) {
            presenter.setTarget(selectedEntity.get().getEntity());
            setTitle("Discussions for " + selectedEntity.get().getBrowserText());
        }
        else {
            presenter.clearTarget();
            setTitle("Nothing selected");
        }
    }

    @Override
    public void initialize() {
        presenter = new DiscussionThreadPresenter(getProjectId());
        final ScrollPanel scrollPanel = new ScrollPanel(presenter.getWidget());
        scrollPanel.setHeight("100%");
        add(scrollPanel);

    }

    @Override
    public Collection<EntityData> getSelection() {
        return Collections.emptyList();
    }
}
