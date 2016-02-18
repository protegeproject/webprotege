package edu.stanford.bmir.protege.web.client.portlet;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/02/16
 */
public class PortletContentHolder extends SimplePanel {

    @Override
    public void setWidget(IsWidget w) {
        w.asWidget().setSize("100%", "100%");
        super.setWidget(w);
    }

    @Override
    public void setWidget(Widget w) {
        w.setSize("100%", "100%");
        super.setWidget(w);
    }
}
