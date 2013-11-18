package edu.stanford.bmir.protege.web.client.workspace;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.TabPanelListenerAdapter;
import edu.stanford.bmir.protege.web.client.ui.ProjectDisplayContainerPanel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class GwtExtAdapterPanel extends FlowPanel {

    public GwtExtAdapterPanel() {

        final ProjectDisplayContainerPanel projectDisplayContainerPanel = new ProjectDisplayContainerPanel();
        add(projectDisplayContainerPanel);

        projectDisplayContainerPanel.addListener(new TabPanelListenerAdapter() {
            @Override
            public boolean doBeforeTabChange(TabPanel source, Panel newPanel, Panel oldPanel) {
                GWT.log("Project tab changed");
                return true;
            }

            @Override
            public boolean doBeforeClose(Panel panel) {
                GWT.log("Project tab close");
                return true;
            }
        });

        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                layoutGwtExtPanel(projectDisplayContainerPanel);
            }
        });
        layoutGwtExtPanel(projectDisplayContainerPanel);
    }

    private void layoutGwtExtPanel(final Panel rootPanel) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                Element element = getElement();
                int clientWidth = element.getClientWidth();
                int clientHeight = element.getClientHeight();
                rootPanel.setSize(clientWidth, clientHeight);
                rootPanel.doLayout();
            }
        });
    }
}
