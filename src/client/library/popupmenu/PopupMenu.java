package edu.stanford.bmir.protege.web.client.library.popupmenu;

import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.action.UIAction;

import java.util.Optional;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/08/2013
 */
public class PopupMenu {

    private final PopupPanel popupPanel;

    private final PopupMenuContainer holder;

    private Optional<UIObject> lastInvoker = Optional.empty();

    public PopupMenu() {
        popupPanel = new PopupPanel();
        popupPanel.setAutoHideEnabled(true);
        popupPanel.addStyleName(BUNDLE.menu().popupMenu());
        holder = new PopupMenuContainer();
        popupPanel.setWidget(holder);
        holder.setDismissHandler(this::hide);
    }



    public UIAction addItem(final String label, final Runnable handler) {
        AbstractUiAction action = new AbstractUiAction(label) {
            @Override
            public void execute() {
                handler.run();
            }
        };
        addItem(action);
        return action;
    }

    public void addItem(final UIAction action) {
        holder.addMenuItem(new MenuItem(action));
    }

    public void addSeparator() {
        holder.addSeparator();
    }

    public void show() {
        popupPanel.show();
        resetSelectionAndFocusMenu();
        lastInvoker = Optional.empty();
    }

    public void show(int x, int y) {
        popupPanel.setPopupPosition(x, y);
        popupPanel.show();
        lastInvoker = Optional.empty();
        resetSelectionAndFocusMenu();

    }

    public void showRelativeTo(UIObject target) {
        popupPanel.showRelativeTo(target);
        lastInvoker = Optional.of(target);
        resetSelectionAndFocusMenu();
    }

    void resetSelectionAndFocusMenu() {
        holder.selectFirstEnabledMenuItem();
        holder.focus();
    }

    public void hide() {
        popupPanel.hide();
        holder.clearSelection();
        lastInvoker.ifPresent(invoker -> invoker.getElement().focus());
    }
}
