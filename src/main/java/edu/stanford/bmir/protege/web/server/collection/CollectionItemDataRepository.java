package edu.stanford.bmir.protege.web.server.collection;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItemData;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Aug 2017
 */
public interface CollectionItemDataRepository extends Repository {

    void save(CollectionItemData data);

    long count(CollectionId collectionId);

    @Nonnull
    List<CollectionItemData> find(CollectionId collectionId);

    @Nonnull
    CollectionItemData find(@Nonnull CollectionId collectionId,
                            @Nonnull CollectionItem elementId);

    @Nonnull
    List<CollectionItem> list(@Nonnull CollectionId collectionId,
                              int skip,
                              int limit);

    void create(CollectionId collectionId, List<CollectionItem> items);
}
