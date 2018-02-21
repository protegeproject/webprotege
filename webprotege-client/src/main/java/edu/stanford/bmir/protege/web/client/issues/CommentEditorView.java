package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public interface CommentEditorView extends IsWidget, HasInitialFocusable {

    void clear();

    void setBody(@Nonnull String body);

    @Nonnull
    String getBody();
}
