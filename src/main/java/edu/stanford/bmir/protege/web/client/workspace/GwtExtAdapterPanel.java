package edu.stanford.bmir.protege.web.client.workspace;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.TabPanelListenerAdapter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.ui.ProjectDisplayContainerPanel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class GwtExtAdapterPanel extends FlowPanel {

    public GwtExtAdapterPanel(EventBus eventBus, DispatchServiceManager dispatchServiceManager, ProjectManager projectManager) {

        final ProjectDisplayContainerPanel projectDisplayContainerPanel = new ProjectDisplayContainerPanel(eventBus, dispatchServiceManager, projectManager);
        add(projectDisplayContainerPanel);

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
