package edu.stanford.bmir.protege.web.client.issues;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.ui.library.richtext.RichTextEditorPresenter;
import edu.stanford.bmir.protege.web.client.ui.library.richtext.RichTextEditorViewImpl;
import edu.stanford.bmir.protege.web.client.ui.library.richtext.RichTextToolbarImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class CommentEditorViewImpl extends Composite implements CommentEditorView {

    private final RichTextEditorPresenter richTextEditorPresenter;

    interface CommentEditorViewImplUiBinder extends UiBinder<HTMLPanel, CommentEditorViewImpl> {

    }

    private static CommentEditorViewImplUiBinder ourUiBinder = GWT.create(CommentEditorViewImplUiBinder.class);

    @UiField
    RichTextToolbarImpl toolbar;

    @UiField
    RichTextEditorViewImpl bodyField;

    @Inject
    public CommentEditorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        richTextEditorPresenter = new RichTextEditorPresenter(toolbar, bodyField);
    }

    @Override
    public void clear() {
        bodyField.setText("");
    }

    @Override
    public void setBody(@Nonnull String body) {
        this.bodyField.setText(body);
    }

    @Nonnull
    @Override
    public String getBody() {
        return bodyField.getText().trim();
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return Optional.of(bodyField);
    }
}