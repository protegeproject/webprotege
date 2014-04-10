package edu.stanford.bmir.protege.web.client.ui.library.sidebar;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/01/2012
 * <p>
 *     A bar of clickable items.  Each item has a label.
 * </p>
 */
public class SideBar<I extends SideBarItem> extends Composite implements HasSelectionHandlers<I>, IsWidget {

    public static final String SIDEBAR_STYLE_NAME = "web-protege-sidebar";

    public static final String ITEM_BASE_STYLE = "web-protege-sidebar-item-base";

    public static final String ITEM_LABEL_STYLE = "web-protege-sidebar-item-label";

    public static final String ITEM_SELECTED_STYLE = "web-protege-sidebar-item-selected";

    public static final String ITEM_DESELECTED_STYLE = "web-protege-sidebar-item-deselected";



    private List<ItemWidget> items = new ArrayList<ItemWidget>();

    private ItemWidget currentSelection = null;
    
    private FlowPanel itemListHolder = new FlowPanel();
    
    public SideBar() {
        itemListHolder.addStyleName(SIDEBAR_STYLE_NAME);
        initWidget(itemListHolder);

    }

    public void setSelectedItemByLabel(String label) {
        checkNotNull(label);
        if(getSelectedItem().getLabel().equals(label)) {
            return;
        }
        for(ItemWidget i : items) {
            boolean iSelected = i.getItem().getLabel().equals(label);
            i.setSelected(iSelected);
            if(iSelected) {
                currentSelection = i;
            }
        }
        fireSelectionChanged();
    }

    public void addItem(final I item) {
        
        FocusPanel itemHolder = createItemWidget(item);
        itemListHolder.add(itemHolder);
        items.add(new ItemWidget(item, itemHolder));
        if(items.size() == 1) {
            setSelectedItem(item);
        }
    }

    public void clearSelection() {
        currentSelection = null;
    }
    
    public void clearItems() {
        itemListHolder.clear();
        items.clear();
        clearSelection();
    }
    
    public int indexOf(I item) {
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    private FocusPanel createItemWidget(final I item) {
        FocusPanel itemHolder = new FocusPanel();
        final Label itemLabel = new Label(item.getLabel());
        itemLabel.setWidth("90%");
        itemHolder.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(MouseUpEvent event) {
                setSelectedItem(item);
            }
        });
        itemHolder.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if(event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                    moveSelectionUp();
                }
                else if(event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                    moveSelectionDown();
                }
            }
        });
        itemHolder.addStyleName(ITEM_BASE_STYLE);
        itemLabel.addStyleName(ITEM_LABEL_STYLE);
        itemHolder.addStyleName(ITEM_DESELECTED_STYLE);

        itemHolder.add(itemLabel);
        return itemHolder;
    }
    
    public void moveSelectionUp() {
        int currentIndex = items.indexOf(currentSelection);
        if(currentIndex == -1) {
            return;
        }
        currentIndex--;
        if(currentIndex < 0) {
            currentIndex = items.size() - 1;
        }
        setSelectedItem(items.get(currentIndex).getItem());
    }
    
    public void moveSelectionDown() {
        int currentIndex = items.indexOf(currentSelection);
        if(currentIndex == -1) {
            return;
        }
        currentIndex++;
        if(currentIndex > items.size() - 1) {
            currentIndex = 0;
        }
        setSelectedItem(items.get(currentIndex).getItem());
    }

    public void setSelectedItem(I item) {
        checkNotNull(item, "item must not be null");
        if (!item.equals(currentSelection)) {
            for(ItemWidget i : items) {
                boolean iSelected = i.getItem() == item;
                i.setSelected(iSelected);
                if(iSelected) {
                    currentSelection = i;
                }
            }
            fireSelectionChanged();
        }
    }

    public void setSelectedItem(int index) {
        setSelectedItem(items.get(index).getItem());
    }
    
    /**
     * Adds a {@link com.google.gwt.event.logical.shared.SelectionEvent} handler.
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addSelectionHandler(final SelectionHandler<I> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    private void fireSelectionChanged() {
        SelectionEvent.fire(this, currentSelection.getItem());
    }

    
    
    public I getSelectedItem() {
        return currentSelection.getItem();
    }


    public class ItemWidget {

        private I item;

        private FocusPanel widget;

        public ItemWidget(I item, FocusPanel widget) {
            this.item = item;
            this.widget = widget;
        }

        public I getItem() {
            return item;
        }

        public Widget getWidget() {
            return widget;
        }

        /**
         * Sets the style of the widget to selected or deselected.
         * @param b If <code>true</code>, sets the style to the selected style
         * (given by {@link SideBar#ITEM_SELECTED_STYLE}). If <code>false</code> sets the style to the deselected
         * style (given by {@link SideBar#ITEM_DESELECTED_STYLE}.
         */
        public void setSelected(boolean b) {
            if(b) {
                widget.removeStyleName(ITEM_DESELECTED_STYLE);
                widget.addStyleName(ITEM_SELECTED_STYLE);
                widget.setFocus(true);
            }
            else {
                widget.removeStyleName(ITEM_SELECTED_STYLE);
                widget.addStyleName(ITEM_DESELECTED_STYLE);
            }
        }
    }
}
