package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public abstract class HierarchyChangedEvent<T extends OWLEntity, H> extends ProjectEvent<H> implements HasSignature {

    private T child;

    private T parent;

    private HierarchyId<T> hierarchyId;

    public HierarchyChangedEvent(ProjectId source, T child, T parent, HierarchyId<T> hierarchyId) {
        super(source);
        this.child = child;
        this.parent = parent;
        this.hierarchyId = hierarchyId;
    }

    protected HierarchyChangedEvent() {
    }

    @SuppressWarnings("unchecked")
    public T getChild() {
        return child;
    }

    @SuppressWarnings("unchecked")
    public T getParent() {
        return parent;
    }

    public HierarchyId<?> getHierarchyId() {
        return hierarchyId;
    }

    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> result = new HashSet<OWLEntity>();
        if(child instanceof OWLEntity) {
            result.add((OWLEntity) child);
        }
        if(parent instanceof OWLEntity) {
            result.add((OWLEntity) parent);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return "HierarchyChangedEvent".hashCode() + getProjectId().hashCode() + child.hashCode() + parent.hashCode() + hierarchyId.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof HierarchyChangedEvent)) {
            return false;
        }
        HierarchyChangedEvent other = (HierarchyChangedEvent) obj;
        return this.getProjectId().equals(other.getProjectId()) && this.child.equals(other.child) && this.parent.equals(other.parent) && this.hierarchyId.equals(other.hierarchyId);
    }
}
