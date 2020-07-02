package edu.stanford.bmir.protege.web.server.form;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2017
 */
public class FormDataRepository_IT {

//    private ProjectId projectId = ProjectId.get(DISCRIMINATOR.randomUUID().toString());
//
//    private OWLEntity entity = MockingUtils.mockOWLClass();
//
//    private FormDataRepository repository;
//
//    private Morphia morphia;
//
//    private MongoClient mongoClient;
//
//    private Datastore datastore;
//
//    private CollectionId collectionId = CollectionId.get("The Collection {Some special \"chars\"}");
//
////    @Before
//    public void setUp() throws Exception {
//        morphia = MongoTestUtils.createMorphia();
//        mongoClient = MongoTestUtils.createMongoClient();
//        datastore = morphia.createDatastore(mongoClient, MongoTestUtils.getTestDbName());
//        repository = new FormDataRepository(datastore);
//    }
//
////    @Test
//    public void shouldStoreData() throws Exception {
//        Map<FormFieldId, FormDataValue> map = new HashMap<>();
//        map.put(FormFieldId.get("FirstName"), FormDataPrimitive.get("John"));
//        FormData formData = new FormData(null, map, formDescriptor);
//        repository.store(projectId,
//                         collectionId,
//                         FormId.get("MyForm"),
//                         entity,
//                         formData);
//    }
//
////    @Test
//    public void shouldRetriveData() throws Exception {
//        Map<FormFieldId, FormDataValue> map = new HashMap<>();
//        map.put(FormFieldId.get("FirstName"), FormDataPrimitive.get("John"));
//        map.put(FormFieldId.get("LastName"), FormDataPrimitive.get("Smith"));
//        map.put(FormFieldId.get("Age"), FormDataPrimitive.get(62));
//        map.put(FormFieldId.get("Tenure"), FormDataPrimitive.get(true));
//        Map<String, FormDataValue> val = new HashMap<>();
//        val.put("Street", FormDataPrimitive.get("1265 Welch Road"));
//        val.put("City", FormDataPrimitive.get("Stanford"));
//        val.put("State", FormDataPrimitive.get("CA"));
//        val.put("Zip", FormDataPrimitive.get(94304));
//        FormDataObject address = new FormDataObject(val);
//        map.put(FormFieldId.get("Address"), address);
//        map.put(FormFieldId.get("Projects"), new FormDataList(
//                Arrays.asList(FormDataPrimitive.get("Protégé Project"),
//                              FormDataPrimitive.get("Bioportal"))));
//        map.put(FormFieldId.get("Homepage"), FormDataPrimitive.get(IRI.create("http://www.stanford.edu/~johnsmith")));
//        FormData formData = new FormData(null, map, formDescriptor);
//        FormId formId = FormId.get("MyForm");
//        repository.store(projectId,
//                         collectionId,
//                         formId,
//                         entity,
//                         formData);
//        FormData fd = repository.get(projectId, collectionId, formId,  entity);
//        assertThat(fd, Matchers.is(formData));
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        datastore.getDB().dropDatabase();
//        mongoClient.close();
//    }
}
