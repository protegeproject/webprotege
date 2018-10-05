package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionCallback;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.gwtcodemirror.client.GWTCodeMirror;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class CommentEditorViewImpl extends Composite implements CommentEditorView {

    private final CommentAutoCompleter commentAutoCompleter;

    interface CommentEditorViewImplUiBinder extends UiBinder<HTMLPanel, CommentEditorViewImpl> {

    }

    private static CommentEditorViewImplUiBinder ourUiBinder = GWT.create(CommentEditorViewImplUiBinder.class);

    @UiField(provided = true)
    GWTCodeMirror bodyField;

    @Inject
    public CommentEditorViewImpl(CommentAutoCompleter commentAutoCompleter) {
        this.commentAutoCompleter = commentAutoCompleter;
        bodyField = new GWTCodeMirror("gfm");
        bodyField.setAutoCompletionHandler(this::handleAutocomplete);
        bodyField.setLineNumbersVisible(false);
        bodyField.addAutoCompleteCharacter('@');
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    private void handleAutocomplete(String query, EditorPosition caretPosition, int caretPos, AutoCompletionCallback callback) {
        commentAutoCompleter.handleAutocomplete(query, caretPosition, caretPos, callback);
    }

    @Override
    public void clear() {
        bodyField.setValue("");
    }

    @Override
    public void setBody(@Nonnull String body) {
        Scheduler.get().scheduleDeferred(() -> bodyField.setValue(body));
    }

    @Nonnull
    @Override
    public String getBody() {
        return bodyField.getValue().trim();
    }

    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return java.util.Optional.of(() -> bodyField.setFocus(true));
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Scheduler.get().scheduleDeferred(() -> focus(bodyField.getElement()));
    }

    private void focus(@Nonnull Element element) {
        if("textarea".equalsIgnoreCase(element.getTagName())) {
            element.focus();
            return;
        }
        for(int i = 0; i < element.getChildCount(); i++) {
            Node node = element.getChild(i);
            if(Element.is(node)) {
                focus(Element.as(node));
            }
        }
    }
}