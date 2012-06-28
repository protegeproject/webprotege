package edu.stanford.bmir.protege.web.client.ui.library.common;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 */
public class SimplePanelDisplay extends SimplePanel {

    public static final String STYLE_NAME = "web-protege-display";

    public SimplePanelDisplay() {
        addStyleName(STYLE_NAME);
    }

    /**
     * Wraps a widget in a {@link SimplePanel} (a {@link SimplePanel} get mapped to a block level element in CSS.
     * @param widget The widget to be wrapped.
     * @return The {@link SimplePanel} that wraps the widget.
     */
    public static SimplePanel wrapInSimplePanel(Widget widget) {
        SimplePanel wrappingPanel = new SimplePanel();
        wrappingPanel.add(widget);
        return wrappingPanel;
    }

}
