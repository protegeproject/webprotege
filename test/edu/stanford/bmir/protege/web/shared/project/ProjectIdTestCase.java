package edu.stanford.bmir.protege.web.shared.project;


import com.google.common.base.Optional;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
public class ProjectIdTestCase {

    @Test
    public void equalsShouldReturnTrueForObjectsWithSameId() {
        String uuid = "0d8f03d4-d9bb-496d-a78c-146868af8265";
        ProjectId projectIdA = ProjectId.get(uuid);
        ProjectId projectIdB = ProjectId.get(uuid);
        assertEquals(projectIdA, projectIdB);
    }

    @Test
    public void equalsShouldReturnFalseForObjectsWithDifferentIds() {
        String uuidA = "0d8f03d4-d9bb-496d-a78c-146868af8265";
        String uuidB = "d16a6ca0-3afc-4af3-8a95-82cdc82f52cc";
        ProjectId projectIdA = ProjectId.get(uuidA);
        ProjectId projectIdB = ProjectId.get(uuidB);
        assertFalse(projectIdA.equals(projectIdB));
    }

    @Test
    public void equalsNullReturnsFalse() {
        String uuid = "0d8f03d4-d9bb-496d-a78c-146868af8265";
        ProjectId projectId = ProjectId.get(uuid);
        assertFalse(projectId.equals(null));
    }

    @Test(expected = ProjectIdFormatException.class)
    public void malformedUUIDThrowsProjectIdFormatException() {
        String malformedId = "wrong";
        ProjectId.get(malformedId);
    }

    @Test
    public void getIdReturnsSuppliedValue() {
        String uuid = "0d8f03d4-d9bb-496d-a78c-146868af8265";
        ProjectId projectId = ProjectId.get(uuid);
        assertEquals(uuid, projectId.getId());
    }

    @Test
    public void getRegExpReturnsCorrectExpression() {
        String regExp = ProjectId.getIdRegExp().getSource();
        assertEquals("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", regExp);
    }

    @Test
    public void getFromNullableReturnsAbsentWithNull() {
        Optional<ProjectId> projectIdOptional = ProjectId.getFromNullable(null);
        assertFalse(projectIdOptional.isPresent());
    }

    @Test
    public void getFromNullableReturnsProjectIdWithNonNull() {
        String uuid = "0d8f03d4-d9bb-496d-a78c-146868af8265";
        Optional<ProjectId> projectIdOptional = ProjectId.getFromNullable(uuid);
        assertTrue(projectIdOptional.isPresent());
    }

    @Test(expected = ProjectIdFormatException.class)
    public void getFromNullableThrowsProjectIdFormatExceptionForMalformedId() {
        String malformedId = "wrong";
        ProjectId.getFromNullable(malformedId);
    }
}
