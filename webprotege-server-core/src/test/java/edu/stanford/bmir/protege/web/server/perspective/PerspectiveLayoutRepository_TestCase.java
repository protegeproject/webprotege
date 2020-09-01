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

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class PerspectiveLayoutRepository_TestCase {

    private PerspectiveLayoutRepository repository;

    private MongoDatabase database;

    private MongoClient mongoClient;

    @Before
    public void setUp() throws Exception {
        mongoClient = MongoTestUtils.createMongoClient();
        database = mongoClient.getDatabase(MongoTestUtils.getTestDbName());
        var objectMapper = new ObjectMapperProvider().get();
        repository = new PerspectiveLayoutRepositoryImpl(database, objectMapper);
        repository.ensureIndexes();
    }

    private MongoCollection<Document> getCollection() {
        return database.getCollection(PerspectiveLayoutRepositoryImpl.PERSPECTIVE_LAYOUTS);
    }

    @Test
    public void shouldCreateIndexes() {
        var collection = getCollection();
        try (var cursor = collection.listIndexes().cursor()) {
            var index = cursor.tryNext();
            assertThat(index, not(nullValue()));
        }
    }

    @Test
    public void shouldSave() {
        var record = createTestRecord();
        repository.saveLayout(record);
        assertThat(getCollection().countDocuments(), Matchers.is(1L));
    }

    /**
     * @noinspection ConstantConditions
     */
    @Test
    public void shouldRetrieveSaved() {
        var record = createTestRecord();
        repository.saveLayout(record);
        var saved = repository.findLayout(record.getProjectId(), record.getUserId(), record.getPerspectiveId());
        assertThat(saved, equalTo(Optional.of(record)));
    }

    @Test
    public void shouldNotSaveDuplicates() {
        var record = createTestRecord();
        repository.saveLayout(record);
        repository.saveLayout(record);
        assertThat(getCollection().countDocuments(), Matchers.is(1L));
    }

    /** @noinspection ConstantConditions*/
    @Test
    public void shouldDropLayout() {
        var record = createTestRecord();
        repository.saveLayout(record);
        assertThat(getCollection().countDocuments(), Matchers.is(1L));
        repository.dropLayout(record.getProjectId(), record.getUserId(), record.getPerspectiveId());
        assertThat(getCollection().countDocuments(), Matchers.is(0L));
    }

    private static PerspectiveLayoutRecord createTestRecord() {
        return PerspectiveLayoutRecord.get(ProjectId.getNil(),
                                           UserId.getUserId("Matthew"),
                                           PerspectiveId.generate(),
                                           null);
    }


    @After
    public void tearDown() throws Exception {
        database.drop();
        mongoClient.close();
    }
}
