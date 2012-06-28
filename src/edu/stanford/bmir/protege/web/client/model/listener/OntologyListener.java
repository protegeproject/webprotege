package edu.stanford.bmir.protege.web.client.model.listener;

import edu.stanford.bmir.protege.web.client.model.event.*;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

public interface OntologyListener {

    void entityCreated(EntityCreateEvent createEvent);

    void entityDeleted(EntityDeleteEvent deleteEvent);

    void entityRenamed(EntityRenameEvent renameEvent);

    void propertyValueAdded(PropertyValueEvent propertyValueEvent);

    void propertyValueRemoved(PropertyValueEvent propertyValueEvent);

    void propertyValueChanged(PropertyValueEvent propertyValueEvent);

    void individualAddedRemoved(OntologyEvent ontologyEvent);

    void entityBrowserTextChanged(EntityBrowserTextChangedEvent event);

}
