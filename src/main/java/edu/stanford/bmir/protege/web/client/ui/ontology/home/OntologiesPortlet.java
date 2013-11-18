package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.library.common.Refreshable;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Collection;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class OntologiesPortlet extends AbstractOWLEntityPortlet implements Refreshable, HasSelectionHandlers<ProjectId> {

    public OntologiesPortlet(Project project) {
        super(project);
    }

    @Override
    public void reload() {
    }

    @Override
    public void initialize() {
    }

    /**
     * Adds a {@link com.google.gwt.event.logical.shared.SelectionEvent} handler.
     * @param handler the handler
     * @return the registration for the event
     */
    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<ProjectId> handler) {
        return null;
    }

    /**
     * Refreshes this refreshable.
     */
    @Override
    public void refresh() {
    }

    @Override
    public Collection<EntityData> getSelection() {
        return null;
    }


}
