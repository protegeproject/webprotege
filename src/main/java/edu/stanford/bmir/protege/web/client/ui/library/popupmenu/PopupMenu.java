package edu.stanford.bmir.protege.web.client.ui.library.popupmenu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;

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
        panel.addStyleName(BUNDLE.menu().popupMenuItem());
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
