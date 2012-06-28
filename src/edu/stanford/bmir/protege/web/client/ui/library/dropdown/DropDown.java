package edu.stanford.bmir.protege.web.client.ui.library.dropdown;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/02/2012
 */
public class DropDown<T> extends FocusPanel implements HasValueChangeHandlers<T> {
    
    public static final String STYLE_NAME = "web-protege-drop-down";
    
    public static final String ITEM_STYLE_NAME = "web-protege-drop-down-item";

    public static final String ITEM_SELECTED_STYLE_NAME = "web-protege-drop-down-item-selected";

    public static final String ITEM_DESELECTED_STYLE_NAME = "web-protege-drop-down-item-deselected";
    
    public static final String POPUP_STYLE_NAME = "web-protege-drop-down-popup";

    public static final String BUTTON_TEXT = "\u25BC";

    private InlineLabel selectedItemDisplayLabel = new InlineLabel();
    
    private DropDownModel<T> model;
    
    private int selIndex = -1;

    private final InlineLabel buttonLabel;

    public DropDown(DropDownModel<T> model) {
        this.model = model;
        addStyleName(STYLE_NAME);
        FlowPanel contentHolder = new FlowPanel();
        contentHolder.addStyleName("web-protege-drop-down-content-holder");
        contentHolder.add(selectedItemDisplayLabel);
        buttonLabel = new InlineLabel(BUTTON_TEXT);
        buttonLabel.addStyleName("web-protege-drop-down-button-holder");
        contentHolder.add(buttonLabel);
        int maxLength = 0;
        for(int i = 0; i < model.getSize(); i++) {
            String rendering = model.getRendering(i);
            if(rendering.length() > maxLength) {
                maxLength = rendering.length();
            }
        }
        add(contentHolder);
        contentHolder.setWidth((maxLength * 10) + "px");
        

        
        if(model.getSize() > 0) {
            setSelectedItem(model.getItemAt(0));
        }
        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                handleClick();
            }
        });
        addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                handleKeyDown(event);
            }
        });
    }
    
    public void setSelectedItem(T item) {
        selIndex = -1;
        for(int i = 0; i < model.getSize(); i++) {
            if(model.getItemAt(i).equals(item)) {
                setSelectedIndex(i);
                break;
            }
        }
    }

    public void setSelectedIndex(int index) {
        if(index < 0 || index > model.getSize() - 1) {
            return;
        }
        selectedItemDisplayLabel.setText(model.getRendering(index));
        selIndex = index;
        ValueChangeEvent.fire(this, getSelectedItem());
    }

    public T getSelectedItem() {
        if(selIndex < 0 && selIndex >= model.getSize()) {
            return null;
        }
        return model.getItemAt(selIndex);
    }
    
    public int getSelIndex() {
        return selIndex;
    }



    private void handleClick() {
        showPopup();
    }

    private void handleKeyDown(KeyDownEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            handleDown();
        }
        else if(event.getNativeKeyCode() == KeyCodes.KEY_UP) {
            handleUp();
        }
    }

    private void handleDown() {
        int selIndex = getSelIndex();
        selIndex++;
        if(selIndex == model.getSize()) {
            selIndex = 0;
        }
        setSelectedIndex(selIndex);
    }

    private void handleUp() {
        int selIndex = getSelIndex();
        selIndex--;
        if(selIndex < 0) {
            selIndex = model.getSize() - 1;
        }
        setSelectedIndex(selIndex);
    }

    private void showPopup() {
        final PopupPanel popupPanel = new PopupPanel(true);
        popupPanel.addStyleName(POPUP_STYLE_NAME);
        FlowPanel itemPanel = new FlowPanel();
        popupPanel.setWidget(itemPanel);

        popupPanel.addCloseHandler(new CloseHandler<PopupPanel>() {
            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                DropDown.this.setFocus(true);
            }
        });

        for(int i = 0; i < model.getSize(); i++) {
            final T item = model.getItemAt(i);
            Label itemLabel = new Label(model.getRendering(i));
            itemLabel.addStyleName(ITEM_STYLE_NAME);
            if(selIndex == i) {
                itemLabel.addStyleName(ITEM_SELECTED_STYLE_NAME);
            }
            else {
                itemLabel.addStyleName(ITEM_DESELECTED_STYLE_NAME);
            }
            itemPanel.add(itemLabel);
            itemLabel.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    setSelectedItem(item);
                    popupPanel.hide();
                }
            });
        }
        popupPanel.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop() + getOffsetHeight());
        popupPanel.show();

    }


    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
