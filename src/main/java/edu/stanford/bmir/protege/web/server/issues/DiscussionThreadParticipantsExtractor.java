package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2017
 */
public class DiscussionThreadParticipantsExtractor {

    private final CommentParticipantsExtractor extractor;

    @Inject
    public DiscussionThreadParticipantsExtractor(CommentParticipantsExtractor extractor) {
        this.extractor = extractor;
    }

    public Set<UserId> extractParticipants(EntityDiscussionThread thread) {
        return thread.getComments().stream()
                .flatMap(comment -> extractor.extractParticipants(comment).stream())
                .collect(toSet());
    }
}
