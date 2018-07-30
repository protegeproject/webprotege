
package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import static edu.stanford.bmir.protege.web.shared.PrimitiveType.ANNOTATION_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(MockitoJUnitRunner.class)
public class OWLAnnotationPropertyData_TestCase {

    private OWLAnnotationPropertyData data;

    @Mock
    private OWLAnnotationProperty entity;

    private String browserText = "The browserText";

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    @Before
    public void setUp() {
        shortForms = ImmutableMap.of();
        data = OWLAnnotationPropertyData.get(entity, browserText, shortForms);
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        OWLAnnotationPropertyData.get(null, browserText, shortForms);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(data.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_browserText_IsNull() {
        OWLAnnotationPropertyData.get(entity, null, shortForms);
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
    @SuppressWarnings("ObjectEqualsNull" )
    public void shouldNotBeEqualToNull() {
        assertThat(data.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(data, is(OWLAnnotationPropertyData.get(entity, browserText, shortForms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(data,
                   is(not(OWLAnnotationPropertyData.get(Mockito.mock(OWLAnnotationProperty.class), browserText, shortForms))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_browserText() {
        assertThat(data,
                   is(not(OWLAnnotationPropertyData.get(entity, "String-6b8e4a42-6d66-47e8-b5ec-e9c463c9a0fc", shortForms))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(data.hashCode(), is(OWLAnnotationPropertyData.get(entity, browserText, shortForms).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(data.toString(), Matchers.startsWith("OWLAnnotationPropertyData" ));
    }

    @Test
    public void shouldReturn_true_For_isOWLAnnotationProperty() {
        assertThat(data.isOWLAnnotationProperty(), is(true));
    }

    @Test
    public void should_getType() {
        assertThat(data.getType(), is(ANNOTATION_PROPERTY));
    }
}
