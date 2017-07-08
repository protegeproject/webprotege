package edu.stanford.bmir.protege.web.server.form;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataObject;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2017
 */
public class FormDataRepository_IT {

    private ProjectId projectId = ProjectId.get(UUID.randomUUID().toString());

    private OWLEntity entity = MockingUtils.mockOWLClass();

    private FormDataRepository repository;

    private Morphia morphia;

    private MongoClient mongoClient;

    private Datastore datastore;

    @Before
    public void setUp() throws Exception {
        morphia = MongoTestUtils.createMorphia();
        mongoClient = MongoTestUtils.createMongoClient();
        datastore = morphia.createDatastore(mongoClient, MongoTestUtils.getTestDbName());
        repository = new FormDataRepository(datastore);
    }

    @Test
    public void shouldStoreData() throws Exception {
        Map<FormElementId, FormDataValue> map = new HashMap<>();
        map.put(FormElementId.get("FirstName"), FormDataPrimitive.get("John"));
        FormData formData = new FormData(map);
        repository.store(projectId,
                         new FormId("MyForm"),
                         entity,
                         formData);
    }

    @Test
    public void shouldRetriveData() throws Exception {
        Map<FormElementId, FormDataValue> map = new HashMap<>();
        map.put(FormElementId.get("FirstName"), FormDataPrimitive.get("John"));
        Map<String, FormDataValue> val = new HashMap<>();
        val.put("Street", FormDataPrimitive.get("1788 Oak Creek Drive"));
        FormDataObject address = new FormDataObject(val);
        map.put(FormElementId.get("Address"), address);
        map.put(FormElementId.get("TheList"), new FormDataList(Arrays.asList(FormDataPrimitive.get(1), FormDataPrimitive.get(2))));
        map.put(FormElementId.get("iri"), FormDataPrimitive.get(IRI.create("http://stuff.com")));
        FormData formData = new FormData(map);
        FormId formId = new FormId("MyForm");
        repository.store(projectId,
                         formId,
                         entity,
                         formData);
        FormData fd = repository.get(projectId, formId,  entity);
        assertThat(fd, Matchers.is(formData));
    }

    @After
    public void tearDown() throws Exception {
        mongoClient.close();
//        datastore.getDB().dropDatabase();
    }
}
