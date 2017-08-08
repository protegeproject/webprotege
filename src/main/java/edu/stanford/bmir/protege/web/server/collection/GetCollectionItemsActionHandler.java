package edu.stanford.bmir.protege.web.server.collection;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.collection.GetCollectionItemsAction;
import edu.stanford.bmir.protege.web.shared.collection.GetCollectionItemsResult;
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
public class GetCollectionItemsActionHandler extends AbstractHasProjectActionHandler<GetCollectionItemsAction, GetCollectionItemsResult> {

    @Nonnull
    private final CollectionItemDataRepository repository;

    @Inject
    public GetCollectionItemsActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull CollectionItemDataRepository repository) {
        super(accessManager);
        this.repository = checkNotNull(repository);
    }

    @Override
    public Class<GetCollectionItemsAction> getActionClass() {
        return GetCollectionItemsAction.class;
    }

    @Override
    public GetCollectionItemsResult execute(GetCollectionItemsAction action, ExecutionContext executionContext) {
        List<CollectionItem> elementIdList = repository.list(action.getCollectionId(),
                                                             action.getPageRequest().getSkip(),
                                                             action.getPageRequest().getPageSize());
        return new GetCollectionItemsResult(new Page<>(action.getPageRequest().getPageNumber(),
                                                       1,
                                                       elementIdList,
                                                       elementIdList.size()),
                                            action.getPageRequest());
    }
}
