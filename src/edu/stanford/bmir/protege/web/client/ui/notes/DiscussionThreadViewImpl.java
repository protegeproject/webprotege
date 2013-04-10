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

    interface DiscussionThreadViewImplUiBinder extends UiBinder<HTMLPanel, DiscussionThreadViewImpl> {

    }

    private static DiscussionThreadViewImplUiBinder ourUiBinder = GWT.create(DiscussionThreadViewImplUiBinder.class);

    public DiscussionThreadViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }


    @UiField
    protected HasWidgets notesList;


    @Override
    public void removeAllNotes() {
        notesList.clear();
    }

    @Override
    public void addNote(Widget noteView, int depth) {
        SimplePanel wrapperPanel = new SimplePanel(noteView);
        Element element = wrapperPanel.getElement();
        Style style = element.getStyle();
        style.setMarginLeft(depth * 40, Style.Unit.PX);
        notesList.add(wrapperPanel);
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