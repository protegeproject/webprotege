package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class DataPropertyHierarchyParentRemovedEvent extends HierarchyChangedEvent<OWLDataProperty, DataPropertyHierarchyParentRemovedHandler> {

    public transient static final Type<DataPropertyHierarchyParentRemovedHandler> TYPE = new Type<DataPropertyHierarchyParentRemovedHandler>();

    public DataPropertyHierarchyParentRemovedEvent(ProjectId source, OWLDataProperty child, OWLDataProperty parent, HierarchyId<OWLDataProperty> hierarchyId) {
        super(source, child, parent, hierarchyId);
    }

    private DataPropertyHierarchyParentRemovedEvent() {
    }

    @Override
    public Type<DataPropertyHierarchyParentRemovedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DataPropertyHierarchyParentRemovedHandler handler) {
        handler.handleDataPropertyHierarchyParentRemoved(this);
    }
}
