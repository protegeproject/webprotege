package edu.stanford.bmir.protege.web.client.ui.library.text;

import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.anchor.AnchorClickedHandler;
import edu.stanford.bmir.protege.web.client.ui.anchor.HasAnchor;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasTextRendering;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/12/2012
 * <p>
 *     A TextBox that can resize itself based on its content.  The {@code ExpandingTextBox} also supports auto-completion
 *     using a GWT {@link SuggestBox}.
 * </p>
 */
public class ExpandingTextBox extends SimplePanel implements Focusable, HasText, HasEnabled, HasValue<String>, HasTextRendering, HasPlaceholder, HasSelectionHandlers<SuggestOracle.Suggestion>, HasKeyUpHandlers, HasFocusHandlers, HasAnchor {

    private final ExpandingTextBoxImpl impl;

    public ExpandingTextBox() {
        impl = new ExpandingTextBoxImpl();
        setWidget(impl);
    }

    public ExpandingTextBoxMode getMode() {
        return impl.getMode();
    }

    public void setAnchorVisible(boolean visible) {
        impl.setAnchorVisible(visible);
    }

    @Override
    public HandlerRegistration addAnchorClickedHandler(AnchorClickedHandler handler) {
        return impl.addAnchorClickedHandler(handler);
    }

    public void setAnchorTitle(String title) {
        impl.setAnchorTitle(title);
    }


    public void setMode(ExpandingTextBoxMode mode) {
        impl.setMode(mode);
    }

    public void setMultiline(boolean b) {
        if(b) {
            setMode(ExpandingTextBoxMode.MULTI_LINE);
        }
        else {
            setMode(ExpandingTextBoxMode.SINGLE_LINE);
        }
    }

    @Override
    public String getPlaceholder() {
        return impl.getPlaceholder();
    }

    @Override
    public void setPlaceholder(String placeholder) {
        impl.setPlaceholder(placeholder);
    }



    /**
     * Returns true if the widget is enabled, false if not.
     */
    @Override
    public boolean isEnabled() {
        return impl.isEnabled();
    }

    /**
     * Sets whether this widget is enabled.
     * @param enabled <code>true</code> to enable the widget, <code>false</code>
     * to disable it
     */
    @Override
    public void setEnabled(boolean enabled) {
        impl.setEnabled(enabled);
    }

    /**
     * Gets this object's text.
     * @return the object's text
     */
    @Override
    public String getText() {
        return impl.getText();
    }

    /**
     * Sets this object's text.
     * @param text the object's new text
     */
    @Override
    public void setText(String text) {
        impl.setText(text);
    }

    /**
     * Gets this object's value.
     * @return the object's value
     */
    @Override
    public String getValue() {
        return getText();
    }

    /**
     * Sets this object's value without firing any events. This should be
     * identical to calling setValue(value, false).
     * <p>
     * It is acceptable to fail assertions or throw (documented) unchecked
     * exceptions in response to bad values.
     * <p>
     * Widgets must accept null as a valid value. By convention, setting a widget to
     * null clears value, calling getValue() on a cleared widget returns null. Widgets
     * that can not be cleared (e.g. {@link com.google.gwt.user.client.ui.CheckBox}) must find another valid meaning
     * for null input.
     * @param value the object's new value
     */
    @Override
    public void setValue(String value) {
        setText(value);
    }

    /**
     * Sets this object's value. Fires
     * {@link com.google.gwt.event.logical.shared.ValueChangeEvent} when
     * fireEvents is true and the new value does not equal the existing value.
     * <p>
     * It is acceptable to fail assertions or throw (documented) unchecked
     * exceptions in response to bad values.
     * @param value the object's new value
     * @param fireEvents fire events if true and value is new
     */
    @Override
    public void setValue(String value, boolean fireEvents) {
        setText(value);
    }

    /**
     * Adds a {@link com.google.gwt.event.logical.shared.ValueChangeEvent} handler.
     * @param handler the handler
     * @return the registration for the event
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return impl.addValueChangeHandler(handler);
    }

    /**
     * Adds a {@link com.google.gwt.event.logical.shared.SelectionEvent} handler.
     * @param handler the handler
     * @return the registration for the event
     */
    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<SuggestOracle.Suggestion> handler) {
        return impl.addSelectionHandler(handler);
    }

    /**
     * Adds a {@link com.google.gwt.event.dom.client.KeyUpEvent} handler.
     * @param handler the key up handler
     * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove this handler
     */
    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return impl.addKeyUpHandler(handler);
    }

    /**
     * Adds a {@link com.google.gwt.event.dom.client.FocusEvent} handler.
     * @param handler the focus handler
     * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove this handler
     */
    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return impl.addFocusHandler(handler);
    }

    public void setOracle(SuggestOracle suggestOracle) {
        impl.setSuggestOracle(suggestOracle);
    }

    public void setAutoSelectSuggestions(boolean autoSelectSuggestions) {
        impl.setAutoSelectSuggestions(autoSelectSuggestions);
    }

    public SuggestBox getSuggestBox() {
        return impl.getSuggestBox();
    }

    /**
     * Gets the widget's position in the tab index.
     * @return the widget's tab index
     */
    @Override
    public int getTabIndex() {
        return impl.getTabIndex();
    }

    /**
     * Sets the widget's 'access key'. This key is used (in conjunction with a
     * browser-specific modifier key) to automatically focus the widget.
     * @param key the widget's access key
     */
    @Override
    public void setAccessKey(char key) {
        impl.setAccessKey(key);
    }

    /**
     * Explicitly focus/unfocus this widget. Only one widget can have focus at a
     * time, and the widget that does will receive all keyboard events.
     * @param focused whether this widget should take focus or release it
     */
    @Override
    public void setFocus(boolean focused) {
        impl.setFocus(focused);
    }

    /**
     * Sets the widget's position in the tab index. If more than one widget has
     * the same tab index, each such widget will receive focus in an arbitrary
     * order. Setting the tab index to <code>-1</code> will cause this widget to
     * be removed from the tab order.
     * @param index the widget's tab index
     */
    @Override
    public void setTabIndex(int index) {
        impl.setTabIndex(index);
    }
}
