package edu.stanford.bmir.protege.web.client.ui.library.common;

import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.Size;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/01/2012
 */
public class FitLayoutPanelAdapter extends Panel implements ProvidesResize, RequiresResize {

    private Widget widget;

    /**
     * Create a new Panel.
     */
    public FitLayoutPanelAdapter() {
        setLayout(new FitLayout());
        addListener(new PanelListenerAdapter() {
            @Override
            public void onBodyResize(Panel panel, String width, String height) {
                FitLayoutPanelAdapter.this.onResize();
            }
        });
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    /**
     * This method must be called whenever the implementor's size has been
     * modified.
     */
    public void onResize() {
        Size size = getSize();

//        widget.setSize(size.getWidth() + "px", size.getHeight() + "px");
//        RequiresResize requiresResize = (RequiresResize) widget;
//        requiresResize.onResize();
    }
}
