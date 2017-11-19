
package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class GetOboTermSynonymsAction_TestCase {

    private GetOboTermSynonymsAction getOboTermSynonymsAction;
    @Mock
    private ProjectId projectId;
    @Mock
    private OWLEntity entity;

    @Before
    public void setUp() {
        getOboTermSynonymsAction = new GetOboTermSynonymsAction(projectId, entity);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new GetOboTermSynonymsAction(null, entity);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(getOboTermSynonymsAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new GetOboTermSynonymsAction(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(getOboTermSynonymsAction.getEntity(), is(this.entity));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermSynonymsAction, is(getOboTermSynonymsAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermSynonymsAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermSynonymsAction, is(new GetOboTermSynonymsAction(projectId, entity)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(getOboTermSynonymsAction, is(not(new GetOboTermSynonymsAction(Mockito.mock(ProjectId.class), entity))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(getOboTermSynonymsAction, is(not(new GetOboTermSynonymsAction(projectId, Mockito.mock(OWLEntity.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermSynonymsAction.hashCode(), is(new GetOboTermSynonymsAction(projectId, entity).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermSynonymsAction.toString(), startsWith("GetOboTermSynonymsAction"));
    }

}
