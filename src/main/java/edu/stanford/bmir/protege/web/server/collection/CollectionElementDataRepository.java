package edu.stanford.bmir.protege.web.server.collection;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementData;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Aug 2017
 */
public interface CollectionElementDataRepository extends Repository {

    void save(CollectionElementData data);

    long count(CollectionId collectionId);

    @Nonnull
    List<CollectionElementData> find(CollectionId collectionId);

    @Nonnull
    CollectionElementData find(@Nonnull CollectionId collectionId,
                               @Nonnull CollectionElementId elementId);

    @Nonnull
    List<CollectionElementId> list(@Nonnull CollectionId collectionId,
                                   int skip,
                                   int limit);
}
