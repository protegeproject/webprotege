package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class DiscussionThreadViewImpl extends Composite implements DiscussionThreadView {

    private static final int REPLY_INDENT_PX = 20;

    interface DiscussionThreadViewImplUiBinder extends UiBinder<HTMLPanel, DiscussionThreadViewImpl> {

    }

    private static DiscussionThreadViewImplUiBinder ourUiBinder = GWT.create(DiscussionThreadViewImplUiBinder.class);

    public DiscussionThreadViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }


    @UiField
    protected FlexTable notesList;


    @Override
    public void removeAllNotes() {
        notesList.removeAllRows();
    }

    @Override
    public void addNote(NoteContainerPresenter notePresenter, int depth) {
        Widget noteView = notePresenter.getWidget();
        if(!notePresenter.getNote().getInReplyTo().isPresent()) {
            final NoteSubjectView noteSubjectLabel = new NoteSubjectViewImpl();

            notesList.setWidget(notesList.getRowCount(), 0, noteSubjectLabel.getWidget());
        }
        SimplePanel wrapperPanel = new SimplePanel(noteView);
        Element element = wrapperPanel.getElement();
        Style style = element.getStyle();
        style.setMarginLeft(depth * REPLY_INDENT_PX, Style.Unit.PX);
        notesList.setWidget(notesList.getRowCount(), 0, wrapperPanel);
    }

//    @Override
//    public void removeNote(NoteView noteView) {
//        notesList.remove(noteView.getWidget());
//    }

    @Override
    public Widget getWidget() {
        return this;
    }
}