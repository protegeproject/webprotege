
package edu.stanford.bmir.protege.web.shared.sharing;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ProjectSharingSettings_TestCase {

    private ProjectSharingSettings projectSharingSettings;

    @Mock
    private ProjectId projectId;

    private Optional<SharingPermission> linkSharingPermission = Optional.of(SharingPermission.EDIT);

    private List<SharingSetting> sharingSettings = new ArrayList<>();

    @Before
    public void setUp() {
        projectSharingSettings = new ProjectSharingSettings(projectId, linkSharingPermission, sharingSettings);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectSharingSettings(null, linkSharingPermission, sharingSettings);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(projectSharingSettings.getProjectId(), is(this.projectId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_linkSharingPermission_IsNull() {
        new ProjectSharingSettings(projectId, null, sharingSettings);
    }

    @Test
    public void shouldReturnSupplied_linkSharingPermission() {
        assertThat(projectSharingSettings.getLinkSharingPermission(), is(this.linkSharingPermission));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_sharingSettings_IsNull() {
        new ProjectSharingSettings(projectId, linkSharingPermission, null);
    }

    @Test
    public void shouldReturnSupplied_sharingSettings() {
        assertThat(projectSharingSettings.getSharingSettings(), is(this.sharingSettings));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(projectSharingSettings, is(projectSharingSettings));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(projectSharingSettings.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(projectSharingSettings, is(new ProjectSharingSettings(projectId, linkSharingPermission, sharingSettings)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(projectSharingSettings, is(not(new ProjectSharingSettings(mock(ProjectId.class), linkSharingPermission, sharingSettings))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_linkSharingPermission() {
        assertThat(projectSharingSettings, is(not(new ProjectSharingSettings(projectId, Optional.of(SharingPermission.COMMENT), sharingSettings))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_sharingSettings() {
        ArrayList<SharingSetting> otherSharingSettings = new ArrayList<>();
        otherSharingSettings.add(mock(SharingSetting.class));
        assertThat(projectSharingSettings, is(not(new ProjectSharingSettings(projectId, linkSharingPermission, otherSharingSettings))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(projectSharingSettings.hashCode(), is(new ProjectSharingSettings(projectId, linkSharingPermission, sharingSettings).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(projectSharingSettings.toString(), startsWith("ProjectSharingSettings"));
    }

}
