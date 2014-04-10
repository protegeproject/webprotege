package edu.stanford.bmir.protege.web.shared.watches;

import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class EntityFrameWatch implements EntityBasedWatch {

    private OWLEntity entity;

    public EntityFrameWatch(OWLEntity entity) {
        this.entity = entity;
    }

    /**
     * For serialization only
     */
    private EntityFrameWatch() {
    }

    public OWLEntity getEntity() {
        return entity;
    }

    @Override
    public OWLEntity getWatchedObject() {
        return entity;
    }

    @Override
    public int hashCode() {
        return "EntityFrameWatch".hashCode() + entity.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EntityFrameWatch)) {
            return false;
        }
        EntityFrameWatch other = (EntityFrameWatch) obj;
        return this.entity.equals(other.entity);
    }
}
