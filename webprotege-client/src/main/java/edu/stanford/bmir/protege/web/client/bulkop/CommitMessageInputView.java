package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2018
 */
public interface CommitMessageInputView extends IsWidget {

    void setDefaultCommitMessage(@Nonnull String message);

    @Nonnull
    String getCommitMessage();
}
