
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
import org.semanticweb.owlapi.model.OWLDataProperty;

import static edu.stanford.bmir.protege.web.shared.PrimitiveType.DATA_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(MockitoJUnitRunner.class)
public class OWLDataPropertyData_TestCase {

    private OWLDataPropertyData oWLDataPropertyData;

    @Mock
    private OWLDataProperty entity;

    private String browserText = "The browserText";

    @Before
    public void setUp() {
        oWLDataPropertyData = new OWLDataPropertyData(entity, browserText);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new OWLDataPropertyData(null, browserText);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(oWLDataPropertyData.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_browserText_IsNull() {
        new OWLDataPropertyData(entity, null);
    }

    @Test
    public void shouldReturnSupplied_browserText() {
        assertThat(oWLDataPropertyData.getBrowserText(), is(this.browserText));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(oWLDataPropertyData, is(oWLDataPropertyData));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(oWLDataPropertyData.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(oWLDataPropertyData, is(new OWLDataPropertyData(entity, browserText)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(oWLDataPropertyData, is(not(new OWLDataPropertyData(Mockito.mock(OWLDataProperty.class), browserText))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_browserText() {
        assertThat(oWLDataPropertyData, is(not(new OWLDataPropertyData(entity, "String-aa07abdc-dc06-4e7a-82ba-58eaa8482871"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(oWLDataPropertyData.hashCode(), is(new OWLDataPropertyData(entity, browserText).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(oWLDataPropertyData.toString(), Matchers.startsWith("OWLDataPropertyData"));
    }

    @Test
    public void should_getType() {
        assertThat(oWLDataPropertyData.getType(), is(DATA_PROPERTY));
    }

    @Test
    public void shouldReturn_false_For_isOWLAnnotationProperty() {
        assertThat(oWLDataPropertyData.isOWLAnnotationProperty(), is(false));
    }
}
