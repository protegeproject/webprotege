package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableMap;
import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectPrefixes;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Collections;

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
public class ProjectPrefixesStore_IT {

    private final ProjectId projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");

    private ProjectPrefixesStore store;

    private Datastore datastore;

    private ProjectPrefixes projectPrefixes;

    @Before
    public void setUp() throws Exception {
        Morphia morphia = createMorphia();
        MongoClient client = createMongoClient();
        datastore = morphia.createDatastore(client, getTestDbName());
        store = new ProjectPrefixesStore(datastore);
        ImmutableMap.Builder<String, String> prefixesMap = ImmutableMap.builder();
        prefixesMap.put("a:", "http://ont.org/a/");
        projectPrefixes = ProjectPrefixes.get(
                projectId,
                prefixesMap.build()
        );
    }

    @Test
    public void shouldSavePrefixes() {
        store.save(projectPrefixes);
        assertThat(datastore.getCount(ProjectPrefixes.class), is(1L));
    }

    @Test
    public void shouldNotCreateDuplicates() {
        store.save(projectPrefixes);
        store.save(projectPrefixes);
        assertThat(datastore.getCount(ProjectPrefixes.class), is(1L));
    }

    @Test
    public void shouldRetrivePrefixes() {
        store.save(projectPrefixes);
        ProjectPrefixes prefixes = store.find(projectId);
        assertThat(prefixes, is(projectPrefixes));
    }

    @After
    public void tearDown() {
        datastore.getDB().dropDatabase();
    }
}
