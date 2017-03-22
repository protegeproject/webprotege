
package edu.stanford.bmir.protege.web.shared.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.PrimitiveType;

import static edu.stanford.bmir.protege.web.shared.PrimitiveType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class OWLClassData_TestCase {

    private OWLClassData clsData;

    @Mock
    private OWLClass entity;

    private String browserText = "The browserText";

    @Before
    public void setUp() {
        clsData = new OWLClassData(entity, browserText);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new OWLClassData(null, browserText);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(clsData.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_browserText_IsNull() {
        new OWLClassData(entity, null);
    }

    @Test
    public void shouldReturnSupplied_browserText() {
        assertThat(clsData.getBrowserText(), is(this.browserText));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(clsData, is(clsData));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(clsData.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(clsData, is(new OWLClassData(entity, browserText)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(clsData, is(not(new OWLClassData(Mockito.mock(OWLClass.class), browserText))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_browserText() {
        assertThat(clsData, is(not(new OWLClassData(entity, "String-f194bedd-dffb-4dda-b795-50790e318fc9"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(clsData.hashCode(), is(new OWLClassData(entity, browserText).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(clsData.toString(), startsWith("OWLClassData"));
    }

    @Test
    public void should_getType() {
        assertThat(clsData.getType(), equalTo(CLASS));
    }
}
