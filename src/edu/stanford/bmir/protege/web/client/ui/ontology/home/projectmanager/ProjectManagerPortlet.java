package edu.stanford.bmir.protege.web.client.ui.ontology.home.projectmanager;

import com.gwtext.client.widgets.Resizable;
import com.gwtext.client.widgets.ResizableConfig;
import com.gwtext.client.widgets.event.ResizableListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.portal.Portlet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 */
public class ProjectManagerPortlet extends Portlet {

    public static final String TITLE = "Projects";

    public ProjectManagerPortlet() {

        setTitle(TITLE);
        setLayout(new FitLayout());
        ProjectManagerDisplay display = new ProjectManagerDisplayImpl();
        add(display.getDisplayWidget());

        ResizableConfig config1 = new ResizableConfig();
        final Resizable resizable = new Resizable(this, config1);
        resizable.addListener(new ResizableListenerAdapter() {
            @Override
            public void onResize(Resizable self, int width, int height) {
                doOnResize(width, height);
            }
        });
    }

    public void doOnResize(int width, int height) {
        setWidth(width);
        setHeight(height);
        doLayout();
    }


}
