package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-12
 */
public class StubLanguageEditor implements LanguageEditor {

    @Nullable
    private String value = null;

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void setValue(String object) {
        this.value = object;
    }

    @Override
    public void clearValue() {
        this.value = null;
    }

    @Override
    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<String>> handler) {
        return () -> { };
    }

    @Override
    public Widget asWidget() {
        throw new RuntimeException("asWidget Not stubbed");
    }

    @Override
    public String getPlaceholder() {
        return "";
    }

    @Override
    public void setPlaceholder(String placeholder) {

    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return () -> {};
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {

    }

    @Override
    public boolean isWellFormed() {
        return true;
    }
}
