package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/03/2013
 */
public class ObjectPropertyHierarchyParentAddedEvent extends HierarchyChangedEvent<OWLObjectProperty, ObjectPropertyHierarchyParentAddedHandler> {

    public static final transient Type<ObjectPropertyHierarchyParentAddedHandler> TYPE = new Type<ObjectPropertyHierarchyParentAddedHandler>();

    public ObjectPropertyHierarchyParentAddedEvent(ProjectId source, OWLObjectProperty child, OWLObjectProperty parent, HierarchyId<OWLObjectProperty> hierarchyId) {
        super(source, child, parent, hierarchyId);
    }

    /**
     * For serialization only
     */
    private ObjectPropertyHierarchyParentAddedEvent() {
    }

    @Override
    public Type<ObjectPropertyHierarchyParentAddedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ObjectPropertyHierarchyParentAddedHandler handler) {
        handler.handleObjectPropertyHierarchyParentAdded(this);
    }
}
