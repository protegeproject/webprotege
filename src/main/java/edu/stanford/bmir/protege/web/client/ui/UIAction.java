package edu.stanford.bmir.protege.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 13/06/2014
 */
public interface UIAction {


    interface LabelChangedHandler {
        void handleLabelChanged(UIAction action);
    }

    interface StateChangedHandler {
        void handleStateChanged(UIAction action);
    }
    void setLabelChangedHandler(LabelChangedHandler labelChangedHandler);

    void setStateChangedHandler(StateChangedHandler stateChangedHandler);


    String getLabel();

    boolean isEnabled();

    boolean isVisible();

    void execute(ClickEvent clickEvent);
}
