package edu.stanford.bmir.protege.web.client.portlet;

import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/02/16
 */
public class PortletAction extends AbstractUiAction {

    private final Runnable handler;

    private final String style;

    public PortletAction(String label, Runnable handler) {
        super(label);
        this.handler = checkNotNull(handler);
        this.style = "";
    }

    public PortletAction(String label, String style, Runnable handler) {
        super(label);
        this.handler = checkNotNull(handler);
        this.style = checkNotNull(style);
    }

    @Override
    public final void execute() {
        handler.run();
    }

    @Override
    public boolean hasIcon() {
        return !style.isEmpty();
    }

    @Nonnull
    @Override
    public String getStyle() {
        return style;
    }
}
