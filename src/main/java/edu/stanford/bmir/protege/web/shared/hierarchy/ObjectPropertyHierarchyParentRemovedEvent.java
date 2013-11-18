package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class ObjectPropertyHierarchyParentRemovedEvent extends HierarchyChangedEvent<OWLObjectProperty, ObjectPropertyHierarchyParentRemovedHandler> {

    public transient static final Type<ObjectPropertyHierarchyParentRemovedHandler> TYPE = new Type<ObjectPropertyHierarchyParentRemovedHandler>();

    public ObjectPropertyHierarchyParentRemovedEvent(ProjectId source, OWLObjectProperty child, OWLObjectProperty parent, HierarchyId<OWLObjectProperty> hierarchyId) {
        super(source, child, parent, hierarchyId);
    }

    private ObjectPropertyHierarchyParentRemovedEvent() {
    }

    @Override
    public Type<ObjectPropertyHierarchyParentRemovedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ObjectPropertyHierarchyParentRemovedHandler handler) {
        handler.handleObjectPropertyHierarchyParentRemoved(this);
    }
}
