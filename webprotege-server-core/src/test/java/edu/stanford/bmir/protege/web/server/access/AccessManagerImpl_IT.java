package edu.stanford.bmir.protege.web.server.access;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.access.BuiltInRole;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Apr 2017
 */
@SuppressWarnings("unchecked" )
@RunWith(MockitoJUnitRunner.class)
public class AccessManagerImpl_IT {

    private static final String THE_USER_NAME = "The User";

    private static final String USER_NAME_FIELD = "userName";

    private static final String ASSIGNED_ROLES_FIELD = "assignedRoles";

    private static final String ROLE_CLOSURE_FIELD = "roleClosure";

    private static final String ACTION_CLOSURE_FIELD = "actionClosure";

    private AccessManagerImpl manager;

    private MongoClient mongoClient;

    private Subject subject;

    private ApplicationResource resource;

    private Set<RoleId> assignedRoles;

    private MongoDatabase database;

    private MongoCollection<Document> collection;

    @Mock
    private RoleOracle roleOracle;

    private Document storedDocument;

    private Document document1;

    private Document userQuery;

    @Before
    public void setUp() throws Exception {
        Morphia morphia = MongoTestUtils.createMorphia();
        mongoClient = MongoTestUtils.createMongoClient();
        Datastore datastore = morphia.createDatastore(mongoClient, MongoTestUtils.getTestDbName());
        manager = new AccessManagerImpl(RoleOracleImpl.get(),
                                        datastore);

        subject = Subject.forUser(THE_USER_NAME);
        resource = ApplicationResource.get();
        assignedRoles = Collections.singleton(BuiltInRole.CAN_COMMENT.getRoleId());
        database = mongoClient.getDatabase(MongoTestUtils.getTestDbName());
        collection = database.getCollection("RoleAssignments");
        manager.setAssignedRoles(
                subject,
                resource,
                assignedRoles
        );

        userQuery = new Document(USER_NAME_FIELD, THE_USER_NAME);
        storedDocument = collection.find(userQuery).first();
    }

    @Test
    public void shouldStoreAssignedRoles() {
        assertThat(storedDocument, is(notNullValue()));
        assertThat((List<String>) storedDocument.get(ASSIGNED_ROLES_FIELD), hasItems("CanComment"));
    }

    @Test
    public void shouldStoreRoleClosure() {
        assertThat(storedDocument, is(notNullValue()));
        // Just check it contains a parent role
        assertThat((List<String>) storedDocument.get(ROLE_CLOSURE_FIELD), hasItems("CanView"));
    }

    @Test
    public void shouldStoreActionClosure() {
        assertThat(storedDocument, is(notNullValue()));
        assertThat((List<String>) storedDocument.get(ACTION_CLOSURE_FIELD), hasItems("ViewProject"));
    }

    @Test
    public void shouldNotStoreDuplicate() {
        manager.setAssignedRoles(
                subject,
                resource,
                assignedRoles
        );
        assertThat(collection.count(), is(1L));
    }

    @Test
    public void shouldRebuildRoleClosure() {
        collection.updateOne(userQuery, new Document("$set", new Document("roleClosure", emptyList())));
        collection.updateOne(userQuery, new Document("$set", new Document("actionClosure", emptyList())));
        manager.rebuild();
        Document rebuiltDocument = collection.find().first();
        assertThat((List<String>) rebuiltDocument.get(ROLE_CLOSURE_FIELD), hasItems("CanView"));
    }

    @Test
    public void shouldRebuildActionClosure() {
        collection.updateOne(userQuery, new Document("$set", new Document("roleClosure", emptyList())));
        collection.updateOne(userQuery, new Document("$set", new Document("actionClosure", emptyList())));
        manager.rebuild();
        Document rebuiltDocument = collection.find().first();
        assertThat((List<String>) rebuiltDocument.get(ACTION_CLOSURE_FIELD), hasItems("ViewProject"));
    }

    @After
    public void tearDown() {
        database.drop();
        mongoClient.close();
    }
}
