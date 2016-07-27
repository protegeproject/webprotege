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
public class AnnotationPropertyHierarchyParentRemovedEvent extends HierarchyChangedEvent<OWLAnnotationProperty, AnnotationPropertyHierarchyParentRemoved> {

    public transient static final Event.Type<AnnotationPropertyHierarchyParentRemoved> TYPE = new Event.Type<AnnotationPropertyHierarchyParentRemoved>();

    public AnnotationPropertyHierarchyParentRemovedEvent(ProjectId source, OWLAnnotationProperty child, OWLAnnotationProperty parent, HierarchyId<OWLAnnotationProperty> hierarchyId) {
        super(source, child, parent, hierarchyId);
    }

    private AnnotationPropertyHierarchyParentRemovedEvent() {
    }

    @Override
    public Event.Type<AnnotationPropertyHierarchyParentRemoved> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnnotationPropertyHierarchyParentRemoved handler) {
        handler.handleAnnotationPropertyHierarchyParentRemoved(this);
    }
}
