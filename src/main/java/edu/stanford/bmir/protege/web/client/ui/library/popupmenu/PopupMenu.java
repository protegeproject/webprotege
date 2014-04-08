package edu.stanford.bmir.protege.web.client.ui.library.popupmenu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/08/2013
 */
public class PopupMenu {

    public static final String POPUP_MENU_STYLE_NAME = "web-protege-popup-menu";

    public static final String POPUP_MENU_INNER_STYLE_NAME = "web-protege-popup-menu-inner";

    public static final String ITEM_STYLE_NAME = "web-protege-popup-menu-item";

    private final PopupPanel popupPanel;

    private final FlowPanel holder;

    public PopupMenu() {
        popupPanel = new PopupPanel();
        popupPanel.setAutoHideEnabled(true);
        popupPanel.addStyleName(POPUP_MENU_STYLE_NAME);
        holder = new FlowPanel();
        holder.addStyleName(POPUP_MENU_INNER_STYLE_NAME);
        popupPanel.setWidget(holder);
    }

    public void addItem(final String text, final ClickHandler clickHandler) {
        addItem(new SafeHtmlBuilder().appendEscaped(text).toSafeHtml(), clickHandler);
    }

    public void addItem(final SafeHtml safeHtml, final ClickHandler clickHandler) {
        PopupMenuItem popupMenuItem = new PopupMenuItem(safeHtml) {
            @Override
            public void handleClicked(ClickEvent clickEvent) {
                clickHandler.onClick(clickEvent);
            }
        };
        addItem(popupMenuItem);
    }

    public void addItem(final PopupMenuItem popupMenuItem) {
        HTML panel = new HTML(popupMenuItem.getHtml());
        panel.addStyleName(ITEM_STYLE_NAME);
        holder.add(panel);
        panel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
                popupMenuItem.handleClicked(event);
            }
        });
    }

    public void show() {
        popupPanel.show();
    }

    public void hide() {
        popupPanel.hide();
    }

    public void showRelativeTo(UIObject target) {
        popupPanel.showRelativeTo(target);
    }
}
