package edu.stanford.bmir.protege.web.server.tag;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.createMongoClient;
import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.createMorphia;
import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.getTestDbName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2018
 */
public class EntityTagsRepository_TestCase {

    private static final String UUID = "12345678-1234-1234-1234-123456789abc";

    private EntityTagsRepository repository;

    private MongoClient mongoClient;

    private OWLEntity entity;

    private TagId tagIdA, tagIdB;

    private ProjectId projectId;
    private EntityTags entityTags;

    @Before
    public void setUp() throws Exception {
        Morphia morphia = createMorphia();
        mongoClient = createMongoClient();
        Datastore datastore = morphia.createDatastore(mongoClient, getTestDbName());
        repository = new EntityTagsRepository(datastore);
        repository.ensureIndexes();
        entity = new OWLClassImpl(IRI.create("http://stuff.com/entities/A"));
        tagIdA = TagId.getId("12345678-1234-1234-1234-123456789abc");
        tagIdB = TagId.getId("12345678-5678-5678-5678-123456789abc");
        projectId = ProjectId.get(UUID);
        List<TagId> tags = Arrays.asList(tagIdA, tagIdB);
        entityTags = new EntityTags(projectId,
                                    entity,
                                    tags);
    }

    @Test
    public void shouldSaveEntityTags() {
        repository.save(entityTags);
        assertThat(repository.findByEntity(projectId, entity), hasItem(entityTags));
    }

    @Test
    public void shouldNotSaveDuplicateEntityTags() {
        repository.save(entityTags);
        repository.save(entityTags);
        List<EntityTags> retrievedTags = repository.findByEntity(projectId, entity);
        assertThat(retrievedTags, hasItem(entityTags));
        assertThat(retrievedTags.size(), is(1));
    }

    @Test
    public void shouldFindByTag() {
        repository.save(entityTags);
        assertThat(repository.findByTagId(tagIdA), hasItem(entityTags));
    }

    @Test
    public void shouldAddTag() {
        repository.save(entityTags);
        TagId theTagId = TagId.getId("12345678-abcd-abcd-abcd-123456789abc");
        repository.addTag(projectId, entity, theTagId);
        assertThat(repository.findByTagId(theTagId), hasSize(1));
    }

    @Test
    public void shouldRemoveTag() {
        repository.save(entityTags);
        repository.removeTag(projectId, entity, tagIdA);
        assertThat(repository.findByTagId(tagIdA), is(empty()));
        assertThat(repository.findByTagId(tagIdB), hasSize(1));
    }

    @After
    public void tearDown() throws Exception {
        mongoClient.dropDatabase(getTestDbName());
        mongoClient.close();
    }
}
