
package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SetOboTermIdAction_TestCase {

    private SetOboTermIdAction setOboTermIdAction;
    @Mock
    private ProjectId projectId;
    @Mock
    private OWLEntity entity;
    @Mock
    private OBOTermId oboTermId;

    @Before
    public void setUp() {
        setOboTermIdAction = new SetOboTermIdAction(projectId, entity, oboTermId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new SetOboTermIdAction(null, entity, oboTermId);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(setOboTermIdAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new SetOboTermIdAction(projectId, null, oboTermId);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(setOboTermIdAction.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_oboTermId_IsNull() {
        new SetOboTermIdAction(projectId, entity, null);
    }

    @Test
    public void shouldReturnSupplied_oboTermId() {
        assertThat(setOboTermIdAction.getOboTermId(), is(this.oboTermId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(setOboTermIdAction, is(setOboTermIdAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(setOboTermIdAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(setOboTermIdAction, is(new SetOboTermIdAction(projectId, entity, oboTermId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(setOboTermIdAction, is(not(new SetOboTermIdAction(mock(ProjectId.class), entity, oboTermId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(setOboTermIdAction, is(not(new SetOboTermIdAction(projectId, mock(OWLEntity.class), oboTermId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_oboTermId() {
        assertThat(setOboTermIdAction, is(not(new SetOboTermIdAction(projectId, entity, mock(OBOTermId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(setOboTermIdAction.hashCode(), is(new SetOboTermIdAction(projectId, entity, oboTermId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(setOboTermIdAction.toString(), startsWith("SetOboTermIdAction"));
    }

}
