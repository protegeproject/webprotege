
package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableList;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.ProjectIdFactory;
import edu.stanford.bmir.protege.web.server.issues.EntityDiscussionThreadRepository;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Optional;

import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.getTestDbName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ProjectAccessManagerImpl_IT {

    public static final long TIMESTAMP_A = 33L;

    public static final long TIMESTAMP_B = 44L;

    private ProjectAccessManagerImpl manager;

    private final ProjectId projectId = ProjectIdFactory.getFreshProjectId();

    private final UserId userId = UserId.getUserId("Jane Doe");

    private final UserId otherUserId = UserId.getUserId("John Smith");

    private MongoClient mongoClient;

    private MongoDatabase database;

    @Before
    public void setUp() throws Exception {
        mongoClient = MongoTestUtils.createMongoClient();
        database = mongoClient.getDatabase(getTestDbName());
        manager = new ProjectAccessManagerImpl(database);
    }

    @After
    public void tearDown() throws Exception {
        mongoClient.dropDatabase(getTestDbName());
        mongoClient.close();
    }

    @Test
    public void shouldSaveItem() {
        manager.logProjectAccess(projectId, userId, TIMESTAMP_A);
        assertThat(getCollection().count(), is(1L));
    }

    @Test
    public void shouldNotSaveDuplicateProjectUserItems() {
        manager.logProjectAccess(projectId, userId, TIMESTAMP_A);
        manager.logProjectAccess(projectId, userId, TIMESTAMP_B);
        assertThat(getCollection().count(), is(1L));
    }

    @Test
    public void shouldSaveSameProjectDifferentUsers() {
        manager.logProjectAccess(projectId, userId, TIMESTAMP_A);
        manager.logProjectAccess(projectId, otherUserId, TIMESTAMP_B);
        assertThat(getCollection().count(), is(2L));
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase(getTestDbName())
                          .getCollection("ProjectAccessRecords");
    }


}
