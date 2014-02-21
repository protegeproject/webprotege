package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.notes.DiscussionThread;
import edu.stanford.bmir.protege.web.shared.notes.Note;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class NoteContainerViewImpl extends Composite implements NoteContainerView {



    interface NoteContainerViewImplUiBinder extends UiBinder<HTMLPanel, NoteContainerViewImpl> {

    }

    private static NoteContainerViewImplUiBinder ourUiBinder = GWT.create(NoteContainerViewImplUiBinder.class);

    @UiField
    protected NoteView noteView;

    @UiField
    protected NoteActionView noteActionView;


    private NotePresenter notePresenter;

    private NoteActionPresenter noteActionPresenter;

    public NoteContainerViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        this.notePresenter = new NotePresenter(noteView);
        this.noteActionPresenter = new NoteActionPresenter(noteActionView);
    }



    @Override
    public void setNote(Note note, DiscussionThread context) {
        notePresenter.setNote(note);
        noteActionPresenter.setNote(note, context);
    }

    @Override
    public Widget getWidget() {
        return this;
    }
}