
package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.LocalNameDictionaryLanguage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import static edu.stanford.bmir.protege.web.shared.PrimitiveType.OBJECT_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(value = MockitoJUnitRunner.class)
public class OWLObjectPropertyData_TestCase {

    private OWLObjectPropertyData oWLObjectPropertyData;

    @Mock
    private OWLObjectProperty entity;

    private String browserText = "The browserText";

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    @Before
    public void setUp() {
        shortForms = ImmutableMap.of(LocalNameDictionaryLanguage.get(), browserText);
        oWLObjectPropertyData = OWLObjectPropertyData.get(entity, shortForms);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        OWLObjectPropertyData.get(null, shortForms);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(oWLObjectPropertyData.getEntity(), is(this.entity));
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
        assertThat(oWLObjectPropertyData, is(OWLObjectPropertyData.get(entity, shortForms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(oWLObjectPropertyData, is(not(OWLObjectPropertyData.get(Mockito.mock(OWLObjectProperty.class), shortForms))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(oWLObjectPropertyData.hashCode(), is(OWLObjectPropertyData.get(entity, shortForms).hashCode()));
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
