package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableMap;
import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclarations;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.createMongoClient;
import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.createMorphia;
import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.getTestDbName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Feb 2018
 */
public class PrefixDeclarationsStore_IT {

    private final ProjectId projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");

    private PrefixDeclarationsStore store;

    private Datastore datastore;

    private PrefixDeclarations prefixDeclarations;

    @Before
    public void setUp() throws Exception {
        Morphia morphia = createMorphia();
        MongoClient client = createMongoClient();
        datastore = morphia.createDatastore(client, getTestDbName());
        store = new PrefixDeclarationsStore(datastore);
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
        assertThat(datastore.getCount(PrefixDeclarations.class), is(1L));
    }

    @Test
    public void shouldNotCreateDuplicates() {
        store.save(prefixDeclarations);
        store.save(prefixDeclarations);
        assertThat(datastore.getCount(PrefixDeclarations.class), is(1L));
    }

    @Test
    public void shouldRetrivePrefixes() {
        store.save(prefixDeclarations);
        PrefixDeclarations prefixes = store.find(projectId);
        assertThat(prefixes, is(prefixDeclarations));
    }

    @After
    public void tearDown() {
        datastore.getDB().dropDatabase();
        datastore.getDB().getMongo().close();
    }
}
