package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.lang.LangSuggestOracle;
import edu.stanford.bmir.protege.web.client.library.common.EventStrategy;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.lang.LanguageTagFormatter;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/12/2012
 */
public class DefaultLanguageEditor extends Composite implements LanguageEditor, HasPlaceholder {

    private final SuggestBox suggestBox;

    private boolean dirty = false;

    private String placeholder = "";

    @Inject
    public DefaultLanguageEditor(LangSuggestOracle oracle) {
        suggestBox = new SuggestBox(oracle);
        suggestBox.addSelectionHandler(event -> {
            setDirty(true, EventStrategy.FIRE_EVENTS);
        });
        suggestBox.getValueBox().addValueChangeHandler(event -> {
            setDirty(true, EventStrategy.FIRE_EVENTS);
        });
        initWidget(suggestBox);
    }


    @Override
    public Widget getWidget() {
        return this;
    }

    /**
     * Returns true if the widget is enabled, false if not.
     */
    @Override
    public boolean isEnabled() {
        return suggestBox.getValueBox().isEnabled();
    }

    /**
     * Sets whether this widget is enabled.
     * @param enabled <code>true</code> to enable the widget, <code>false</code>
     * to disable it
     */
    @Override
    public void setEnabled(boolean enabled) {
        suggestBox.getValueBox().setEnabled(enabled);
        if(enabled) {
            setPlaceholderInternal(placeholder);
        }
        else {
            setPlaceholderInternal("");
        }
    }

    /**
     * Adds a {@link com.google.gwt.event.logical.shared.ValueChangeEvent} handler.
     * @param handler the handler
     * @return the registration for the event
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<String>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setValue(String object) {
        suggestBox.setValue(LanguageTagFormatter.format(object));
        setDirty(false, EventStrategy.DO_NOT_FIRE_EVENTS);
    }


    @Override
    public Optional<String> getValue() {
        String value = suggestBox.getText().trim();
        if(value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(value);
    }

    @Override
    public void clearValue() {
        suggestBox.setValue("");
        setDirty(false, EventStrategy.DO_NOT_FIRE_EVENTS);
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }

    /**
     * Determines if this object is dirty.
     * @return {@code true} if the object is dirty, otherwise {@code false}.
     */
    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public String getPlaceholder() {
        return suggestBox.getElement().getAttribute("placeholder");
    }

    @Override
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        setPlaceholderInternal(placeholder);
    }

    private void setPlaceholderInternal(String placeholder) {
        suggestBox.getElement().setAttribute("placeholder", placeholder);
    }


    private void setDirty(boolean dirty, EventStrategy eventStrategy) {
        this.dirty = dirty;
        if(eventStrategy == EventStrategy.FIRE_EVENTS) {
            fireEvent(new DirtyChangedEvent());
            Optional<String> value = getValue();
            ValueChangeEvent.fire(this, value);
        }
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }
}
