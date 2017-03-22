
package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import static edu.stanford.bmir.protege.web.shared.PrimitiveType.OBJECT_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(value = MockitoJUnitRunner.class)
public class OWLObjectPropertyData_TestCase {

    private OWLObjectPropertyData oWLObjectPropertyData;

    @Mock
    private OWLObjectProperty entity;

    private String browserText = "The browserText";

    @Before
    public void setUp() {
        oWLObjectPropertyData = new OWLObjectPropertyData(entity, browserText);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new OWLObjectPropertyData(null, browserText);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(oWLObjectPropertyData.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_browserText_IsNull() {
        new OWLObjectPropertyData(entity, null);
    }

    @Test
    public void shouldReturnSupplied_browserText() {
        assertThat(oWLObjectPropertyData.getBrowserText(), is(this.browserText));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(oWLObjectPropertyData, is(oWLObjectPropertyData));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(oWLObjectPropertyData.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(oWLObjectPropertyData, is(new OWLObjectPropertyData(entity, browserText)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(oWLObjectPropertyData, is(not(new OWLObjectPropertyData(Mockito.mock(OWLObjectProperty.class), browserText))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_browserText() {
        assertThat(oWLObjectPropertyData, is(not(new OWLObjectPropertyData(entity, "String-b5c89b5d-753e-4678-800b-ee8aff6d631e"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(oWLObjectPropertyData.hashCode(), is(new OWLObjectPropertyData(entity, browserText).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(oWLObjectPropertyData.toString(), startsWith("OWLObjectPropertyData"));
    }

    @Test
    public void should_getType() {
        assertThat(oWLObjectPropertyData.getType(), is(OBJECT_PROPERTY));
    }

    @Test
    public void shouldReturn_false_For_isOWLAnnotationProperty() {
        assertThat(oWLObjectPropertyData.isOWLAnnotationProperty(), is(false));
    }

}
