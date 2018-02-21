package edu.stanford.bmir.protege.web.client.about;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/08/2013
 */
public class AboutBox {



    public void show() {
        final PopupPanel popupPanel = new PopupPanel();
        popupPanel.setAutoHideEnabled(true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setVisible(false);
        popupPanel.setGlassStyleName("glass");
        popupPanel.setWidget(new AboutBoxContent());
        popupPanel.show();
        popupPanel.addStyleName("glass-popup-shadow");
        Scheduler.get().scheduleDeferred(() -> {
            int left = (Window.getClientWidth() - popupPanel.getOffsetWidth()) / 2;
            int top = (Window.getClientHeight() - popupPanel.getOffsetHeight()) / 2;
            popupPanel.setPopupPosition(left, top);
            popupPanel.setVisible(true);
        });
    }
}
