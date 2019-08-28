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
import java.util.List;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2018
 */
public class EntityTagsRepositoryImpl_TestCase {

    private static final String UUID = "12345678-1234-1234-1234-123456789abc";

    private EntityTagsRepositoryImpl repository;

    private MongoClient mongoClient;

    private OWLEntity entity;

    private TagId tagIdA, tagIdB;

    private ProjectId projectId;
    private EntityTags entityTags;

    @Before
    public void setUp() throws Exception {
        projectId = ProjectId.get(UUID);
        Morphia morphia = createMorphia();
        mongoClient = createMongoClient();
        Datastore datastore = morphia.createDatastore(mongoClient, getTestDbName());
        repository = new EntityTagsRepositoryImpl(projectId, datastore);
        repository.ensureIndexes();
        entity = new OWLClassImpl(IRI.create("http://stuff.com/entities/A"));
        tagIdA = TagId.getId("12345678-1234-1234-1234-123456789abc");
        tagIdB = TagId.getId("12345678-5678-5678-5678-123456789abc");
        List<TagId> tags = Arrays.asList(tagIdA, tagIdB);
        entityTags = new EntityTags(projectId,
                                    entity,
                                    tags);
    }

    @Test
    public void shouldSaveEntityTags() {
        repository.save(entityTags);
        assertThat(repository.findByEntity(entity), is(Optional.of(entityTags)));
    }

    @Test
    public void shouldNotSaveDuplicateEntityTags() {
        repository.save(entityTags);
        repository.save(entityTags);
        Optional<EntityTags> retrievedTags = repository.findByEntity(entity);
        assertThat(retrievedTags, is(Optional.of(entityTags)));
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
        repository.addTag(entity, theTagId);
        assertThat(repository.findByTagId(theTagId).size(), is(1));
    }

    @Test
    public void shouldRemoveTag() {
        repository.save(entityTags);
        repository.removeTag(entity, tagIdA);
        assertThat(repository.findByTagId(tagIdA).size(), is(0));
        assertThat(repository.findByTagId(tagIdB).size(), is(1));
    }

    @After
    public void tearDown() throws Exception {
        mongoClient.dropDatabase(getTestDbName());
        mongoClient.close();
    }
}
