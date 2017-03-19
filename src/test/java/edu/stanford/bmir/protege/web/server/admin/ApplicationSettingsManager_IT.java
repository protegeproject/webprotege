package edu.stanford.bmir.protege.web.server.admin;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
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
public class ApplicationSettingsManager_IT {

    private final ApplicationSettings applicationSettings = new ApplicationSettings(
            "TheApplicationName" ,
            "TheApplicationLogoUrl" ,
            "TheAdminEmailAddress" ,
            new ApplicationLocation(
                    "TheScheme" ,
                    "TheHost" ,
                    "ThePath" ,
                    33
            )
    );

    private ApplicationSettingsManager manager;

    private Morphia morphia;

    private MongoClient mongoClient;

    private Datastore datastore;

    @Before
    public void setUp() throws Exception {
        mongoClient = MongoTestUtils.createMongoClient();
        morphia = MongoTestUtils.createMorphia();
        datastore = morphia.createDatastore(mongoClient, MongoTestUtils.getTestDbName());
        manager = new ApplicationSettingsManager(datastore);
    }

    @After
    public void tearDown() {
        mongoClient.dropDatabase(MongoTestUtils.getTestDbName());
    }

    @Test
    public void shouldSaveSettings() {
        manager.setApplicationSettings(applicationSettings);
        assertThat(datastore.getCount(ApplicationSettings.class), is(1L));
    }

    @Test
    public void shouldSaveSingleSettings() {
        manager.setApplicationSettings(applicationSettings);
        manager.setApplicationSettings(applicationSettings);
        assertThat(datastore.getCount(ApplicationSettings.class), is(1L));
    }

    @Test
    public void shouldGetSavedSettings() {
        manager.setApplicationSettings(applicationSettings);
        ApplicationSettings settings = manager.getApplicationSettings();
        assertThat(settings, is(applicationSettings));
    }

}
