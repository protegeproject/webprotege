package edu.stanford.bmir.protege.web.server.crud.persistence;

import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 */
public class ProjectEntityCrudKitSettingsTestCase {

    public static final String PROJECT_ID_FIELD_NAME = "projectId";

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullProjectIdInConstructor() {
        new ProjectEntityCrudKitSettings(null, mock(EntityCrudKitSettings.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullEntityCrudKitSettings() {
        new ProjectEntityCrudKitSettings(mock(ProjectId.class), null);
    }

    @Test
    public void getProjectIdShouldReturnValueEqualToProjectIdSuppliedInConstructor() {
        ProjectId projectId = mock(ProjectId.class);
        ProjectEntityCrudKitSettings settings = new ProjectEntityCrudKitSettings(projectId, mock(EntityCrudKitSettings.class));
        assertEquals(projectId, settings.getProjectId());
    }

    @Test
    public void getSettingsShouldReturnValueEqualToSettingsSuppliedInConstructor() {
        EntityCrudKitSettings<?> entityCrudKitSettings = mock(EntityCrudKitSettings.class);
        ProjectEntityCrudKitSettings settings = new ProjectEntityCrudKitSettings(mock(ProjectId.class), entityCrudKitSettings);
        assertEquals(entityCrudKitSettings, settings.getSettings());
    }

    @Test
    public void hashCodeShouldBeEqualForSameProjectIdAndSettings() {
        ProjectId projectId = mock(ProjectId.class);
        EntityCrudKitSettings<?> settings = mock(EntityCrudKitSettings.class);
        ProjectEntityCrudKitSettings settingsA = new ProjectEntityCrudKitSettings(projectId, settings);
        ProjectEntityCrudKitSettings settingsB = new ProjectEntityCrudKitSettings(projectId, settings);
        assertEquals(settingsA.hashCode(), settingsB.hashCode());
    }

    @Test
    public void equalsShouldReturnTrueForEqualProjectIdAndEqualSettings() {
        ProjectId projectId = mock(ProjectId.class);
        EntityCrudKitSettings<?> settings = mock(EntityCrudKitSettings.class);
        ProjectEntityCrudKitSettings settingsA = new ProjectEntityCrudKitSettings(projectId, settings);
        ProjectEntityCrudKitSettings settingsB = new ProjectEntityCrudKitSettings(projectId, settings);
        assertEquals(settingsA, settingsB);
    }

    @Test(expected = NoSuchFieldException.class)
    public void classShouldNotHaveAnIdField() throws NoSuchFieldException {
        ProjectEntityCrudKitSettings.class.getDeclaredField("id");
    }

    @Test
    public void classShouldHaveAProjectIdField() {
        try {
            ProjectEntityCrudKitSettings.class.getDeclaredField(PROJECT_ID_FIELD_NAME);
        } catch (NoSuchFieldException e) {
            fail("projectId field does not exist");
        }
    }
}
