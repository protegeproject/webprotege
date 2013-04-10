package edu.stanford.bmir.protege.web.shared.hierarchy;


import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public abstract class HierarchyRelationshipAddedEvent<T extends Serializable, H> extends HierarchyChangedEvent<T, H> {

    public HierarchyRelationshipAddedEvent(ProjectId source, T child, T parent, HierarchyId<T> hierarchyId) {
        super(source, child, parent, hierarchyId);
    }

    protected HierarchyRelationshipAddedEvent() {
    }

//    @Override
//    public int hashCode() {
//        return "HierarchyRelationshipAddedEvent".hashCode() + getHierarchyId().hashCode() + getChild().hashCode() + getParent().hashCode() + getProjectId().hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if(obj == this) {
//            return true;
//        }
//        if(!(obj instanceof HierarchyRelationshipAddedEvent)) {
//            return false;
//        }
//        HierarchyRelationshipAddedEvent other = (HierarchyRelationshipAddedEvent) obj;
//        return this.getHierarchyId().equals(other.getHierarchyId()) && this.getChild().equals(other.getChild()) && this.getParent().equals(other.getParent()) && this.getProjectId().equals(other.getProjectId());
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("HierarchyRelationshipAddedEvent");
//        sb.append("(");
//        sb.append(getHierarchyId());
//        sb.append(" ");
//        sb.append(getChild());
//        sb.append(" ");
//        sb.append(getParent());
//        sb.append(" ");
//        sb.append(getProjectId());
//        sb.append(")");
//        return sb.toString();
//    }
}
