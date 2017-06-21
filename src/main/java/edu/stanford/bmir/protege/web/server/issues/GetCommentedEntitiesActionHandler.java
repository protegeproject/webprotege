package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.mansyntax.render.HasGetRendering;
import edu.stanford.bmir.protege.web.server.pagination.Pager;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData.byEntity;
import static edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData.byLastModified;
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

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final HasGetRendering renderer;

    @Inject
    public GetCommentedEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull EntityDiscussionThreadRepository repository,
                                             @Nonnull OWLOntology rootOntology,
                                             @Nonnull HasGetRendering renderer) {
        super(accessManager);
        this.repository = repository;
        this.rootOntology = rootOntology;
        this.renderer = renderer;
    }

    @Override
    public Class<GetCommentedEntitiesAction> getActionClass() {
        return GetCommentedEntitiesAction.class;
    }

    @Override
    public GetCommentedEntitiesResult execute(GetCommentedEntitiesAction action,
                                                 ExecutionContext executionContext) {
        PageRequest request = action.getPageRequest();
        List<EntityDiscussionThread> allThreads = repository.getThreadsInProject(action.getProjectId());


        Map<OWLEntity, List<EntityDiscussionThread>> commentsByEntity = allThreads.stream()
                                                                                  .collect(groupingBy(
                                                                                          EntityDiscussionThread::getEntity));

        List<CommentedEntityData> result = new ArrayList<>();
        commentsByEntity.forEach((entity, threads) -> {
            if (rootOntology.containsEntityInSignature(entity)) {
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
                        renderer.getRendering(entity),
                        totalThreadCount,
                        openThreadCount,
                        entityComments.size(),
                        lastComment.getUpdatedAt().orElse(lastComment.getCreatedAt()),
                        lastComment.getCreatedBy(),
                        participants
                ));
            }
        });
        if(action.getSortingKey() == SortingKey.SORT_BY_ENTITY) {
            result.sort(byEntity);
        }
        else {
            result.sort(byLastModified);
        }
        Pager<CommentedEntityData> pager = Pager.getPagerForPageSize(result, request.getPageSize());
        return new GetCommentedEntitiesResult(action.getProjectId(), pager.getPage(request.getPageNumber()));
    }
}
