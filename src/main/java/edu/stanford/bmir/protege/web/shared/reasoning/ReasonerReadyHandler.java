package edu.stanford.bmir.protege.web.shared.reasoning;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/09/2014
 */
public interface ReasonerReadyHandler extends EventHandler {

    void handleReasonerReady(ReasonerReadyEvent event);
}
