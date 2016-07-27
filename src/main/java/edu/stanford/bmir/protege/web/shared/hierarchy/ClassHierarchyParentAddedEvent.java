package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/03/2013
 */
public class ClassHierarchyParentAddedEvent extends HierarchyChangedEvent<OWLClass, ClassHierarchyParentAddedHandler> {

    public static final transient Event.Type<ClassHierarchyParentAddedHandler> TYPE = new Event.Type<ClassHierarchyParentAddedHandler>();

    public ClassHierarchyParentAddedEvent(ProjectId source, OWLClass child, OWLClass parent, HierarchyId<OWLClass> hierarchyId) {
        super(source, child, parent, hierarchyId);
    }

    private ClassHierarchyParentAddedEvent() {
    }

    @Override
    public Event.Type<ClassHierarchyParentAddedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ClassHierarchyParentAddedHandler handler) {
        handler.handleClassHierarchyParentAdded(this);
    }
}
