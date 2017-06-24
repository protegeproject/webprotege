
package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetOboTermDefinitionAction_TestCase {

    private GetOboTermDefinitionAction getOboTermDefinitionAction;
    @Mock
    private ProjectId projectId;
    @Mock
    private OWLEntity term;

    @Before
    public void setUp() {
        getOboTermDefinitionAction = new GetOboTermDefinitionAction(projectId, term);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new GetOboTermDefinitionAction(null, term);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(getOboTermDefinitionAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_term_IsNull() {
        new GetOboTermDefinitionAction(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_term() {
        assertThat(getOboTermDefinitionAction.getTerm(), is(this.term));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermDefinitionAction, is(getOboTermDefinitionAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermDefinitionAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermDefinitionAction, is(new GetOboTermDefinitionAction(projectId, term)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(getOboTermDefinitionAction, is(not(new GetOboTermDefinitionAction(mock(ProjectId.class), term))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_term() {
        assertThat(getOboTermDefinitionAction, is(not(new GetOboTermDefinitionAction(projectId, mock(OWLEntity.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermDefinitionAction.hashCode(), is(new GetOboTermDefinitionAction(projectId, term).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermDefinitionAction.toString(), startsWith("GetOboTermDefinitionAction"));
    }

}
