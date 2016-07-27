package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class AnnotationPropertyHierarchyParentAddedEvent extends HierarchyChangedEvent<OWLAnnotationProperty, AnnotationPropertyHierarchyParentAddedHandler> {

    public transient static final Event.Type<AnnotationPropertyHierarchyParentAddedHandler> TYPE = new Event.Type<AnnotationPropertyHierarchyParentAddedHandler>();


    public AnnotationPropertyHierarchyParentAddedEvent(ProjectId source, OWLAnnotationProperty child, OWLAnnotationProperty parent, HierarchyId<OWLAnnotationProperty> hierarchyId) {
        super(source, child, parent, hierarchyId);
    }

    /**
     * For serialization only
     */
    private AnnotationPropertyHierarchyParentAddedEvent() {
    }

    @Override
    public Event.Type<AnnotationPropertyHierarchyParentAddedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnnotationPropertyHierarchyParentAddedHandler handler) {
        handler.handleAnnotationPropertyHierarchyParentAdded(this);
    }
}
