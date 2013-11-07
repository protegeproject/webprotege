package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.notes.NoteDetails;
import edu.stanford.bmir.protege.web.shared.notes.NoteHeader;
import edu.stanford.bmir.protege.web.shared.notes.NoteType;

import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class NoteDetailsEditor extends Composite implements NoteDetailsDisplay {


    private NoteEditorPanel editorPanel = new NoteEditorPanel();

    private Optional<NoteDetails> lastDetails;

    public NoteDetailsEditor() {
        initWidget(editorPanel);
    }

    @Override
    public void setValue(NoteDetails object) {
        lastDetails = Optional.of(object);
        editorPanel.setAuthor(object.getNoteHeader().getAuthor().getUserName());
        editorPanel.setDate(object.getNoteHeader().getTimestamp());
        editorPanel.setBody(object.getNoteContent().getBody().or(""));
    }

    @Override
    public void clearValue() {
        editorPanel.setAuthor("");
        editorPanel.setDate(new Date().getTime());
        editorPanel.setBody("");
    }

    @Override
    public Optional<NoteDetails> getValue() {
        if(!lastDetails.isPresent()) {
            return Optional.absent();
        }
        NoteDetails last = lastDetails.get();
        NoteHeader header = last.getNoteHeader();
        NoteContent.Builder contentBuilder = NoteContent.builder();
        contentBuilder.setNoteType(NoteType.COMMENT);
        contentBuilder.setBody(editorPanel.getBody());
        NoteContent content = contentBuilder.build();
        return Optional.of(new NoteDetails(header, content));
    }

    @Override
    public Widget getWidget() {
        return editorPanel;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<NoteDetails>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return getValue().isPresent();
    }
}
