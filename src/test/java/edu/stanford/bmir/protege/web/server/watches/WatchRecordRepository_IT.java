package edu.stanford.bmir.protege.web.server.watches;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.WatchType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
public class WatchRecordRepository_IT {

    private WatchRecordRepositoryImpl repository;

    private Datastore datastore;

    private MongoClient mongoClient;

    private UserId userId = UserId.getUserId("The User");

    private OWLEntity entity = new OWLClassImpl(IRI.create("http://the.ontology/ClsA"));

    private ProjectId projectId = ProjectId.get(UUID.randomUUID().toString());

    @Before
    public void setUp() throws Exception {
        Morphia morphia = MongoTestUtils.createMorphia();
        mongoClient = MongoTestUtils.createMongoClient();
        datastore = morphia.createDatastore(mongoClient, MongoTestUtils.getTestDbName());
        repository = new WatchRecordRepositoryImpl(datastore);
        repository.ensureIndexes();
    }

    @Test
    public void shouldSaveWatch() {
        repository.saveWatchRecord(new WatchRecord(projectId, userId, entity, WatchType.ENTITY));
        assertThat(datastore.getCount(WatchRecord.class), is(1L));
    }

    @Test
    public void shouldNotDuplicateWatch() {
        repository.saveWatchRecord(new WatchRecord(projectId, userId, entity, WatchType.ENTITY));
        repository.saveWatchRecord(new WatchRecord(projectId, userId, entity, WatchType.ENTITY));
        assertThat(datastore.getCount(WatchRecord.class), is(1L));
    }

    @Test
    public void shouldReplaceWatchWithDifferentType() {
        repository.saveWatchRecord(new WatchRecord(projectId, userId, entity, WatchType.ENTITY));
        repository.saveWatchRecord(new WatchRecord(projectId, userId, entity, WatchType.BRANCH));
        assertThat(datastore.getCount(WatchRecord.class), is(1L));
        List<WatchRecord> watches = repository.findWatchRecords(projectId, userId, singleton(entity));
        assertThat(watches.size(), is(1));
        assertThat(watches.iterator().next().getType(), is(WatchType.BRANCH));
    }

    @Test
    public void shouldFindWatchByEntity() {
        WatchRecord watchRecord = new WatchRecord(projectId, userId, entity, WatchType.ENTITY);
        repository.saveWatchRecord(watchRecord);
        assertThat(repository.findWatchRecords(projectId, singleton(entity)), hasItem(watchRecord));
    }

    @Test
    public void shouldFindWatchByUserIdAndEntity() {
        WatchRecord watchRecord = new WatchRecord(projectId, userId, entity, WatchType.ENTITY);
        repository.saveWatchRecord(watchRecord);
        assertThat(repository.findWatchRecords(projectId, userId, singleton(entity)), hasItem(watchRecord));
    }

    @Test
    public void shouldDeleteWatchRecord() {
        WatchRecord watchRecord = new WatchRecord(projectId, userId, entity, WatchType.ENTITY);
        repository.saveWatchRecord(watchRecord);
        assertThat(datastore.getCount(WatchRecord.class), is(1L));
        repository.deleteWatchRecord(watchRecord);
        assertThat(datastore.getCount(WatchRecord.class), is(0L));
    }

    @After
    public void tearDown() throws Exception {
        datastore.getDB().dropDatabase();
        mongoClient.close();
    }
}
