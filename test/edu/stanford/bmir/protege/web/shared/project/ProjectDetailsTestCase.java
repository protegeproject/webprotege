package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/10/2013
 */
public class ProjectDetailsTestCase {

    private ProjectId projectId = ProjectId.get("0d8f03d4-d9bb-496d-a78c-146868af8265");

    @Test(expected = NullPointerException.class)
    public void nullProjectIdInConstructorThrowsNullPointerException() {
        new ProjectDetails(null, "", "", UserId.getGuest(), false);
    }

    @Test(expected = NullPointerException.class)
    public void nullDisplayNameInConstructorThrowsNullPointerException() {
        new ProjectDetails(projectId, null, "", UserId.getGuest(), false);
    }

    @Test(expected = NullPointerException.class)
    public void nullProjectDescriptionInConstructorThrowsNullPointerException() {
        new ProjectDetails(projectId, "", null, UserId.getGuest(), false);
    }

    @Test(expected = NullPointerException.class)
    public void nullUserIdInConstructorThrowsNullPointerException() {
        new ProjectDetails(projectId, "", "", null, false);
    }

    @Test
    public void emptyDisplayNameInConstructorIsOK() {
        ProjectDetails projectDetails = new ProjectDetails(projectId, "", "", UserId.getGuest(), false);
        assertEquals(projectDetails.getDisplayName(), "");
    }

    @Test
    public void emptyDescriptionInConstructorIsOK() {
        ProjectDetails projectDetails = new ProjectDetails(projectId, "", "", UserId.getGuest(), false);
        assertEquals(projectDetails.getDescription(), "");
    }

    @Test
    public void suppliedProjectIdIsReturnedByAccessor() {
        ProjectDetails projectDetails = new ProjectDetails(projectId, "", "", UserId.getGuest(), false);
        assertEquals(projectDetails.getProjectId(), projectId);
    }

    @Test
    public void suppliedUserIdIsReturnedByAccessor() {
        ProjectDetails projectDetails = new ProjectDetails(projectId, "", "", UserId.getGuest(), false);
        assertEquals(projectDetails.getOwner(), UserId.getGuest());
    }

    @Test
    public void suppliedTrashValueIsReturnedByAccessor() {
        ProjectDetails projectDetails = new ProjectDetails(projectId, "", "", UserId.getGuest(), true);
        assertEquals(projectDetails.isInTrash(), true);
    }

    @Test
    public void equalValuesMeansEqualProjectDetails() {
        ProjectDetails projectDetailsA = new ProjectDetails(projectId, "A", "B", UserId.getUserId("C"), true);
        ProjectDetails projectDetailsB = new ProjectDetails(projectId, "A", "B", UserId.getUserId("C"), true);
        assertEquals(projectDetailsA, projectDetailsB);
    }

    @Test
    public void equalValuesMeansEqualHashCodes() {
        ProjectDetails projectDetailsA = new ProjectDetails(projectId, "A", "B", UserId.getUserId("C"), true);
        ProjectDetails projectDetailsB = new ProjectDetails(projectId, "A", "B", UserId.getUserId("C"), true);
        assertEquals(projectDetailsA.hashCode(), projectDetailsB.hashCode());
    }

}
