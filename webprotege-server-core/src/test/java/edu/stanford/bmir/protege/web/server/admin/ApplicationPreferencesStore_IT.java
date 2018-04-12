package edu.stanford.bmir.protege.web.server.admin;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.app.ApplicationPreferences;
import edu.stanford.bmir.protege.web.server.app.ApplicationPreferencesStore;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
public class ApplicationPreferencesStore_IT {

    private final ApplicationPreferences applicationPreferences = new ApplicationPreferences(
            "TheApplicationName" ,
            "TheSystemNotificationEmailAddress" ,
            new ApplicationLocation(
                    "TheScheme" ,
                    "TheHost" ,
                    "ThePath" ,
                    33
            ),
            44L
    );

    private ApplicationPreferencesStore manager;

    private Morphia morphia;

    private MongoClient mongoClient;

    private Datastore datastore;

    @Before
    public void setUp() throws Exception {
        mongoClient = MongoTestUtils.createMongoClient();
        morphia = MongoTestUtils.createMorphia();
        datastore = morphia.createDatastore(mongoClient, MongoTestUtils.getTestDbName());
        manager = new ApplicationPreferencesStore(datastore);
    }

    @After
    public void tearDown() {
        mongoClient.dropDatabase(MongoTestUtils.getTestDbName());
        mongoClient.close();
    }

    @Test
    public void shouldSaveSettings() {
        manager.setApplicationPreferences(applicationPreferences);
        assertThat(datastore.getCount(ApplicationPreferences.class), is(1L));
    }

    @Test
    public void shouldSaveSingleSettings() {
        manager.setApplicationPreferences(applicationPreferences);
        manager.setApplicationPreferences(applicationPreferences);
        assertThat(datastore.getCount(ApplicationPreferences.class), is(1L));
    }

    @Test
    public void shouldGetSavedSettings() {
        manager.setApplicationPreferences(applicationPreferences);
        ApplicationPreferences settings = manager.getApplicationPreferences();
        assertThat(settings, is(applicationPreferences));
    }

}
