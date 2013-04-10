package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/03/2013
 */
public class DataPropertyHierarchyParentAddedEvent extends HierarchyChangedEvent<OWLDataProperty, DataPropertyHierarchyParentAddedHandler> {

    public static final transient Type<DataPropertyHierarchyParentAddedHandler> TYPE = new Type<DataPropertyHierarchyParentAddedHandler>();

    public DataPropertyHierarchyParentAddedEvent(ProjectId source, OWLDataProperty child, OWLDataProperty parent, HierarchyId<OWLDataProperty> hierarchyId) {
        super(source, child, parent, hierarchyId);
    }

    /**
     * For serialization only
     */
    private DataPropertyHierarchyParentAddedEvent() {
    }

    @Override
    public Type<DataPropertyHierarchyParentAddedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DataPropertyHierarchyParentAddedHandler handler) {
        handler.handleDataPropertyParentAdded(this);
    }
}
