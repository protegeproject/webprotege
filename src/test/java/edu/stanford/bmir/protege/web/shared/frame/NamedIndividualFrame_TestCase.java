package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class NamedIndividualFrame_TestCase {


    private NamedIndividualFrame namedIndividualFrame;

    private NamedIndividualFrame otherNamedIndividualFrame;

    @Mock
    private OWLNamedIndividualData subject;

    private Set<OWLClassData> types;

    @Mock
    private OWLClassData typeA, typeB;

    @Mock
    private PropertyValueList propertyValueList;


    private Set<OWLNamedIndividualData> sameIndividuals;

    @Mock
    private OWLNamedIndividualData individualA, individualB;

    @Before
    public void setUp() throws Exception {
        types = Sets.newHashSet(typeA, typeB);
        sameIndividuals = Sets.newHashSet(individualA, individualB);
        namedIndividualFrame = new NamedIndividualFrame(subject, types, propertyValueList, sameIndividuals);
        otherNamedIndividualFrame = new NamedIndividualFrame(subject, types, propertyValueList, sameIndividuals);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Subject_IsNull() {
        new NamedIndividualFrame(null, types, propertyValueList, sameIndividuals);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Types_IsNull() {
        new NamedIndividualFrame(subject, null, propertyValueList, sameIndividuals);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_PropertyValues_IsNull() {
        new NamedIndividualFrame(subject, types, null, sameIndividuals);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_SameIndividuals_IsNull() {
        new NamedIndividualFrame(subject, types, propertyValueList, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(namedIndividualFrame, is(equalTo(namedIndividualFrame)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(namedIndividualFrame, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(namedIndividualFrame, is(equalTo(otherNamedIndividualFrame)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(namedIndividualFrame.hashCode(), is(otherNamedIndividualFrame.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(namedIndividualFrame.toString(), startsWith("NamedIndividualFrame"));
    }

    @Test
    public void shouldReturnSupplied_Subject() {
        assertThat(namedIndividualFrame.getSubject(), is(subject));
    }

    @Test
    public void shouldReturnSupplied_Types() {
        assertThat(namedIndividualFrame.getClasses(), is(types));
    }

    @Test
    public void shouldReturnSupplied_PropertyValues() {
        assertThat(namedIndividualFrame.getPropertyValueList(), is(propertyValueList));
    }

    @Test
    public void shouldReturnSupplied_SameIndividuals() {
        assertThat(namedIndividualFrame.getSameIndividuals(), is(sameIndividuals));
    }
}