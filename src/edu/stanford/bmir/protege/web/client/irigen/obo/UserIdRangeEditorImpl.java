package edu.stanford.bmir.protege.web.client.irigen.obo;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.library.itemarea.ItemListSuggestBox;
import edu.stanford.bmir.protege.web.client.ui.library.itemarea.UserIdSuggestOracle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.irigen.obo.UserIdRange;
import edu.stanford.bmir.protege.web.shared.user.UserId;

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
    protected ItemListSuggestBox<UserId> userIdEditor;

    @UiField
    protected TextBox startEditor;

    @UiField
    protected TextBox endEditor;

    private boolean dirty;

    public UserIdRangeEditorImpl() {
        userIdEditor = new ItemListSuggestBox<UserId>(new UserIdSuggestOracle(), new TextBox());
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
            return Optional.absent();
        }
        if(!getStart().isPresent()) {
            return Optional.absent();
        }
        if(!getEnd().isPresent()) {
            return Optional.absent();
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