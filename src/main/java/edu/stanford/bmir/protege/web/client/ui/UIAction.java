package edu.stanford.bmir.protege.web.client.ui;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 13/06/2014
 */
public interface UIAction {

    public static interface LabelChangedHandler {
        void handleLabelChanged(UIAction action);
    }

    public static interface StateChangedHandler {
        void handleStateChanged(UIAction action);
    }
    void setLabelChangedHandler(LabelChangedHandler labelChangedHandler);

    void setStateChangedHandler(StateChangedHandler stateChangedHandler);


    String getLabel();

    boolean isEnabled();

    void execute();
}
