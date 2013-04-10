package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public interface HasEventList<T extends SerializableEvent<?>> {

    EventList<T> getEventList();

}
