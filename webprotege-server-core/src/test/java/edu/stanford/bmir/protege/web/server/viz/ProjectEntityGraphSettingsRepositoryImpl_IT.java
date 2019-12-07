package edu.stanford.bmir.protege.web.server.viz;

import com.google.common.collect.ImmutableList;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.viz.AnyEdgeCriteria;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
public class ProjectEntityGraphSettingsRepositoryImpl_IT {

    private ProjectEntityGraphSettingsRepositoryImpl repository;

    private MongoClient client;

    private MongoDatabase database;

    private ProjectId projectId;

    @Before
    public void setUp() {
        projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");
        client = MongoTestUtils.createMongoClient();
        database = client.getDatabase(MongoTestUtils.getTestDbName());
        var objectMapper = new ObjectMapperProvider().get();
        repository = new ProjectEntityGraphSettingsRepositoryImpl(database,
                                                                  objectMapper);
    }

    @Test
    public void shouldSaveSettings() {
        var settings = ProjectEntityGraphSettings.get(projectId,
                                                      ImmutableList.of(
                                                              AnyEdgeCriteria.get()
                                                      ));
        repository.saveSettings(settings);
        var collection = database.getCollection(ProjectEntityGraphSettingsRepositoryImpl.getCollectionName());
        assertThat(collection.countDocuments(), is(1L));
        ProjectEntityGraphSettings savedSettings = repository.getSettings(projectId);
        assertThat(savedSettings, is(settings));
    }

    @Test
    public void shouldDuplicateSaveSettings() {
        var settings = ProjectEntityGraphSettings.get(projectId,
                                                      ImmutableList.of(
                                                              AnyEdgeCriteria.get()
                                                      ));
        repository.saveSettings(settings);
        repository.saveSettings(settings);
        var collection = database.getCollection(ProjectEntityGraphSettingsRepositoryImpl.getCollectionName());
        assertThat(collection.countDocuments(), is(1L));
        ProjectEntityGraphSettings savedSettings = repository.getSettings(projectId);
        assertThat(savedSettings, is(settings));
    }

    @After
    public void tearDown() {
        client.dropDatabase(MongoTestUtils.getTestDbName());
        client.close();
    }
}
