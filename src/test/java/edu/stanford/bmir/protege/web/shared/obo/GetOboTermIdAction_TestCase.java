
package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(value = org.mockito.runners.MockitoJUnitRunner.class)
public class GetOboTermIdAction_TestCase {

    private GetOboTermIdAction getOboTermIdAction;
    @Mock
    private ProjectId projectId;
    @Mock
    private OWLEntity term;

    @Before
    public void setUp() {
        getOboTermIdAction = new GetOboTermIdAction(projectId, term);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new GetOboTermIdAction(null, term);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(getOboTermIdAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_term_IsNull() {
        new GetOboTermIdAction(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_term() {
        assertThat(getOboTermIdAction.getTerm(), is(this.term));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermIdAction, is(getOboTermIdAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermIdAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermIdAction, is(new GetOboTermIdAction(projectId, term)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(getOboTermIdAction, is(not(new GetOboTermIdAction(mock(ProjectId.class), term))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_term() {
        assertThat(getOboTermIdAction, is(not(new GetOboTermIdAction(projectId, mock(OWLEntity.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermIdAction.hashCode(), is(new GetOboTermIdAction(projectId, term).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermIdAction.toString(), startsWith("GetOboTermIdAction"));
    }
}
