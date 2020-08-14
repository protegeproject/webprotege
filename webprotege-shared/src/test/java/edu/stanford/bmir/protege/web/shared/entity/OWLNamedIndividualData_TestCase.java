
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
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import static edu.stanford.bmir.protege.web.shared.PrimitiveType.NAMED_INDIVIDUAL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class OWLNamedIndividualData_TestCase {

    private OWLNamedIndividualData oWLNamedIndividualData;

    @Mock
    private OWLNamedIndividual entity;

    private String browserText = "The browserText";

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    @Before
    public void setUp() {
        shortForms = ImmutableMap.of(LocalNameDictionaryLanguage.get(), browserText);
        oWLNamedIndividualData = OWLNamedIndividualData.get(entity, shortForms);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        OWLNamedIndividualData.get(null, shortForms);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(oWLNamedIndividualData.getEntity(), is(this.entity));
    }

    @Test
    public void shouldReturnSupplied_browserText() {
        assertThat(oWLNamedIndividualData.getBrowserText(), is(this.browserText));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(oWLNamedIndividualData, is(oWLNamedIndividualData));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(oWLNamedIndividualData.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(oWLNamedIndividualData, is(OWLNamedIndividualData.get(entity, shortForms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(oWLNamedIndividualData, is(not(OWLNamedIndividualData.get(Mockito.mock(OWLNamedIndividual.class), shortForms))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(oWLNamedIndividualData.hashCode(), is(OWLNamedIndividualData.get(entity, shortForms).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(oWLNamedIndividualData.toString(), startsWith("OWLNamedIndividualData"));
    }

    @Test
    public void should_getType() {
        assertThat(oWLNamedIndividualData.getType(), is(NAMED_INDIVIDUAL));
    }
}
