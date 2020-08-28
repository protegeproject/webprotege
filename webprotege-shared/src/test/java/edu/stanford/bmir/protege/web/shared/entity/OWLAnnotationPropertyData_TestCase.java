
package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.LocalNameDictionaryLanguage;
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
        shortForms = ImmutableMap.of(LocalNameDictionaryLanguage.get(), browserText);
        data = OWLAnnotationPropertyData.get(entity, shortForms);
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        OWLAnnotationPropertyData.get(null, shortForms);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(data.getEntity(), is(this.entity));
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
        assertThat(data, is(OWLAnnotationPropertyData.get(entity, shortForms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(data,
                   is(not(OWLAnnotationPropertyData.get(Mockito.mock(OWLAnnotationProperty.class), shortForms))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(data.hashCode(), is(OWLAnnotationPropertyData.get(entity, shortForms).hashCode()));
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
