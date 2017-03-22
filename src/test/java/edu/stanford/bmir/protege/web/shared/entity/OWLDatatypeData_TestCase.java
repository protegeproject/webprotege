
package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.OWLDatatype;

import static edu.stanford.bmir.protege.web.shared.PrimitiveType.DATA_TYPE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@RunWith(value = org.mockito.runners.MockitoJUnitRunner.class)
public class OWLDatatypeData_TestCase {

    private OWLDatatypeData data;

    @Mock
    private OWLDatatype entity;

    private String browserText = "The browserText";

    @Before
    public void setUp()
        throws Exception
    {
        data = new OWLDatatypeData(entity, browserText);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new OWLDatatypeData(null, browserText);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(data.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_browserText_IsNull() {
        new OWLDatatypeData(entity, null);
    }

    @Test
    public void shouldReturnSupplied_browserText() {
        assertThat(data.getBrowserText(), is(this.browserText));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(data, is(data));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(data.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(data, is(new OWLDatatypeData(entity, browserText)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(data, is(Matchers.not(new OWLDatatypeData(Mockito.mock(OWLDatatype.class), browserText))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_browserText() {
        assertThat(data, is(Matchers.not(new OWLDatatypeData(entity, "String-21fa6bff-caee-40c3-85b1-411d0df51d1a"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(data.hashCode(), is(new OWLDatatypeData(entity, browserText).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(data.toString(), startsWith("OWLDatatypeData"));
    }

    @Test
    public void should_getType() {
        assertThat(data.getType(), is(DATA_TYPE));
    }
}
