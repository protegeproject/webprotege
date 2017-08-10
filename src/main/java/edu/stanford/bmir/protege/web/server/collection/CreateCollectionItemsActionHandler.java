package edu.stanford.bmir.protege.web.server.collection;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.collection.CreateCollectionItemsAction;
import edu.stanford.bmir.protege.web.shared.collection.CreateCollectionItemsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Aug 2017
 */
public class CreateCollectionItemsActionHandler extends AbstractHasProjectActionHandler<CreateCollectionItemsAction, CreateCollectionItemsResult> {

    @Nonnull
    private final CollectionItemDataRepository repository;

    @Inject
    public CreateCollectionItemsActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull CollectionItemDataRepository repository) {
        super(accessManager);
        this.repository = checkNotNull(repository);
    }

    @Override
    public Class<CreateCollectionItemsAction> getActionClass() {
        return CreateCollectionItemsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Override
    public CreateCollectionItemsResult execute(CreateCollectionItemsAction action, ExecutionContext executionContext) {
        List<CollectionItem> items = action.getItems().stream()
                                           .map(CollectionItem::get)
                                           .collect(toList());
        repository.create(action.getCollectionId(), items);
        return new CreateCollectionItemsResult();
    }
}
