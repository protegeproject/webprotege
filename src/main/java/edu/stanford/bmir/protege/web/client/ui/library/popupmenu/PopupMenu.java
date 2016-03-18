package edu.stanford.bmir.protege.web.client.ui.library.popupmenu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.ui.UIAction;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/08/2013
 */
public class PopupMenu {

    private final PopupPanel popupPanel;

    private final FlowPanel holder;

    public PopupMenu() {
        popupPanel = new PopupPanel();
        popupPanel.setAutoHideEnabled(true);
        popupPanel.addStyleName(BUNDLE.menu().popupMenu());
        holder = new FlowPanel();
        holder.addStyleName(BUNDLE.menu().popupMenuInner());
        popupPanel.setWidget(holder);
    }

    public UIAction addItem(final String label, final ClickHandler clickHandler) {
        AbstractUiAction action = new AbstractUiAction(label) {
            @Override
            public void execute(ClickEvent clickEvent) {
                clickHandler.onClick(clickEvent);
            }
        };
        addItem(action);
        return action;
    }

    public void addItem(final UIAction action) {
        holder.add(new MenuItem(action, this));
    }

    public void addSeparator() {
        SimplePanel sep = new SimplePanel();
        sep.addStyleName(WebProtegeClientBundle.BUNDLE.menu().separator());
        holder.add(sep);
    }

    public void show() {
        popupPanel.show();
    }

    public void show(int x, int y) {
        popupPanel.setPopupPosition(x, y);
        popupPanel.show();
    }

    public void hide() {
        popupPanel.hide();
    }

    public void showRelativeTo(UIObject target) {
        popupPanel.showRelativeTo(target);
    }
}
