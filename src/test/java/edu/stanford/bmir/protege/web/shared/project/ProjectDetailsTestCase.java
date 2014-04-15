package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/10/2013
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectDetailsTestCase {

    public static final boolean IN_TRASH = true;

    @Mock
    private ProjectId projectId;

    @Mock
    private UserId userId;

    private String displayName;

    private String description;

    private ProjectDetails projectDetails;

    @Before
    public void setUp() throws Exception {
        displayName = "DisplayName";
        description = "Description";
        projectDetails = new ProjectDetails(projectId, displayName, description, userId, IN_TRASH);
    }

    @Test(expected = NullPointerException.class)
    public void nullProjectIdInConstructorThrowsNullPointerException() {
        new ProjectDetails(null, displayName, description, userId, IN_TRASH);
    }

    @Test(expected = NullPointerException.class)
    public void nullDisplayNameInConstructorThrowsNullPointerException() {
        new ProjectDetails(projectId, null, displayName, userId, IN_TRASH);
    }

    @Test(expected = NullPointerException.class)
    public void nullProjectDescriptionInConstructorThrowsNullPointerException() {
        new ProjectDetails(projectId, displayName, null, userId, IN_TRASH);
    }

    @Test(expected = NullPointerException.class)
    public void nullUserIdInConstructorThrowsNullPointerException() {
        new ProjectDetails(projectId, displayName, "", null, IN_TRASH);
    }

    @Test
    public void emptyDisplayNameInConstructorIsOK() {
        assertEquals(projectDetails.getDisplayName(), displayName);
    }

    @Test
    public void emptyDescriptionInConstructorIsOK() {
        assertEquals(projectDetails.getDescription(), description);
    }

    @Test
    public void suppliedProjectIdIsReturnedByAccessor() {
        assertEquals(projectDetails.getProjectId(), projectId);
    }

    @Test
    public void suppliedUserIdIsReturnedByAccessor() {
        assertEquals(projectDetails.getOwner(), userId);
    }

    @Test
    public void suppliedTrashValueIsReturnedByAccessor() {
        assertEquals(projectDetails.isInTrash(), IN_TRASH);
    }

    @Test
    public void equalValuesMeansEqualProjectDetails() {
        ProjectDetails projectDetailsA = projectDetails;
        ProjectDetails projectDetailsB = new ProjectDetails(projectId, displayName, description, userId, IN_TRASH);
        assertEquals(projectDetailsA, projectDetailsB);
    }

    @Test
    public void equalValuesMeansEqualHashCodes() {
        ProjectDetails projectDetailsA = projectDetails;
        ProjectDetails projectDetailsB = new ProjectDetails(projectId, displayName, description, userId, IN_TRASH);
        assertEquals(projectDetailsA.hashCode(), projectDetailsB.hashCode());
    }
    
    
    @Test
    public void buildBuildsEqualObject() {
        ProjectDetails details = projectDetails.builder().build();
        assertThat(details, is(equalTo(projectDetails)));
    }

    @Test
    public void buildBuildsCorrectDetails() {
        ProjectDetails details = ProjectDetails.builder(projectId, userId, displayName, description)
                .setInTrash(IN_TRASH).build();
        assertThat(details, is(equalTo(projectDetails)));
    }

    @Test
    public void builderSetDisplayNameChangesDisplayName() {
        ProjectDetails details = projectDetails.builder().setDisplayName("Hello").build();
        assertThat(details.getDisplayName(), is(equalTo("Hello")));
    }

    @Test
    public void builderSetDescriptionChangesDescriptionName() {
        ProjectDetails details = projectDetails.builder().setDescription("Hello").build();
        assertThat(details.getDescription(), is(equalTo("Hello")));
    }

    @Test
    public void builderSetInTrashChangesInTrash() {
        ProjectDetails details = projectDetails.builder().setInTrash(true).build();
        assertThat(details.isInTrash(), is(true));
    }

    @Test
    public void builderSetOwnerChangesOwner() {
        UserId userId = mock(UserId.class);
        ProjectDetails details = projectDetails.builder().setOwner(userId).build();
        assertThat(details.getOwner(), is(equalTo(userId)));
    }

}
