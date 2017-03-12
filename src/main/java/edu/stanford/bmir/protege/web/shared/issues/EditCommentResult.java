package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2016
 */
public class EditCommentResult implements Result, HasEventList<ProjectEvent<?>> {

    @Nullable
    private Comment editedComment;

    private EventList<ProjectEvent<?>> projectEventList;

    public EditCommentResult(@Nonnull Optional<Comment> editedComment,
                             @Nonnull EventList<ProjectEvent<?>> projectEventList) {
        this.editedComment = checkNotNull(editedComment).orElse(null);
        this.projectEventList = checkNotNull(projectEventList);
    }

    @GwtSerializationConstructor
    private EditCommentResult() {
    }

    @Nonnull
    public Optional<Comment> getEditedComment() {
        return Optional.ofNullable(editedComment);
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return projectEventList;
    }
}
