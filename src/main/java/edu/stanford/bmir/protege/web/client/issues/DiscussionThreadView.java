package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.issues.Status;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public interface DiscussionThreadView extends IsWidget, HasEnabled {

    void clear();

    void setStatus(@Nonnull Status status);

    @Nonnull
    Status getStatus();

    void addCommentView(@Nonnull CommentView commentView);

    void setStatusChangedHandler(@Nonnull StatusChangedHandler handler);
}
