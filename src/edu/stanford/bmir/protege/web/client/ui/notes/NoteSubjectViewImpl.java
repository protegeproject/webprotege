package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public class NoteSubjectViewImpl extends Composite implements NoteSubjectView {

    interface NoteSubjectViewImplUiBinder extends UiBinder<HTMLPanel, NoteSubjectViewImpl> {

    }

    private static NoteSubjectViewImplUiBinder ourUiBinder = GWT.create(NoteSubjectViewImplUiBinder.class);

    @UiField
    protected HasText subjectLabel;

    public NoteSubjectViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public void setSubject(Optional<String> subject) {
        subjectLabel.setText(subject.or(""));
    }

    @Override
    public Widget getWidget() {
        return this;
    }
}