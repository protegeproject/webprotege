
package edu.stanford.bmir.protege.web.shared.sharing;

import java.util.ArrayList;
import java.util.List;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ProjectSharingSettings_TestCase {

    private ProjectSharingSettings projectSharingSettings;
    @Mock
    private ProjectId projectId;

    private SharingPermission defaultSharingPermission = SharingPermission.EDIT;

    private List<SharingSetting> sharingSettings;

    @Before
    public void setUp()
        throws Exception
    {
        sharingSettings = new ArrayList<>();
        projectSharingSettings = new ProjectSharingSettings(projectId, defaultSharingPermission, sharingSettings);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectSharingSettings(null, defaultSharingPermission, sharingSettings);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        MatcherAssert.assertThat(projectSharingSettings.getProjectId(), Matchers.is(this.projectId));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_defaultSharingSetting_IsNull() {
        new ProjectSharingSettings(projectId, null, sharingSettings);
    }

    @Test
    public void shouldReturnSupplied_defaultSharingSetting() {
        MatcherAssert.assertThat(projectSharingSettings.getDefaultSharingPermission(), Matchers.is(this.defaultSharingPermission));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_sharingSettings_IsNull() {
        new ProjectSharingSettings(projectId, defaultSharingPermission, null);
    }

    @Test
    public void shouldReturnSupplied_sharingSettings() {
        MatcherAssert.assertThat(projectSharingSettings.getSharingSettings(), Matchers.is(this.sharingSettings));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(projectSharingSettings, Matchers.is(projectSharingSettings));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(projectSharingSettings.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(projectSharingSettings, Matchers.is(new ProjectSharingSettings(projectId, defaultSharingPermission, sharingSettings)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        MatcherAssert.assertThat(projectSharingSettings, Matchers.is(Matchers.not(new ProjectSharingSettings(mock(ProjectId.class), defaultSharingPermission, sharingSettings))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_defaultSharingSetting() {
        MatcherAssert.assertThat(projectSharingSettings, Matchers.is(Matchers.not(new ProjectSharingSettings(projectId, SharingPermission.COMMENT, sharingSettings))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_sharingSettings() {
        ArrayList<SharingSetting> otherSettings = new ArrayList<>();
        otherSettings.add(mock(SharingSetting.class));
        MatcherAssert.assertThat(projectSharingSettings, Matchers.is(Matchers.not(new ProjectSharingSettings(projectId, defaultSharingPermission, otherSettings))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(projectSharingSettings.hashCode(), Matchers.is(new ProjectSharingSettings(projectId, defaultSharingPermission, sharingSettings).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(projectSharingSettings.toString(), Matchers.startsWith("ProjectSharingSettings"));
    }

}
