package edu.stanford.bmir.protege.web.shared.watches;

import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class HierarchyBranchWatch implements EntityBasedWatch {

    private OWLEntity ancestorEntity;

    public HierarchyBranchWatch(OWLEntity ancestorEntity) {
        this.ancestorEntity = ancestorEntity;
    }

    /**
     * For serialization only
     */
    private HierarchyBranchWatch() {

    }

    @Override
    public OWLEntity getWatchedObject() {
        return ancestorEntity;
    }

    @Override
    public OWLEntity getEntity() {
        return ancestorEntity;
    }

    @Override
    public int hashCode() {
        return "HierarchyBranchWatch".hashCode() + ancestorEntity.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof HierarchyBranchWatch)) {
            return false;
        }
        HierarchyBranchWatch other = (HierarchyBranchWatch) obj;
        return this.ancestorEntity.equals(other.ancestorEntity);
    }
}
