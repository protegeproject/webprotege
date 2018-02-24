
package edu.stanford.bmir.protege.web.shared.project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@RunWith(MockitoJUnitRunner.class)
public class ProjectPrefixes_TestCase {

    private static final String PREFIX_NAME_A = "a:";

    private static final String PREFIX_A = "http://ont.org/a/";

    private static final String PREFIX_NAME_B = "b:";

    private static final String PREFIX_B = "http://ont.org/b/";

    private ProjectId projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");

    private Map<String, String> prefixes;

    private ProjectPrefixes projectPrefixes;

    @Before
    public void setUp() {
        prefixes = new HashMap<>();
        prefixes.put(PREFIX_NAME_A, PREFIX_A);
        prefixes.put(PREFIX_NAME_B, PREFIX_B);
        projectPrefixes = ProjectPrefixes.get(projectId, prefixes);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(projectPrefixes, is(projectPrefixes));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(projectPrefixes.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(projectPrefixes, is(ProjectPrefixes.get(projectId, prefixes)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(projectPrefixes.hashCode(), is(ProjectPrefixes.get(projectId, prefixes).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(projectPrefixes.toString(), startsWith("ProjectPrefixes"));
    }

    @Test
    public void should_getProjectId() {
        assertThat(projectPrefixes.getProjectId(), is(projectId));
    }

    @Test
    public void should_getPrefixes() {
        assertThat(projectPrefixes.getPrefixes(), is(prefixes));
    }

    @Test
    public void should_getPrefixForPrefixName() {
        assertThat(projectPrefixes.getPrefixForPrefixName(PREFIX_NAME_A), is(Optional.of(PREFIX_A)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_forInvalidPrefixName() {
        assertThat(projectPrefixes.getPrefixForPrefixName("a"), is(Optional.of("")));
    }

    @Test
    public void should_not_getPrefixForNonExistantPrefixName() {
        assertThat(projectPrefixes.getPrefixForPrefixName("x:"), is(Optional.empty()));
    }


}
