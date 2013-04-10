package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class EntityDeprecatedChangedEvent extends ProjectEvent<EntityDeprecatedChangedHandler> {

    public transient static final Type<EntityDeprecatedChangedHandler> TYPE = new Type<EntityDeprecatedChangedHandler>();

    private OWLEntity entity;

    private boolean deprecated;

    public EntityDeprecatedChangedEvent(ProjectId source, OWLEntity entity, boolean deprecated) {
        super(source);
        this.entity = entity;
        this.deprecated = deprecated;
    }

    /**
     * For serialization only
     */
    private EntityDeprecatedChangedEvent() {
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    @Override
    public Type<EntityDeprecatedChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EntityDeprecatedChangedHandler handler) {
        handler.handleEntityDeprecatedChangedEvent(this);
    }
}
