package edu.stanford.bmir.protege.web.client.ui.library.progress;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/07/2013
 */
public class ProgressMonitor {

    private static ProgressMonitor instance = new ProgressMonitor();

    private final PopupPanel popupPanel;

    private final ProgressBarViewImpl widget;

    private ProgressMonitor() {
        popupPanel = new PopupPanel(false, true);
        popupPanel.addStyleName("progress-popup");
        widget = new ProgressBarViewImpl();
        popupPanel.setWidget(widget);
        popupPanel.setGlassEnabled(true);
        popupPanel.setGlassStyleName("glass");
    }

    public static ProgressMonitor get() {
        return instance;
    }

    public void showProgressMonitor(String titleText, String messageText) {
        widget.setTitleText(titleText == null ? "" : titleText);
        widget.setMessageText(messageText == null ? "" : messageText);
        popupPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                int clientWidth = Window.getClientWidth();
                int clientHeight = Window.getClientHeight();
                int x = (clientWidth - offsetWidth) / 2;
                int y = (clientHeight - offsetHeight) / 2;
                popupPanel.setPopupPosition(x, y);
            }
        });
        scheduleCentering(popupPanel);
    }

    private static void scheduleCentering(final PopupPanel popupPanel) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                int left = (Window.getClientWidth() - popupPanel.getOffsetWidth()) / 2;
                int top = (Window.getClientHeight() - popupPanel.getOffsetHeight()) / 2;
                popupPanel.setPopupPosition(left, top);
                popupPanel.setVisible(true);
            }
        });
    }

    public void hideProgressMonitor() {
        popupPanel.hide();
    }
}
