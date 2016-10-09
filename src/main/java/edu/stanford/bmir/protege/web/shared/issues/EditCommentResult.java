package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2016
 */
public class EditCommentResult implements Result {

    private Comment editedComment;

    public EditCommentResult(@Nonnull Comment editedComment) {
        this.editedComment = checkNotNull(editedComment);
    }

    @GwtSerializationConstructor
    private EditCommentResult() {
    }

    @Nonnull
    public Comment getEditedComment() {
        return editedComment;
    }
}
