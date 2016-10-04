package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.library.timelabel.ElapsedTimeLabel;
import edu.stanford.bmir.protege.web.client.user.UserIcon;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

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

    @Inject
    public NoteViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiField
    protected AcceptsOneWidget iconHolder;

    @UiField
    protected HasText authorField;

    @UiField
    protected ElapsedTimeLabel dateField;

    @UiField
    protected HasSafeHtml bodyField;

    @Override
    public void setAuthor(String authorName) {
        authorField.setText(authorName);
        IsWidget widget = UserIcon.get(UserId.getUserId(authorName));
        iconHolder.setWidget(widget);
    }

    @Override
    public void setTimestamp(long timestamp) {
        dateField.setBaseTime(timestamp);
//        dateField.setText(new Date(timestamp).toString());
    }

    @Override
    public void setBody(SafeHtml safeHtml) {
        String modifiedHtml = parseLinks(safeHtml.asString());
        modifiedHtml = makeLinksOpenInNewWindow(modifiedHtml);
        bodyField.setHTML(new SafeHtmlBuilder().appendHtmlConstant(modifiedHtml).toSafeHtml());
    }


    private String makeLinksOpenInNewWindow(String html) {
        RegExp linkRegExp = RegExp.compile("(<a)([^>]*)(>)", "g");
        return linkRegExp.replace(html, "$1$2 target=\"_blank\"$3");
    }

    private String parseLinks(String html) {
//        RegExp urlRegExp = RegExp.compile("(https?://[^\\s<]*)", "g");
//        return urlRegExp.replace(html, "<a href=\"$1\">$1</a>");
        return html;
    }


    @Override
    public Widget getWidget() {
        return this;
    }
}