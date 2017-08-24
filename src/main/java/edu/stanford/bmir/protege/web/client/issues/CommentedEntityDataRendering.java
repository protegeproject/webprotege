package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.shared.TimeUtil;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2017
 */
class CommentedEntityDataRendering {

    private static final String UNRESOLVED_COLOR = "#909";

    private static final String RESOLVED_COLOR = "#090";

    private final CommentedEntityData data;

    private static final Messages MESSAGES = GWT.create(Messages.class);

    public CommentedEntityDataRendering(@Nonnull CommentedEntityData data) {
        this.data = checkNotNull(data);
    }

    @Nonnull
    public String entityBrowserText() {
        return data.getEntityData().getBrowserText();
    }

    @Nonnull
    public String modifiedAt() {
        return TimeUtil.getTimeRendering(data.getLastModified()).toLowerCase();
    }

    @Nonnull
    public String modifiedBy() {
        return data.getLastModifiedBy().getUserName();
    }

    @Nonnull
    public String status() {
        if (data.getOpenThreadCount() > 0) {
            return data.getOpenThreadCount() + " " + MESSAGES.comments_unresolved();
        }
        else {
            return MESSAGES.comments_resolved();
        }
    }

    @Nonnull
    public String statusColor() {
        if (data.getOpenThreadCount() > 0) {
            return UNRESOLVED_COLOR;
        }
        else {
            return RESOLVED_COLOR;
        }
    }

    @Nonnull
    public String commentCount() {
        return Integer.toString(data.getTotalCommentCount());
    }
}
