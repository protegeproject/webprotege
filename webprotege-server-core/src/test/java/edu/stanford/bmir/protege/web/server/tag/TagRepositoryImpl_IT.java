package edu.stanford.bmir.protege.web.server.tag;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.createMongoClient;
import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.createMorphia;
import static edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils.getTestDbName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
public class TagRepositoryImpl_IT {

    private static final String THE_TAG_LABEL = "The tag label";

    private static final String THE_TAG_DESCRIPTION = "The tag description";

    private static final Color COLOR = Color.getHex("#ffffff");

    private static final Color BG_COLOR = Color.getHex("#f0f0f0");


    private TagRepositoryImpl repository;

    private MongoClient client;

    private Tag tag;

    private TagId tagId;

    private ProjectId projectId;

    @Before
    public void setUp() throws Exception {
        client = createMongoClient();
        Morphia morphia = createMorphia();
        Datastore datastore = morphia.createDatastore(client, getTestDbName());
        repository = new TagRepositoryImpl(datastore);
        repository.ensureIndexes();

        tagId = TagId.getId("12345678-1234-1234-1234-123456789abc");
        projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");
        tag = new Tag(tagId,
                      projectId,
                      THE_TAG_LABEL,
                      THE_TAG_DESCRIPTION,
                      COLOR,
                      BG_COLOR);


        repository.saveTag(tag);

    }

    @After
    public void tearDown() {
        client.dropDatabase(getTestDbName());
        client.close();
    }

    /**
     * A util method that gets the tags collection size by querying the database using the
     * low level Java driver.
     */
    private long getTagsCollectionSize() {
        return client.getDatabase(getTestDbName()).getCollection("Tags").count();
    }

    @Test
    public void shouldSaveTag() {
        long count = getTagsCollectionSize();
        assertThat(count, is(1L));
    }

    @Test
    public void shouldFindTagByTagId() {
        Optional<Tag> theFoundTag = repository.findTagByTagId(tagId);
        assertThat(theFoundTag, is(Optional.of(tag)));
    }

    @Test
    public void shouldFindTagsByProjectId() {
        List<Tag> theTags = repository.findTagsByProjectId(projectId);
        assertThat(theTags, hasItems(tag));
    }

    @Test
    public void shouldNotDuplicateTags() {
        repository.saveTag(tag);
        assertThat(getTagsCollectionSize(), is(1L));
    }

    @Test
    public void shouldUpdateTag() {
        Tag updatedTag = new Tag(tagId, projectId, "An updated label", THE_TAG_DESCRIPTION, COLOR, BG_COLOR);
        repository.saveTag(updatedTag);
        assertThat(getTagsCollectionSize(), is(1L));
        Optional<Tag> foundTag = repository.findTagByTagId(tagId);
        assertThat(foundTag, is(Optional.of(updatedTag)));
    }

    @Test(expected = DuplicateKeyException.class)
    public void shouldNotSaveTagWithDuplicateLabel() {
        TagId otherTagId = TagId.getId("1234abcd-abcd-abcd-abcd-123456789abc");
        Tag otherTag = new Tag(otherTagId, projectId, THE_TAG_LABEL, THE_TAG_DESCRIPTION, COLOR, BG_COLOR);
        repository.saveTag(otherTag);
    }
}
