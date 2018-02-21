package edu.stanford.bmir.protege.web.client.portlet;

import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/02/16
 */
public class PortletAction extends AbstractUiAction {

    private final Runnable handler;

    public PortletAction(String label, Runnable handler) {
        super(label);
        this.handler = checkNotNull(handler);
    }

    @Override
    public final void execute() {
        handler.run();
    }
}
