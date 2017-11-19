package edu.stanford.bmir.protege.web.server.webhook;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.webhook.SlackWebhook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
public class SlackWebhookRepository_IT {

    private static final String PAYLOAD_URL_A = "payloadurlA";

    private static final String PAYLOAD_URL_B = "payloadurlB";

    private SlackWebhookRepositoryImpl repository;

    private MongoClient mongoClient;

    private Datastore datastore;

    private SlackWebhook slackWebhookA, slackWebhookB;

    private ProjectId projectId = ProjectId.get("12345678-1234-1234-1234-123456789accdef");

    @Before
    public void setUp() {
        mongoClient = MongoTestUtils.createMongoClient();
        Morphia morphia = MongoTestUtils.createMorphia();
        datastore = morphia.createDatastore(mongoClient, MongoTestUtils.getTestDbName());
        repository = new SlackWebhookRepositoryImpl(datastore);
        repository.ensureIndexes();
        slackWebhookA = new SlackWebhook(projectId, PAYLOAD_URL_A);
        slackWebhookB = new SlackWebhook(projectId, PAYLOAD_URL_B);
    }

    @Test
    public void shouldSaveWebhook() {
        repository.addWebhooks(singletonList(slackWebhookA));
        assertThat(datastore.getCount(SlackWebhook.class), is(1L));
    }

    @Test
    public void shouldSaveMultipleWebhooks() {
        repository.addWebhooks(asList(slackWebhookA, slackWebhookB));
        assertThat(datastore.getCount(SlackWebhook.class), is(2L));
    }

    @Test
    public void shouldNotSaveDuplicates() {
        repository.addWebhooks(Collections.singletonList(slackWebhookA));
        repository.addWebhooks(Collections.singletonList(slackWebhookA));
        assertThat(datastore.getCount(SlackWebhook.class), is(1L));
    }

    @Test
    public void shouldFindWebhookByProjectId() {
        repository.addWebhooks(Collections.singletonList(slackWebhookA));
        List<SlackWebhook> webhooks = repository.getWebhooks(projectId);
        assertThat(webhooks, is(singletonList(slackWebhookA)));
    }

    @Test
    public void shouldClearWebhook() {
        repository.addWebhooks(Collections.singletonList(slackWebhookA));
        repository.clearWebhooks(projectId);
        assertThat(datastore.getCount(SlackWebhook.class), is(0L));
    }

    @After
    public void tearDown() throws Exception {
        datastore.getDB().dropDatabase();
        mongoClient.close();
    }
}
