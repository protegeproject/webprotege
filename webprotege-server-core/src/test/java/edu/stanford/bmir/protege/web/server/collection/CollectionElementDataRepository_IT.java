package edu.stanford.bmir.protege.web.server.collection;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItemData;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jul 2017
 */
public class CollectionElementDataRepository_IT {

    private CollectionItemDataRepository repository;

    private CollectionId collectionId = CollectionId.get("12345678-1234-1234-1234-123456789abc");

    private CollectionItem elementId = CollectionItem.get("The Element Name");

    private Datastore datastore;

    @Before
    public void setUp() {
        Morphia morphia = MongoTestUtils.createMorphia();
        MongoClient client = MongoTestUtils.createMongoClient();
        datastore = morphia.createDatastore(client, MongoTestUtils.getTestDbName());
        repository = new CollectionItemDataRepositoryImpl(datastore);
        repository.ensureIndexes();
    }

    @Test
    public void shouldSaveEmptyCollectionElementData() {
        repository.save(new CollectionItemData(collectionId, elementId));
        assertThat(datastore.getCount(CollectionItemData.class), is(1L));
    }

    @Test
    public void shouldUpdateCollectionElementData() {
//        Map<FormFieldId, FormDataValue> map = new HashMap<>();
//        map.put(FormFieldId.get("theElement"), FormDataPrimitive.get("theValue"));
//        FormData formData = new FormData(null, map, FormDescriptor.empty(FormId.generate()));
//        repository.save(new CollectionItemData(collectionId, elementId, formData));
//        Map<FormFieldId, FormDataValue> map2 = new HashMap<>();
//        map.put(FormFieldId.get("theElement"), FormDataPrimitive.get("theNewValue"));
//        FormData theNewformData = new FormData(null, map2, FormDescriptor.empty(FormId.generate()));
//        repository.save(new CollectionItemData(collectionId, elementId, theNewformData));
//        assertThat(datastore.getCount(CollectionItemData.class), is(1L));
    }

    @Test
    public void shouldSaveNonEmptyCollectionElementData() {
//        Map<FormFieldId, FormDataValue> map = new HashMap<>();
//        map.put(FormFieldId.get("theElement"), FormDataPrimitive.get("theValue"));
//        FormData formData = new FormData(null, map, FormDescriptor.empty(FormId.generate()));
//        repository.save(new CollectionItemData(collectionId, elementId, formData));
//        assertThat(datastore.getCount(CollectionItemData.class), is(1L));
    }

    @Test
    public void shouldFindByCollectionId() {
        CollectionItemData data = new CollectionItemData(collectionId, elementId);
        repository.save(data);
        assertThat(repository.find(collectionId), hasItem(data));
    }

    @Test
    public void shouldFindByCollectionIdAndElementId() {
//        Map<FormFieldId, FormDataValue> map = new HashMap<>();
//        map.put(FormFieldId.get("theElement"), FormDataPrimitive.get("theValue"));
//        FormData formData = new FormData(null, map, FormDescriptor.empty(FormId.generate()));
//        CollectionItemData data = new CollectionItemData(collectionId, elementId, formData);
//        repository.save(data);
//        assertThat(repository.find(collectionId, elementId), is(data));
    }

    @After
    public void tearDown() throws Exception {
        datastore.getDB().dropDatabase();
        datastore.getDB().getMongo().close();
    }
}
