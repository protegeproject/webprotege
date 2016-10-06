package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.ProjectIdFactory;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.issues.Status;
import edu.stanford.bmir.protege.web.shared.issues.ThreadId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Optional;

import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.getTestDbName;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 *
 * An integration test for the repo that stores entity discussion thread.  This test requires
 * a running version of MongoDB.
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class EntityDiscussionThreadRepository_IT {

    private MongoClient mongoClient;

    private EntityDiscussionThread thread;

    private EntityDiscussionThreadRepository repository;

    @Before
    public void setUp() throws Exception {
        Morphia morphia = MongoTestUtils.createMorphia();
        mongoClient = MongoTestUtils.createMongoClient();
        Datastore datastore = morphia.createDatastore(mongoClient, getTestDbName());
        repository = new EntityDiscussionThreadRepository(datastore);
        thread = new EntityDiscussionThread(ThreadId.create(),
                                            ProjectIdFactory.getFreshProjectId(),
                                            MockingUtils.mockOWLClass(),
                                            Status.OPEN,
                                            ImmutableList.of(new Comment(UserId.getUserId("John"),
                                                                         System.currentTimeMillis(),
                                                                         Optional.of(33L),
                                                                         "The body"))
        );
        repository.saveThread(thread);
    }

    @After
    public void tearDown() throws Exception {
        mongoClient.dropDatabase(getTestDbName());
        mongoClient.close();
    }


    @Test
    public void shouldSaveItem() {
        long count = getCollection().count(new Document("_id", thread.getId().getId()));
        assertThat(count, is(1L));
    }

    @Test
    public void shouldFindThread() {
        Optional<EntityDiscussionThread> foundThread = repository.getThread(thread.getId());
        assertThat(foundThread, is(Optional.of(thread)));
    }

    @Test
    public void shouldNotSaveItemTwice() {
        repository.saveThread(thread);
        long count = getCollection().count(new Document("_id", thread.getId().getId()));
        assertThat(count, is(1L));
    }

    @Test
    public void shouldAddComment() {
        repository.saveThread(thread);
        long createdAt = System.currentTimeMillis();
        Comment theComment = new Comment(UserId.getUserId("Matthew"),
                                      createdAt,
                                      Optional.empty(),
                                      "The body");
        repository.addCommentToThread(thread.getId(),
                                      theComment);
        Optional<EntityDiscussionThread> foundThread = repository.getThread(thread.getId());
        assertThat(foundThread.get().getComments(), hasItem(theComment));
    }

    @Test
    public void shouldCloseThread() {
        repository.setThreadStatus(thread.getId(), Status.CLOSED);
        Optional<EntityDiscussionThread> readThread = repository.getThread(thread.getId());
        assertThat(readThread.get().getStatus(), is(Status.CLOSED));
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase(getTestDbName())
                          .getCollection("EntityDiscussionThreads");
    }

}
