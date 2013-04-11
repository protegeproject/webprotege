package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class NoteViewImpl extends Composite implements NoteView {

    interface NoteViewImplUiBinder extends UiBinder<HTMLPanel, NoteViewImpl> {

    }

    private static NoteViewImplUiBinder ourUiBinder = GWT.create(NoteViewImplUiBinder.class);

    public NoteViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiField
    protected HasText authorField;

    @UiField
    protected HasText dateField;

    @UiField
    protected HasSafeHtml bodyField;

    @Override
    public void setAuthor(String authorName) {
        authorField.setText(authorName);
    }

    @Override
    public void setTimestamp(long timestamp) {
        dateField.setText(new Date(timestamp).toString());
    }

    @Override
    public void setBody(SafeHtml safeHtml) {
        bodyField.setHTML(safeHtml);
    }

    @Override
    public Widget getWidget() {
        return this;
    }
}