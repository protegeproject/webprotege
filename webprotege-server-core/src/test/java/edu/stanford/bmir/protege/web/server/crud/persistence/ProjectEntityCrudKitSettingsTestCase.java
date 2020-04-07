package edu.stanford.bmir.protege.web.server.crud.persistence;

import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
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
        ProjectEntityCrudKitSettings.get(null, mock(EntityCrudKitSettings.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullEntityCrudKitSettings() {
        ProjectEntityCrudKitSettings.get(mock(ProjectId.class), null);
    }

    @Test
    public void getProjectIdShouldReturnValueEqualToProjectIdSuppliedInConstructor() {
        ProjectId projectId = mock(ProjectId.class);
        ProjectEntityCrudKitSettings settings = ProjectEntityCrudKitSettings.get(projectId, mock(EntityCrudKitSettings.class));
        assertEquals(projectId, settings.getProjectId());
    }

    @Test
    public void getSettingsShouldReturnValueEqualToSettingsSuppliedInConstructor() {
        EntityCrudKitSettings<?> entityCrudKitSettings = mock(EntityCrudKitSettings.class);
        ProjectEntityCrudKitSettings settings = ProjectEntityCrudKitSettings.get(mock(ProjectId.class), entityCrudKitSettings);
        assertEquals(entityCrudKitSettings, settings.getSettings());
    }

    @Test
    public void hashCodeShouldBeEqualForSameProjectIdAndSettings() {
        ProjectId projectId = mock(ProjectId.class);
        EntityCrudKitSettings<?> settings = mock(EntityCrudKitSettings.class);
        ProjectEntityCrudKitSettings settingsA = ProjectEntityCrudKitSettings.get(projectId, settings);
        ProjectEntityCrudKitSettings settingsB = ProjectEntityCrudKitSettings.get(projectId, settings);
        assertEquals(settingsA.hashCode(), settingsB.hashCode());
    }

    @Test
    public void equalsShouldReturnTrueForEqualProjectIdAndEqualSettings() {
        ProjectId projectId = mock(ProjectId.class);
        EntityCrudKitSettings<?> settings = mock(EntityCrudKitSettings.class);
        ProjectEntityCrudKitSettings settingsA = ProjectEntityCrudKitSettings.get(projectId, settings);
        ProjectEntityCrudKitSettings settingsB = ProjectEntityCrudKitSettings.get(projectId, settings);
        assertEquals(settingsA, settingsB);
    }
}
