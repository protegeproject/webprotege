package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.pagination.Pager;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.issues.GetCommentedEntitiesAction;
import edu.stanford.bmir.protege.web.shared.issues.GetCommentedEntitiesResult;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public class GetCommentedEntitiesActionHandler extends AbstractHasProjectActionHandler<GetCommentedEntitiesAction, GetCommentedEntitiesResult> {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Inject
    public GetCommentedEntitiesActionHandler(@Nonnull ProjectManager projectManager,
                                             AccessManager accessManager,
                                             @Nonnull EntityDiscussionThreadRepository repository) {
        super(projectManager, accessManager);
        this.repository = repository;
    }

    @Override
    public Class<GetCommentedEntitiesAction> getActionClass() {
        return GetCommentedEntitiesAction.class;
    }

    @Override
    protected GetCommentedEntitiesResult execute(GetCommentedEntitiesAction action,
                                                 Project project,
                                                 ExecutionContext executionContext) {
        PageRequest request = action.getPageRequest();
        List<EntityDiscussionThread> allThreads = repository.getThreadsInProject(action.getProjectId());


        Map<OWLEntity, List<EntityDiscussionThread>> commentsByEntity = allThreads.stream()
                                                                                  .collect(groupingBy(
                                                                                          EntityDiscussionThread::getEntity));

        List<CommentedEntityData> result = new ArrayList<>();
        commentsByEntity.forEach((entity, threads) -> {
            if (project.getRootOntology().containsEntityInSignature(entity)) {
                int totalThreadCount = threads.size();
                int openThreadCount = (int) threads.stream()
                                                   .filter(thread -> thread.getStatus().isOpen())
                                                   .count();
                List<Comment> entityComments = threads.stream()
                                                      .flatMap(thread -> thread.getComments()
                                                                               .stream())
                                                      .collect(toList());
                Comment lastComment = entityComments.stream()
                                                    .max(comparing(c -> c.getUpdatedAt().orElse(c.getCreatedAt()))).get();

                List<UserId> participants = entityComments.stream()
                                                          .map(Comment::getCreatedBy)
                                                          .collect(toList());
                result.add(new CommentedEntityData(
                        project.getRenderingManager().getRendering(entity),
                        totalThreadCount,
                        openThreadCount,
                        entityComments.size(),
                        lastComment.getUpdatedAt().orElse(lastComment.getCreatedAt()),
                        lastComment.getCreatedBy(),
                        participants
                ));
            }
        });
        Pager<CommentedEntityData> pager = Pager.getPagerForPageSize(result, request.getPageSize());
        return new GetCommentedEntitiesResult(action.getProjectId(), pager.getPage(request.getPageNumber()));
    }
}
