package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.shared.notes.NoteStatus;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public class NoteHeaderViewImpl extends Composite implements NoteHeaderView {

    interface NoteHeaderViewImplUiBinder extends UiBinder<HTMLPanel, NoteHeaderViewImpl> {

    }

    private static NoteHeaderViewImplUiBinder ourUiBinder = GWT.create(NoteHeaderViewImplUiBinder.class);


    private ResolveNoteHandler resolvedHandler = new ResolveNoteHandler() {
        @Override
        public void handleResolvePressed() {
            GWT.log("ResolveNoteHandler not registered");
        }
    };

    @UiField
    protected HasText subjectLabel;

    @UiField
    protected HasText startedByLabel;

    @UiField
    protected ButtonBase toggleStatusButton;

    @UiField
    protected HasText statusLabel;

    public NoteHeaderViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiHandler("toggleStatusButton")
    protected void handleToggleStatus(ClickEvent clickEvent) {
        resolvedHandler.handleResolvePressed();
    }

    @Override
    public void setSubject(Optional<String> subject) {
        subjectLabel.setText(subject.or(""));
    }

    @Override
    public void setAuthor(UserId userId) {
        startedByLabel.setText(userId.getUserName());
    }

    @Override
    public void setResolveOptionVisible(boolean b) {
        toggleStatusButton.setVisible(b);
    }

    @Override
    public void setReolveOptionText(String text) {
        toggleStatusButton.setText(text);
    }

    @Override
    public void setResolveNoteHandler(ResolveNoteHandler handler) {
        resolvedHandler = handler;
    }

    @Override
    public void setStatus(Optional<NoteStatus> noteStatus) {
        if (noteStatus.isPresent()) {
            statusLabel.setText(noteStatus.get().getDisplayText());
        }
        else {
            statusLabel.setText("open");
        }
    }

    @Override
    public Widget getWidget() {
        return this;
    }
}