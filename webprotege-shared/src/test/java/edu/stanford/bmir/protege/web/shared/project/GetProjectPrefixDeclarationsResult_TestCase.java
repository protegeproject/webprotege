
package edu.stanford.bmir.protege.web.shared.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetProjectPrefixDeclarationsResult_TestCase {

    private GetProjectPrefixDeclarationsResult result;

    @Mock
    private ProjectId projectId;

    private List<PrefixDeclaration> prefixDeclarations;

    @Before
    public void setUp() {
        prefixDeclarations = new ArrayList<>();
        prefixDeclarations.add(mock(PrefixDeclaration.class));
        result = new GetProjectPrefixDeclarationsResult(projectId, prefixDeclarations);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new GetProjectPrefixDeclarationsResult(null, prefixDeclarations);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(result.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_prefixDeclarations_IsNull() {
        new GetProjectPrefixDeclarationsResult(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_prefixDeclarations() {
        assertThat(result.getPrefixDeclarations(), is(this.prefixDeclarations));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(result, is(result));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(result.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(result, is(new GetProjectPrefixDeclarationsResult(projectId, prefixDeclarations)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(result, is(not(new GetProjectPrefixDeclarationsResult(mock(ProjectId.class), prefixDeclarations))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_prefixDeclarations() {
        assertThat(result, is(not(new GetProjectPrefixDeclarationsResult(projectId, Collections.emptyList()))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(result.hashCode(), is(new GetProjectPrefixDeclarationsResult(projectId, prefixDeclarations).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(result.toString(), Matchers.startsWith("GetProjectPrefixDeclarationsResult"));
    }

}
