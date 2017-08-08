package edu.stanford.bmir.protege.web.server.collection;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementData;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.UpdateOptions;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.collection.CollectionElementData.*;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jul 2017
 */
@ApplicationSingleton
public class CollectionElementDataRepositoryImpl implements CollectionElementDataRepository {

    @Nonnull
    private final Datastore datastore;

    @Inject
    public CollectionElementDataRepositoryImpl(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    @Override
    public void ensureIndexes() {
        datastore.ensureIndexes(CollectionElementData.class);
    }

    @Override
    public void save(CollectionElementData data) {
        Query<CollectionElementData> query = createQuery(data.getCollectionId())
                .field(ELEMENT_ID).equal(data.getElementId());
        UpdateOperations<CollectionElementData> updateOperations = datastore.createUpdateOperations(
                CollectionElementData.class);
        Optional<FormData> formData = data.getFormData();
        if (formData.isPresent()) {
            updateOperations.set(FORM_DATA, formData.get());
        }
        else {
            updateOperations.unset(FORM_DATA);
        }
        datastore.update(query, updateOperations, new UpdateOptions().upsert(true));
    }

    @Override
    public long count(CollectionId collectionId) {
        return createQuery(collectionId)
                .count();
    }

    @Override
    @Nonnull
    public List<CollectionElementData> find(CollectionId collectionId) {
        return createQuery(collectionId).asList();
    }

    @Override
    @Nonnull
    public CollectionElementData find(@Nonnull CollectionId collectionId,
                                      @Nonnull CollectionElementId elementId) {
        CollectionElementData data = createQuery(collectionId)
                .field(ELEMENT_ID).equal(elementId).get();
        if (data == null) {
            return new CollectionElementData(collectionId, elementId);
        }
        else {
            return data;
        }
    }

    @Override
    @Nonnull
    public List<CollectionElementId> list(@Nonnull CollectionId collectionId,
                                          int skip,
                                          int limit) {
        return datastore.createQuery(CollectionElementData.class)
                        .field(COLLECTION_ID).equal(collectionId)
                        .asList(new FindOptions().limit(limit).skip(skip))
                        .stream()
                        .map(CollectionElementData::getElementId)
                        .collect(toList());
    }


    private Query<CollectionElementData> createQuery(CollectionId collectionId) {
        return datastore.createQuery(CollectionElementData.class)
                        .field(COLLECTION_ID).equal(collectionId);
    }
}
