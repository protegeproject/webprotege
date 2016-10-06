package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public interface CommentView extends IsWidget, HasEnabled {

    void setCreatedBy(UserId createdBy);

    void setCreatedAt(long timestamp);

    void setUpdatedAt(Optional<Long> updatedAt);

    void setBody(String body);

    void setReplyToCommentHandler(ReplyToCommentHandler handler);
}
