package edu.stanford.bmir.protege.web.client.crud.obo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.user.UserIdSuggestOracle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.crud.oboid.UserIdRange;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class UserIdRangeEditorImpl extends Composite implements UserIdRangeEditor {

    interface UserIdRangeEditorImplUiBinder extends UiBinder<HTMLPanel, UserIdRangeEditorImpl> {

    }

    private static UserIdRangeEditorImplUiBinder ourUiBinder = GWT.create(UserIdRangeEditorImplUiBinder.class);


    @UiField(provided = true)
    protected SuggestBox userIdEditor;

    @UiField
    protected TextBox startEditor;

    @UiField
    protected TextBox endEditor;

    private boolean dirty;

    @Inject
    public UserIdRangeEditorImpl(UserIdSuggestOracle userIdSuggestOracle) {
        userIdEditor = new SuggestBox(userIdSuggestOracle);
        userIdEditor.getElement().setAttribute("placeholder", "Type user name");
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiHandler("userIdEditor")
    protected void handleUserIdChanged(ValueChangeEvent<String> evt) {
        fireValueChanged();
        setDirty();
    }

    private void fireValueChanged() {
        ValueChangeEvent.fire(this, getValue());
    }

    private void setDirty() {
        if (!dirty) {
            dirty = true;
            fireEvent(new DirtyChangedEvent());
        }
    }

    @UiHandler("startEditor")
    protected void handleStartChanged(ValueChangeEvent<String> evt) {
        fireValueChanged();
        setDirty();
    }

    @UiHandler("endEditor")
    protected void handleEndChanged(ValueChangeEvent<String> evt) {
        fireValueChanged();
        setDirty();
    }

    @Override
    public boolean isWellFormed() {
        return getStart().isPresent() && getEnd().isPresent();
    }

    @Override
    public void setValue(UserIdRange object) {
        userIdEditor.setValue(object.getUserId().getUserName());
        if (object.getStart() != 0) {
            startEditor.setValue(Long.toString(object.getStart()));
        }
        else {
            startEditor.setValue("");
        }
        if (object.getEnd() != getDefaultEndValue()) {
            endEditor.setValue(Long.toString(object.getEnd()));
        }
        else {
            endEditor.setText("");
        }
        clearDirty();
    }

    private void clearDirty() {
        dirty = false;
    }

    @Override
    public void clearValue() {
        userIdEditor.setText("");
        startEditor.setText("");
        endEditor.setText("");
        clearDirty();
    }

    @Override
    public Optional<UserIdRange> getValue() {
        if (userIdEditor.getText().trim().isEmpty()) {
            return Optional.empty();
        }
        if(!getStart().isPresent()) {
            return Optional.empty();
        }
        if(!getEnd().isPresent()) {
            return Optional.empty();
        }
        long start = getStart().get();
        long end = getEnd().get();
        if(end < start) {
            end = getDefaultEndValue();
        }
        return Optional.of(new UserIdRange(UserId.getUserId(userIdEditor.getText().trim()), start, end));
    }

    private static long getDefaultEndValue() {
        return Long.MAX_VALUE;
    }

    private Optional<Long> getStart() {
        final String startText = getStartText();
        if (startText.isEmpty()) {
            return Optional.of(0l);
        }
        try {
            return Optional.of(Long.parseLong(startText));
        }
        catch (NumberFormatException e) {
            return Optional.of(0l);
        }
    }

    private Optional<Long> getEnd() {
        final String endText = getEndText();
        if (endText.isEmpty()) {
            return Optional.of(getDefaultEndValue());
        }
        try {
            return Optional.of(Long.parseLong(endText));
        }
        catch (NumberFormatException e) {
            return Optional.of(getDefaultEndValue());
        }
    }


    /**
     * @return A String representing the text in the start editor.
     */
    private String getStartText() {
        return startEditor.getText().trim();
    }

    /**
     * Gets the end text.
     * @return A string representing the trimmed end text.
     */
    private String getEndText() {
        return endEditor.getText().trim();
    }


    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<UserIdRange>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}