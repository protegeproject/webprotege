package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.notes.DiscussionThread;
import edu.stanford.bmir.protege.web.shared.notes.Note;

import javax.inject.Inject;

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

    @Inject
    public NoteContainerViewImpl(NotePresenter notePresenter, NoteActionPresenter noteActionPresenter) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        this.notePresenter = notePresenter;
        this.noteActionPresenter = noteActionPresenter;
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