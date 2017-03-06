package edu.stanford.bmir.protege.web.server.project;

import com.google.googlejavaformat.Op;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Mar 2017
 */
public class ProjectDetailsRepository_IT {

    public static final String COLLECTION_NAME = "ProjectDetails";

    public static final long CREATED_AT = 33L;

    public static final long MODIFIED_AT = 44L;

    public static final boolean IN_TRASH = true;

    private ProjectDetailsRepository repository;

    private MongoDatabase database;

    private ProjectId projectId = getProjectId();

    private ProjectId otherProjectId = getProjectId();

    private static ProjectId getProjectId() {
        return ProjectId.get(UUID.randomUUID().toString());
    }

    private UserId owner = UserId.getUserId("The Owner");

    private UserId createdBy = UserId.getUserId("The Creator");

    private UserId lastModifiedBy = UserId.getUserId("The Editor");

    private UserId otherUser = UserId.getUserId("Other User");

    private ProjectDetails projectDetails;

    @Before
    public void setUp() {
        MongoClient mongoClient = MongoTestUtils.createMongoClient();
        database = mongoClient.getDatabase(MongoTestUtils.getTestDbName());
        repository = new ProjectDetailsRepository(database, new ProjectDetailsConverter());
        projectDetails = new ProjectDetails(projectId,
                                            "The Display Name",
                                            "The Description",
                                            owner,
                                            IN_TRASH,
                                            CREATED_AT,
                                            createdBy,
                                            MODIFIED_AT,
                                            lastModifiedBy);

        // Insert project details
        repository.save(projectDetails);
    }

    @After
    public void cleanUp() {
        database.drop();
    }

    @Test
    public void shouldSaveProjectDetails() {
        assertThat(getCollection().count(), is(1L));
    }

    @Test
    public void shouldUpdateProjectDetails() {
        repository.save(projectDetails);
        assertThat(getCollection().count(), is(1L));
    }

    private MongoCollection<Document> getCollection() {
        return database.getCollection(COLLECTION_NAME);
    }

    @Test
    public void shouldFindByProjectId() {
        Optional<ProjectDetails> details = repository.findOne(projectId);
        assertThat(details, is(Optional.of(projectDetails)));
    }

    @Test
    public void shouldNotContainProjectDetails() {
        assertThat(repository.containsProject(otherProjectId), is(false));
    }

    @Test
    public void shouldContainProjectDetails() {
        repository.save(projectDetails);
        assertThat(repository.containsProject(projectId), is(true));
    }

    @Test
    public void shouldContainProjectWithOwner() {
        assertThat(repository.containsProjectWithOwner(projectId, owner), is(true));
    }

    @Test
    public void shouldNotContainProjectWithOwner() {
        assertThat(repository.containsProjectWithOwner(projectId, otherUser), is(false));
    }

    @Test
    public void shouldDeleteProject() {
        repository.delete(projectId);
        assertThat(getCollection().count(), is(0L));
    }

    @Test
    public void shouldFindProjectByOwner() {
        assertThat(repository.findByOwner(owner), is(singletonList(projectDetails)));
    }

    @Test
    public void shouldSetInTrashTrue() {
        repository.setInTrash(projectId, true);
        assertThat(repository.findOne(projectId).map(d -> d.isInTrash()), is(Optional.of(true)));
    }

    @Test
    public void shouldSetInTrashFalse() {
        repository.setInTrash(projectId, false);
        assertThat(repository.findOne(projectId).map(d -> d.isInTrash()), is(Optional.of(false)));
    }

    @Test
    public void shouldSetModified() {
        long modifiedAt = 55L;
        repository.setModified(projectId, modifiedAt, otherUser);
        Optional<ProjectDetails> projectDetails = repository.findOne(projectId);
        assertThat(projectDetails.isPresent(), is(true));
        if (projectDetails.isPresent()) {
            assertThat(projectDetails.get().getLastModifiedAt(), is(modifiedAt));
            assertThat(projectDetails.get().getLastModifiedBy(), is(otherUser));
        }
    }


}
