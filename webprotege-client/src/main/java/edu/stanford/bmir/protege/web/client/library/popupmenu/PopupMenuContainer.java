package edu.stanford.bmir.protege.web.client.library.popupmenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class PopupMenuContainer extends Composite {

    private static PopupMenuContainerUiBinder ourUiBinder = GWT.create(PopupMenuContainerUiBinder.class);

    public interface PopupMenuDismissHandler {
        void handleDismiss();
    }

    @UiField
    protected FocusPanel focusPanel;

    @UiField
    protected HTMLPanel holder;

    private PopupMenuDismissHandler dismissHandler = () -> {};

    private final List<MenuItem> menuItems = new ArrayList<>();

    private int selectedIndex = -1;


    public PopupMenuContainer() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("focusPanel")
    protected void handleMouseMove(MouseMoveEvent event) {
        selectItemUnderMouseCursor(event);
    }

    @UiHandler("focusPanel")
    protected void handleMouseUp(MouseUpEvent event) {
        executeSelectedMenuItem();
    }

    @UiHandler("focusPanel")
    protected void handleBlue(BlurEvent event) {
        dismissHandler.handleDismiss();
    }

    public void setDismissHandler(@Nonnull PopupMenuDismissHandler dismissHandler) {
        this.dismissHandler = checkNotNull(dismissHandler);
    }

    public boolean isEmpty() {
        return holder.getWidgetCount() == 0;
    }

    /**
     * Set the selected index.  Styles will be updated to reflect the selected index.
     * @param selectedIndex The selected index.
     */
    private void setSelectedIndex(final int selectedIndex) {
        if(selectedIndex < 0 || selectedIndex >= menuItems.size()) {
            this.selectedIndex = -1;
        }
        else {
            this.selectedIndex = selectedIndex;
        }
        for(int i = 0; i < menuItems.size(); i++) {
            MenuItem menuItem = menuItems.get(i);
            applySelectionStyle(menuItem, i == this.selectedIndex);
        }
    }

    private void selectItemUnderMouseCursor(MouseMoveEvent event) {
        int index = 0;
        for(MenuItem menuItem : menuItems) {
            if (menuItem.isEnabled()) {
                Widget widget = menuItem.asWidget();
                int topY = widget.getAbsoluteTop();
                int botY = topY + widget.getOffsetHeight();
                int clientY = event.getClientY();
                if (topY < clientY && clientY <= botY) {
                    setSelectedIndex(index);
                    return;
                }
            }
            index++;
        }
        setSelectedIndex(-1);
    }

    @UiHandler("focusPanel")
    protected void handleMouseOut(MouseOutEvent event) {
        clearSelection();
    }

    @UiHandler("focusPanel")
    protected void handleKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            moveSelectionDown();
        }
        else if (event.getNativeKeyCode() == KeyCodes.KEY_UP) {
            moveSelectionUp();
        }
        else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            executeSelectedMenuItem();
        }
        else if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            executeSelectedMenuItem();
        }
        else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
            dismissMenu();
        }
    }

    private void dismissMenu() {
        dismissHandler.handleDismiss();
    }

    private void executeSelectedMenuItem() {
        getSelectedMenuItem().ifPresent(MenuItem::execute);
        dismissMenu();
    }

    public void clearSelection() {
        setSelectedIndex(-1);
    }

    public void selectFirstEnabledMenuItem() {
        setSelectedIndex(-1);
        moveSelectionDown();
    }

    private Optional<MenuItem> getSelectedMenuItem() {
        if(selectedIndex == -1) {
            return Optional.empty();
        }
        else {
            return Optional.of(menuItems.get(selectedIndex));
        }
    }

    private void moveSelectionDown() {
        for(int i = selectedIndex + 1; i < menuItems.size(); i++) {
            MenuItem menuItem = menuItems.get(i);
            if(menuItem.isEnabled()) {
                setSelectedIndex(i);
                return;
            }
        }
    }

    private void moveSelectionUp() {
        for(int i = selectedIndex - 1; i >= 0; i--) {
            MenuItem menuItem = menuItems.get(i);
            if(menuItem.isEnabled()) {
                setSelectedIndex(i);
                return;
            }
        }
    }

    private void applySelectionStyle(MenuItem menuItem, boolean selected) {
        if (selected) {
            menuItem.asWidget().addStyleName(BUNDLE.menu().popupMenuItemSelected());
        }
        else {
            menuItem.asWidget().removeStyleName(BUNDLE.menu().popupMenuItemSelected());
        }
    }

    public void addMenuItem(@Nonnull MenuItem menuItem) {
        holder.add(menuItem);
        menuItems.add(menuItem);
    }

    public void addSeparator() {
        SimplePanel sep = new SimplePanel();
        sep.addStyleName(BUNDLE.menu().separator());
        holder.add(sep);
    }

    public void focus() {
        focusPanel.setFocus(true);
    }

    interface PopupMenuContainerUiBinder extends UiBinder<HTMLPanel, PopupMenuContainer> {
    }
}
