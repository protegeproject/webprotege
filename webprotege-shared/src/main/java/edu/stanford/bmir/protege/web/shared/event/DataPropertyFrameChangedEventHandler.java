package edu.stanford.bmir.protege.web.shared.event;

import com.google.gwt.event.shared.EventHandler;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public interface DataPropertyFrameChangedEventHandler extends EventHandler, Serializable {

    void dataPropertyFrameChanged(DataPropertyFrameChangedEvent event);
}
