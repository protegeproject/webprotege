package edu.stanford.bmir.protege.web.server.collection;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementData;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
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

    private CollectionElementDataRepository repository;

    private CollectionId collectionId = CollectionId.get("12345678-1234-1234-1234-123456789abc");

    private CollectionElementId elementId = CollectionElementId.get("The Element Name");

    private Datastore datastore;

    @Before
    public void setUp() {
        Morphia morphia = MongoTestUtils.createMorphia();
        MongoClient client = MongoTestUtils.createMongoClient();
        datastore = morphia.createDatastore(client, MongoTestUtils.getTestDbName());
        repository = new CollectionElementDataRepository(datastore);
        repository.ensureIndexes();
    }

    @Test
    public void shouldSaveEmptyCollectionElementData() {
        repository.save(new CollectionElementData(collectionId, elementId));
        assertThat(datastore.getCount(CollectionElementData.class), is(1L));
    }

    @Test
    public void shouldUpdateCollectionElementData() {
        Map<FormElementId, FormDataValue> map = new HashMap<>();
        map.put(FormElementId.get("theElement"), FormDataPrimitive.get("theValue"));
        FormData formData = new FormData(map);
        repository.save(new CollectionElementData(collectionId, elementId, formData));
        Map<FormElementId, FormDataValue> map2 = new HashMap<>();
        map.put(FormElementId.get("theElement"), FormDataPrimitive.get("theNewValue"));
        FormData theNewformData = new FormData(map2);
        repository.save(new CollectionElementData(collectionId, elementId, theNewformData));
        assertThat(datastore.getCount(CollectionElementData.class), is(1L));
    }

    @Test
    public void shouldSaveNonEmptyCollectionElementData() {
        Map<FormElementId, FormDataValue> map = new HashMap<>();
        map.put(FormElementId.get("theElement"), FormDataPrimitive.get("theValue"));
        FormData formData = new FormData(map);
        repository.save(new CollectionElementData(collectionId, elementId, formData));
        assertThat(datastore.getCount(CollectionElementData.class), is(1L));
    }

    @Test
    public void shouldFindByCollectionId() {
        CollectionElementData data = new CollectionElementData(collectionId, elementId);
        repository.save(data);
        assertThat(repository.find(collectionId), hasItem(data));
    }

    @Test
    public void shouldFindByCollectionIdAndElementId() {
        Map<FormElementId, FormDataValue> map = new HashMap<>();
        map.put(FormElementId.get("theElement"), FormDataPrimitive.get("theValue"));
        FormData formData = new FormData(map);
        CollectionElementData data = new CollectionElementData(collectionId, elementId, formData);
        repository.save(data);
        assertThat(repository.find(collectionId, elementId), is(data));
    }

    @After
    public void tearDown() throws Exception {
        datastore.getDB().dropDatabase();
    }
}
