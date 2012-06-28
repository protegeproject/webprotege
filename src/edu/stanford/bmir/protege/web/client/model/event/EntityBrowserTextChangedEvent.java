package edu.stanford.bmir.protege.web.client.model.event;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/06/2012
 * <p>
 *     An event that is fired when the browser text of an entity changes.
 * </p>
 */
public class EntityBrowserTextChangedEvent extends AbstractOntologyEvent implements Serializable {

    /**
     * Private empty constructor for serialization purposes.
     */
    private EntityBrowserTextChangedEvent() {
        super();
    }

    /**
     * Constructs an EntityBrowserTextChangedEvent which is fired when the browser text of an entity has changed.
     * @param source The entity whose browser text has changed.  The browser text returned by
     * {@link edu.stanford.bmir.protege.web.client.rpc.data.EntityData#getBrowserText()} will be the new browser text.
     * @param user The user that initiated the changes that caused the entity browser text to change.
     * @param revision The revision of the ontology after the change has been made.
     */
    public EntityBrowserTextChangedEvent(EntityData source, String user, int revision) {
        super(source, EventType.ENTITY_BROWSER_TEXT_CHANGED, user, revision);
    }
}
