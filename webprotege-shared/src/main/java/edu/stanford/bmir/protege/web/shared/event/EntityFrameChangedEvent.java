package edu.stanford.bmir.protege.web.shared.event;

import com.google.gwt.event.shared.EventHandler;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/12/2012
 */
public abstract class EntityFrameChangedEvent<E extends OWLEntity, H extends EventHandler> extends ProjectEvent<H> implements HasSignature, Serializable {


    private E entity;

    private UserId userId;

    /**
     * For serialization purposes only
     */
    protected EntityFrameChangedEvent() {

    }

    public EntityFrameChangedEvent(E entity, ProjectId projectId, UserId userId) {
        super(projectId);
        this.entity = entity;
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    public E getEntity() {
        return entity;
    }


    /**
     * Gets the signature of the object that implements this interface.
     * @return A set of entities that represent the signature of this object
     */
    @Override
    public Set<OWLEntity> getSignature() {
        return Collections.singleton(entity);
    }

    @Override
    public int hashCode() {
        return "EntityFrameChangedEvent".hashCode() + entity.hashCode() + getSource().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EntityFrameChangedEvent)) {
            return false;
        }
        EntityFrameChangedEvent<?,?> other = (EntityFrameChangedEvent<?,?>) obj;
        return this.entity.equals(other.entity) && this.getSource().equals(other.getSource());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EntityFrameChangedEvent");
        sb.append("Entity(");
        sb.append(entity);
        sb.append("))");
        return sb.toString();
    }
}
