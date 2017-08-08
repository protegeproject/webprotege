package edu.stanford.bmir.protege.web.server.collection;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;
import edu.stanford.bmir.protege.web.shared.collection.GetCollectionElementsAction;
import edu.stanford.bmir.protege.web.shared.collection.GetCollectionElementsResult;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Aug 2017
 */
public class GetCollectionElementsActionHandler extends AbstractHasProjectActionHandler<GetCollectionElementsAction, GetCollectionElementsResult> {

    @Nonnull
    private final CollectionElementDataRepository repository;

    @Inject
    public GetCollectionElementsActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull CollectionElementDataRepository repository) {
        super(accessManager);
        this.repository = checkNotNull(repository);
    }

    @Override
    public Class<GetCollectionElementsAction> getActionClass() {
        return GetCollectionElementsAction.class;
    }

    @Override
    public GetCollectionElementsResult execute(GetCollectionElementsAction action, ExecutionContext executionContext) {
        List<CollectionElementId> elementIdList = repository.list(action.getCollectionId(),
                                                                  action.getPageRequest().getSkip(),
                                                                  action.getPageRequest().getPageSize());
        return new GetCollectionElementsResult(new Page<>(action.getPageRequest().getPageNumber(),
                                                          1,
                                                          elementIdList,
                                                          elementIdList.size()),
                                               action.getPageRequest());
    }
}
