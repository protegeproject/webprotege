
package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.LocalNameDictionaryLanguage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.OWLClass;

import static edu.stanford.bmir.protege.web.shared.PrimitiveType.CLASS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class OWLClassData_TestCase {

    private OWLClassData clsData;

    @Mock
    private OWLClass entity;

    private String browserText = "The browserText";

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    @Before
    public void setUp() {
        shortForms = ImmutableMap.of(LocalNameDictionaryLanguage.get(), browserText);
        clsData = OWLClassData.get(entity, shortForms);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        OWLClassData.get(null, shortForms);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(clsData.getEntity(), is(this.entity));
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
        assertThat(clsData, is(OWLClassData.get(entity, shortForms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(clsData, is(not(OWLClassData.get(Mockito.mock(OWLClass.class), shortForms))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(clsData.hashCode(), is(OWLClassData.get(entity, shortForms).hashCode()));
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
