package edu.stanford.bmir.protege.web.client.ui;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractUiAction implements UIAction {

    private static final StateChangedHandler EMPTY_STATE_CHANGED_HANDLER = new StateChangedHandler() {
        public void handleStateChanged(UIAction action) {

        }
    };

    private static final LabelChangedHandler EMPTY_LABEL_CHANGED_HANDLER = new LabelChangedHandler() {
        public void handleLabelChanged(UIAction action) {

        }
    };
    public static final boolean ENABLED_BY_DEFAULT = true;

    private String label;

    private boolean enabled = ENABLED_BY_DEFAULT;

    private LabelChangedHandler labelChangedHandler = EMPTY_LABEL_CHANGED_HANDLER;

    private StateChangedHandler stateChangedHandler = EMPTY_STATE_CHANGED_HANDLER;

    protected AbstractUiAction(String label) {
        this.label = label;
    }

    public final String getLabel() {
        return label;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public void setLabel(String label) {
        checkNotNull(label);
        if(this.label.equals(label)) {
            return;
        }
        this.label = label;
        labelChangedHandler.handleLabelChanged(this);
    }

    public void setEnabled(boolean enabled) {
        if(this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        stateChangedHandler.handleStateChanged(this);
    }

    public final void setLabelChangedHandler(LabelChangedHandler labelChangedHandler) {
        this.labelChangedHandler = checkNotNull(labelChangedHandler);
    }

    public final void setStateChangedHandler(StateChangedHandler stateChangedHandler) {
        this.stateChangedHandler = stateChangedHandler;
    }
}
