package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/03/2013
 */
public class ClassHierarchyParentRemovedEvent extends HierarchyChangedEvent<OWLClass, ClassHierarchyParentRemovedHandler> {

    public static final transient Type<ClassHierarchyParentRemovedHandler> TYPE = new Type<ClassHierarchyParentRemovedHandler>();

    public ClassHierarchyParentRemovedEvent(ProjectId source, OWLClass child, OWLClass parent, HierarchyId<OWLClass> hierarchyId) {
        super(source, child, parent, hierarchyId);
    }

    private ClassHierarchyParentRemovedEvent() {
    }

    @Override
    public Type<ClassHierarchyParentRemovedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ClassHierarchyParentRemovedHandler handler) {
        handler.handleClassHierarchyParentRemoved(this);
    }


    @Override
    public String toString() {
        return toStringHelper("ClassHierarchyParentRemovedEvent")
                .add("child", getChild())
                .add("parent", getParent())
                .toString();
    }
}
