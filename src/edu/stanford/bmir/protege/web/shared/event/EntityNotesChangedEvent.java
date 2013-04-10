package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class EntityNotesChangedEvent extends ProjectEvent<EntityNotesChangedHandler> {

    public transient static final Type<EntityNotesChangedHandler> TYPE = new Type<EntityNotesChangedHandler>();

    private OWLEntity entity;

    private int totalNotesCount;

    public EntityNotesChangedEvent(ProjectId source, OWLEntity entity, int totalNotesCount) {
        super(source);
        this.entity = entity;
        this.totalNotesCount = totalNotesCount;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public int getTotalNotesCount() {
        return totalNotesCount;
    }

    /**
     * For serialization only
     */
    private EntityNotesChangedEvent() {
    }

    @Override
    public Type<EntityNotesChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EntityNotesChangedHandler handler) {
        handler.entityNotesChanged(this);
    }
}
