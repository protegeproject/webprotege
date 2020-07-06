package edu.stanford.bmir.protege.web.server.viz;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.viz.AnyEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.AnySubClassOfEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraphSettings;
import edu.stanford.bmir.protege.web.shared.viz.ProjectUserEntityGraphSettings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
public class EntityGraphSettingsRepositoryImpl_IT {

    private static final double RANK_SEPARATION = 2.0;

    private EntityGraphSettingsRepositoryImpl repository;

    private MongoClient client;

    private MongoDatabase database;

    private ProjectId projectId;

    private UserId userId;

    @Before
    public void setUp() {
        userId = UserId.getUserId("JohnDoe");
        projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");
        client = MongoTestUtils.createMongoClient();
        database = client.getDatabase(MongoTestUtils.getTestDbName());
        var objectMapper = new ObjectMapperProvider().get();
        repository = new EntityGraphSettingsRepositoryImpl(database,
                                                           objectMapper);
    }

    @Test
    public void shouldSaveSettings() {
        var settings = ProjectUserEntityGraphSettings.getDefault(projectId, userId);
        repository.saveSettings(settings);
        var collection = database.getCollection(EntityGraphSettingsRepositoryImpl.getCollectionName());
        assertThat(collection.countDocuments(), is(1L));
        ProjectUserEntityGraphSettings savedSettings = repository.getSettingsForUserOrProjectDefault(projectId, userId);
        assertThat(savedSettings, is(settings));
    }

    @Test
    public void shouldNotDuplicateSaveSettings() {
        var settings = ProjectUserEntityGraphSettings.getDefault(projectId, userId);
        repository.saveSettings(settings);
        repository.saveSettings(settings);
        var collection = database.getCollection(EntityGraphSettingsRepositoryImpl.getCollectionName());
        assertThat(collection.countDocuments(), is(1L));
        ProjectUserEntityGraphSettings savedSettings = repository.getSettingsForUserOrProjectDefault(projectId, userId);
        assertThat(savedSettings, is(settings));
    }

    @Test
    public void shouldSaveProjectDefaultAndUserSettings() {
        var userSettings = ProjectUserEntityGraphSettings.getDefault(projectId, userId);
        repository.saveSettings(userSettings);

        var projectSettings = ProjectUserEntityGraphSettings.getDefault(projectId, null);
        repository.saveSettings(projectSettings);



        var collection = database.getCollection(EntityGraphSettingsRepositoryImpl.getCollectionName());
        assertThat(collection.countDocuments(), is(2L));

        var savedUserSettings = repository.getSettingsForUserOrProjectDefault(projectId, userId);
        assertThat(savedUserSettings, is(userSettings));

        var savedProjectSettings = repository.getSettingsForUserOrProjectDefault(projectId, null);
        assertThat(savedProjectSettings, is(projectSettings));
    }

    @Test
    public void shouldSaveSettingsWithoutUserId() {
        var settings = ProjectUserEntityGraphSettings.getDefault(projectId, null);
        repository.saveSettings(settings);
        var collection = database.getCollection(EntityGraphSettingsRepositoryImpl.getCollectionName());
        assertThat(collection.countDocuments(), is(1L));
        ProjectUserEntityGraphSettings savedSettings = repository.getSettingsForUserOrProjectDefault(projectId, null);
        assertThat(savedSettings, is(settings));
    }

    @After
    public void tearDown() {
        client.dropDatabase(MongoTestUtils.getTestDbName());
        client.close();
    }
}
