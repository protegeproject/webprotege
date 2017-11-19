
package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLClass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(value = MockitoJUnitRunner.class)
public class SetOboTermCrossProductAction_TestCase {

    private SetOboTermCrossProductAction setOboTermCrossProductAction;
    @Mock
    private ProjectId projectId;
    @Mock
    private OWLClass entity;
    @Mock
    private OBOTermCrossProduct crossProduct;

    @Before
    public void setUp() {
        setOboTermCrossProductAction = new SetOboTermCrossProductAction(projectId, entity, crossProduct);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new SetOboTermCrossProductAction(null, entity, crossProduct);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(setOboTermCrossProductAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new SetOboTermCrossProductAction(projectId, null, crossProduct);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(setOboTermCrossProductAction.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_crossProduct_IsNull() {
        new SetOboTermCrossProductAction(projectId, entity, null);
    }

    @Test
    public void shouldReturnSupplied_crossProduct() {
        assertThat(setOboTermCrossProductAction.getCrossProduct(), is(this.crossProduct));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(setOboTermCrossProductAction, is(setOboTermCrossProductAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(setOboTermCrossProductAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(setOboTermCrossProductAction, is(new SetOboTermCrossProductAction(projectId, entity, crossProduct)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(setOboTermCrossProductAction, is(not(new SetOboTermCrossProductAction(Mockito.mock(ProjectId.class), entity, crossProduct))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(setOboTermCrossProductAction, is(not(new SetOboTermCrossProductAction(projectId, Mockito.mock(OWLClass.class), crossProduct))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_crossProduct() {
        assertThat(setOboTermCrossProductAction, is(not(new SetOboTermCrossProductAction(projectId, entity, Mockito.mock(OBOTermCrossProduct.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(setOboTermCrossProductAction.hashCode(), is(new SetOboTermCrossProductAction(projectId, entity, crossProduct).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(setOboTermCrossProductAction.toString(), startsWith("SetOboTermCrossProductAction"));
    }

}
