
package edu.stanford.bmir.protege.web.shared.project;

import java.util.ArrayList;
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
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SetProjectPrefixDeclarationsAction_TestCase {

    private SetProjectPrefixDeclarationsAction action;

    private ProjectId projectId;

    private List<PrefixDeclaration> prefixDeclarations;

    @Before
    public void setUp() {
        projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");
        prefixDeclarations = new ArrayList<>();
        prefixDeclarations.add(mock(PrefixDeclaration.class));
        action = new SetProjectPrefixDeclarationsAction(projectId, prefixDeclarations);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new SetProjectPrefixDeclarationsAction(null, prefixDeclarations);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(action.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_prefixDeclarations_IsNull() {
        new SetProjectPrefixDeclarationsAction(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_prefixDeclarations() {
        assertThat(action.getPrefixDeclarations(), is(this.prefixDeclarations));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(action));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(action.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(new SetProjectPrefixDeclarationsAction(projectId, prefixDeclarations)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(action, is(Matchers.not(new SetProjectPrefixDeclarationsAction(mock(ProjectId.class), prefixDeclarations))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_prefixDeclarations() {
        List<PrefixDeclaration> otherDecls = new ArrayList<>();
        otherDecls.add(mock(PrefixDeclaration.class));
        assertThat(action, is(Matchers.not(new SetProjectPrefixDeclarationsAction(projectId, otherDecls))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(action.hashCode(), is(new SetProjectPrefixDeclarationsAction(projectId, prefixDeclarations).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(action.toString(), startsWith("SetProjectPrefixDeclarationsAction"));
    }

}
