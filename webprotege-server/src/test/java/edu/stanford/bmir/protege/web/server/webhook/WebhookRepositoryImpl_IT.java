package edu.stanford.bmir.protege.web.server.webhook;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhook;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType.COMMENT_POSTED;
import static edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType.PROJECT_CHANGED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 May 2017
 */
public class WebhookRepositoryImpl_IT {

    private static final String PAYLOAD_URL = "http://the.payload.url/path";

    private WebhookRepositoryImpl repository;

    private MongoClient client;

    private Datastore datastore;

    private ProjectId projectId;

    private List<ProjectWebhookEventType> subscribedToEvents;

    private ProjectWebhook webhook;

    @Before
    public void setUp() throws Exception {
        client = MongoTestUtils.createMongoClient();
        Morphia morphia = MongoTestUtils.createMorphia();
        datastore = morphia.createDatastore(client, MongoTestUtils.getTestDbName());
        repository = new WebhookRepositoryImpl(datastore);

        projectId = ProjectId.get(UUID.randomUUID().toString());
        subscribedToEvents = Collections.singletonList(PROJECT_CHANGED);
        webhook = new ProjectWebhook(projectId,
                                     PAYLOAD_URL,
                                     subscribedToEvents);

        repository.addProjectWebhooks(Collections.singletonList(webhook));
    }

    @Test
    public void shouldSaveWebhook() {
        assertThat(datastore.getCount(ProjectWebhook.class), is(1L));
    }

    @Test
    public void shouldFindWebhookByProjectId() {
        List<ProjectWebhook> webhooks = repository.getProjectWebhooks(projectId);
        assertThat(webhooks, hasItems(webhook));
    }

    @Test
    public void shouldFindWebhooksByProjectIdAndEventType() {
        List<ProjectWebhook> webhooks = repository.getProjectWebhooks(projectId, PROJECT_CHANGED);
        assertThat(webhooks, hasItems(webhook));
    }

    @Test
    public void shouldNotFindWebhooksByProjectIdAndDifferentEventType() {
        List<ProjectWebhook> webhooks = repository.getProjectWebhooks(projectId, COMMENT_POSTED);
        assertThat(webhooks.isEmpty(), is(true));
    }

    @Test
    public void shouldClearProjectWebhooks() {
        repository.clearProjectWebhooks(projectId);
        assertThat(datastore.getCount(ProjectWebhook.class), is(0L));
    }

    @After
    public void tearDown() throws Exception {
        datastore.getDB().dropDatabase();
        client.close();
    }
}
