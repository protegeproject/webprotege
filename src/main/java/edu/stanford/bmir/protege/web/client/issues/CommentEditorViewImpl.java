package edu.stanford.bmir.protege.web.client.issues;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.gwtcodemirror.client.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
    public java.util.Optional<Focusable> getInitialFocusable() {
        return java.util.Optional.of(bodyField);
    }
}