package edu.stanford.bmir.protege.web.client.portlet;

import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.shared.HasStateChangedHandler;
import edu.stanford.bmir.protege.web.shared.StateChangedHandler;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/02/16
 */
public class PortletAction implements HasEnabled, HasStateChangedHandler<PortletAction> {

    private String name;

    private PortletActionHandler actionHandler;

    private boolean enabled = true;

    private StateChangedHandler<PortletAction> stateChangedHandler = new StateChangedHandler<PortletAction>() {
        @Override
        public void handleStateChanged(PortletAction value) {

        }
    };

    public PortletAction(String name, PortletActionHandler actionHandler) {
        this.name = name;
        this.actionHandler = actionHandler;
    }

    public String getName() {
        return name;
    }

    public PortletActionHandler getActionHandler() {
        return actionHandler;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if(this.enabled != enabled) {
            this.enabled = enabled;
            stateChangedHandler.handleStateChanged(this);
        }

    }

    @Override
    public void setStateChangedHandler(StateChangedHandler<PortletAction> stateChangedHandler) {
        this.stateChangedHandler = checkNotNull(stateChangedHandler);
    }
}
