package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class PerspectiveDescriptorRepository_TestCase {

    private PerspectiveDescriptorRepository repository;

    private MongoDatabase database;

    private MongoClient mongoClient;

    @Before
    public void setUp() throws Exception {
        mongoClient = MongoTestUtils.createMongoClient();
        database = mongoClient.getDatabase(MongoTestUtils.getTestDbName());
        var objectMapper = new ObjectMapperProvider().get();
        repository = new PerspectiveDescriptorRepositoryImpl(database,
                                                             objectMapper);
        repository.ensureIndexes();
    }

    @Test
    public void shouldCreateIndexes() {
        var collection = getCollection();
        try(var cursor = collection.listIndexes().cursor()) {
            var index = cursor.tryNext();
            assertThat(index, not(nullValue()));
        }
    }

    private MongoCollection<Document> getCollection() {
        return database.getCollection(PerspectiveDescriptorRepositoryImpl.PERSPECTIVE_DESCRIPTORS);
    }

    @Test
    public void shouldSave() {
        var record = createTestRecord();
        repository.saveDescriptors(ImmutableList.of(record));
        assertThat(getCollection().countDocuments(), Matchers.is(1L));
    }

    @Test
    public void shouldRetrieveSaved() {
        var record = createTestRecord();
        repository.saveDescriptors(ImmutableList.of(record));
        var saved = repository.findDescriptors(record.getProjectId(),
                                   record.getUserId());
        assertThat(saved, hasItems(record));
    }

    @Test
    public void shouldNotSaveDuplicates() {
        var record = createTestRecord();
        repository.saveDescriptors(ImmutableList.of(record));
        repository.saveDescriptors(ImmutableList.of(record));
        assertThat(getCollection().countDocuments(), Matchers.is(1L));
    }

    private static PerspectiveDescriptorsRecord createTestRecord() {
        return PerspectiveDescriptorsRecord.get(ProjectId.getNil(), UserId.getUserId("Matthew"),
                                                PerspectiveId.generate(),
                                                LanguageMap.of("en", "Hello"),
                                                true
            );
    }

    @After
    public void tearDown() throws Exception {
        database.drop();
        mongoClient.close();
    }
}
