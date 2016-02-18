package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class DiscussionThreadViewImpl extends Composite implements DiscussionThreadView {

    private static final int REPLY_INDENT_PX = 10;

    interface DiscussionThreadViewImplUiBinder extends UiBinder<HTMLPanel, DiscussionThreadViewImpl> {

    }

    private static DiscussionThreadViewImplUiBinder ourUiBinder = GWT.create(DiscussionThreadViewImplUiBinder.class);


    private Set<NoteHeaderPresenter> currentPresenters = new HashSet<NoteHeaderPresenter>();

    private final Provider<NoteHeaderPresenter> noteHeaderPresenterProvider;

    @Inject
    public DiscussionThreadViewImpl(Provider<NoteHeaderPresenter> noteHeaderPresenterProvider) {
        this.noteHeaderPresenterProvider = noteHeaderPresenterProvider;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiField
    protected FlexTable notesList;

    @Override
    public void removeAllNotes() {
        notesList.removeAllRows();
        for(NoteHeaderPresenter presenter : currentPresenters) {
            presenter.dispose();
        }
        currentPresenters.clear();
    }

    @Override
    public void addNote(NoteContainerPresenter notePresenter, int depth) {
        Widget noteView = notePresenter.getWidget();
        if(!notePresenter.getNote().getInReplyTo().isPresent()) {
            NoteHeaderPresenter subjectPresenter = noteHeaderPresenterProvider.get();
            currentPresenters.add(subjectPresenter);
            subjectPresenter.setNote(notePresenter.getNote());
            notesList.setWidget(notesList.getRowCount(), 0, subjectPresenter.getWidget());
        }
        SimplePanel wrapperPanel = new SimplePanel(noteView);
        if (depth > 0) {
            Element element = wrapperPanel.getElement();
            Style style = element.getStyle();
            style.setMarginLeft(REPLY_INDENT_PX, Style.Unit.PX);
        }
        notesList.setWidget(notesList.getRowCount(), 0, wrapperPanel);
    }




    @Override
    public Widget getWidget() {
        return this;
    }
}