package edu.stanford.bmir.protege.web.server.collection;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.Sorts;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItemData;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.InsertOptions;
import org.mongodb.morphia.UpdateOptions;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Sort;
import org.mongodb.morphia.query.UpdateOperations;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.collection.CollectionItemData.*;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jul 2017
 */
@ApplicationSingleton
public class CollectionItemDataRepositoryImpl implements CollectionItemDataRepository {

    @Nonnull
    private final Datastore datastore;

    @Inject
    public CollectionItemDataRepositoryImpl(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    @Override
    public void ensureIndexes() {
        datastore.ensureIndexes(CollectionItemData.class);
    }

    @Override
    public void save(CollectionItemData data) {
        Query<CollectionItemData> query = createQuery(data.getCollectionId())
                .field(ITEM).equal(data.getItem());
        UpdateOperations<CollectionItemData> updateOperations = datastore.createUpdateOperations(
                CollectionItemData.class);
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
    public void create(CollectionId collectionId, List<CollectionItem> items) {
        List<CollectionItemData> data = items.stream()
                .map(item -> new CollectionItemData(collectionId, item))
                .collect(toList());
        datastore.save(data, new InsertOptions().continueOnError(true));
    }

    @Override
    @Nonnull
    public List<CollectionItemData> find(CollectionId collectionId) {
        return createQuery(collectionId).asList();
    }

    @Override
    @Nonnull
    public CollectionItemData find(@Nonnull CollectionId collectionId,
                                   @Nonnull CollectionItem elementId) {
        CollectionItemData data = createQuery(collectionId)
                .field(ITEM).equal(elementId).get();
        if (data == null) {
            return new CollectionItemData(collectionId, elementId);
        }
        else {
            return data;
        }
    }

    @Override
    @Nonnull
    public List<CollectionItem> list(@Nonnull CollectionId collectionId,
                                     int skip,
                                     int limit) {
        return datastore.getDB().getCollection("CollectionItemData")
                 .find(new BasicDBObject(COLLECTION_ID, collectionId.getId()),
                       new BasicDBObject(ITEM, 1))
                        // Requires a later version of mongo db - disable for now.
//                 .setCollation(Collation.builder().locale("en").numericOrdering(true).build())
                 .limit(limit)
                 .skip(skip)
                .toArray()
                .stream()
                .map(dbObject -> (String) dbObject.get(ITEM))
                .map(CollectionItem::get)
                .collect(toList());
    }


    private Query<CollectionItemData> createQuery(CollectionId collectionId) {
        return datastore.createQuery(CollectionItemData.class)
                        .field(COLLECTION_ID).equal(collectionId);
    }
}
