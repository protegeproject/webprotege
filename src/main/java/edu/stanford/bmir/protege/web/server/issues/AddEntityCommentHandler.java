package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.issues.mention.MentionParser;
import edu.stanford.bmir.protege.web.shared.issues.mention.ParsedMention;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_OBJECT_COMMENT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class AddEntityCommentHandler extends AbstractHasProjectActionHandler<AddEntityCommentAction, AddEntityCommentResult> {

    private final EntityDiscussionThreadRepository repository;

    @Inject
    public AddEntityCommentHandler(OWLAPIProjectManager projectManager,
                                   AccessManager accessManager,
                                   EntityDiscussionThreadRepository repository) {
        super(projectManager, accessManager);
        this.repository = checkNotNull(repository);
    }

    @Override
    public Class<AddEntityCommentAction> getActionClass() {
        return AddEntityCommentAction.class;
    }

    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return CREATE_OBJECT_COMMENT;
    }

    @Override
    protected AddEntityCommentResult execute(AddEntityCommentAction action,
                                             OWLAPIProject project,
                                             ExecutionContext executionContext) {
        UserId createdBy = executionContext.getUserId();
        long createdAt = System.currentTimeMillis();
        MentionParser mentionParser = new MentionParser();
        List<ParsedMention> mentionList = mentionParser.parseMentions(action.getComment());
        // TODO: Mail mentioned users
        mentionList.forEach(m -> m.getParsedMention().getMentionedUserId().ifPresent(u -> System.out.println("User mentioned: " + u)));

        CommentRenderer r = new CommentRenderer();
        String rendering = r.renderComment(action.getComment());

        Comment comment = new Comment(CommentId.create(), createdBy, createdAt, Optional.empty(), action.getComment(), rendering);
        ThreadId threadId = action.getThreadId();
        repository.addCommentToThread(threadId, comment);
        project.getEventManager().postEvent(new CommentPostedEvent(action.getProjectId(), threadId, comment));
        return new AddEntityCommentResult(action.getProjectId(), threadId, comment, rendering);

    }
}
