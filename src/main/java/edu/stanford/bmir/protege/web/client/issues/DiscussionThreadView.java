package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.issues.Status;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public interface DiscussionThreadView extends IsWidget, HasEnabled {

    void clear();

    void setStatus(Status status);

    void addCommentView(CommentView commentView);

    void setStatusChangedHandler(StatusChangedHandler handler);
}
