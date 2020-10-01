package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableMap;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclarations;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Feb 2018
 */
public class PrefixDeclarationsStore_IT {

    public static final String COLLECTION_NAME = "PrefixDeclarations";
    
    private final ProjectId projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");

    private PrefixDeclarationsStore store;

    private PrefixDeclarations prefixDeclarations;

    private MongoClient client;

    private MongoDatabase database;

    @Before
    public void setUp() throws Exception {
        client = createMongoClient();
        database = client.getDatabase(MongoTestUtils.getTestDbName());
        var objectMapper = new ObjectMapperProvider().get();
        store = new PrefixDeclarationsStore(objectMapper, database);
        ImmutableMap.Builder<String, String> prefixesMap = ImmutableMap.builder();
        prefixesMap.put("a:", "http://ont.org/a/");
        prefixDeclarations = PrefixDeclarations.get(
                projectId,
                prefixesMap.build()
        );
    }

    @Test
    public void shouldSavePrefixes() {
        store.save(prefixDeclarations);
        assertThat(database.getCollection(COLLECTION_NAME).countDocuments(), is(1L));
    }

    @Test
    public void shouldNotCreateDuplicates() {
        store.save(prefixDeclarations);
        store.save(prefixDeclarations);
        assertThat(database.getCollection(COLLECTION_NAME).countDocuments(), is(1L));
    }

    @Test
    public void shouldRetrivePrefixes() {
        store.save(prefixDeclarations);
        PrefixDeclarations prefixes = store.find(projectId);
        assertThat(prefixes, is(prefixDeclarations));
    }

    @After
    public void tearDown() {
        database.drop();
        client.close();
    }
}
