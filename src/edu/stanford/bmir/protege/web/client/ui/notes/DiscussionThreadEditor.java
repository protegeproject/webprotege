package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.notes.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class DiscussionThreadEditor implements DiscussionThreadDisplay {

    private FlowPanel widget = new FlowPanel();

    public DiscussionThreadEditor() {
        for(int i = 0; i < 5; i++) {
            NoteDetailsEditor editor = new NoteDetailsEditor();
            final NoteHeader noteHeader = new NoteHeader(NoteId.createNoteIdFromLexicalForm(""), Optional.<NoteId>absent(), UserId.getUserId("matthewhorridge"), new Date().getTime());
            final String body = "Test note " + i + ". Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
            editor.setValue(new NoteDetails(noteHeader, NoteContent.builder().setNoteType(NoteType.COMMENT).setBody(body).build()));
            widget.add(editor);
        }
    }

    @Override
    public void setValue(DiscussionThread object) {
    }

    @Override
    public void clearValue() {
    }

    @Override
    public Optional<DiscussionThread> getValue() {
        return Optional.absent();
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return widget.addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<DiscussionThread>> handler) {
        return widget.addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        widget.fireEvent(event);
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }
}
