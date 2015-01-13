package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ObjectPropertyFrame_TestCase {

    @Mock
    private OWLObjectProperty subject;

    private Set<OWLClass> domains;

    private Set<OWLClass> ranges;

    private Set<PropertyAnnotationValue> annotations;

    private Set<OWLObjectProperty> inverses;

    private Set<ObjectPropertyCharacteristic> characteristics;

    private ObjectPropertyFrame objectPropertyFrame;

    private ObjectPropertyFrame otherObjectPropertyFrame;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    @Mock
    private PropertyAnnotationValue annotationValue;

    @Mock
    private OWLClass domain;

    @Mock
    private OWLClass range;

    @Mock
    private OWLObjectProperty inverse;

    @Before
    public void setUp() throws Exception {
        annotations = Sets.newHashSet(annotationValue);
        domain = mock(OWLClass.class);
        domains = Sets.newHashSet(domain);
        ranges = Sets.newHashSet(range);
        inverses = Sets.newHashSet(inverse);
        characteristics = Sets.newHashSet(ObjectPropertyCharacteristic.FUNCTIONAL,
                ObjectPropertyCharacteristic.INVERSE_FUNCTIONAL);
        objectPropertyFrame = new ObjectPropertyFrame(
                subject,
                annotations,
                domains,
                ranges,
                inverses,
                characteristics
        );

        otherObjectPropertyFrame = new ObjectPropertyFrame(
                subject,
                annotations,
                domains,
                ranges,
                inverses,
                characteristics
        );
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfSubjectIsNull() {
        new ObjectPropertyFrame(
                null,
                annotations,
                domains,
                ranges,
                inverses,
                characteristics
        );
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfAnnotationsIsNull() {
        new ObjectPropertyFrame(
                subject,
                null,
                domains,
                ranges,
                inverses,
                characteristics
        );
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfDomainsIsNull() {
        new ObjectPropertyFrame(
                subject,
                annotations,
                null,
                ranges,
                inverses,
                characteristics
        );
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfRangesIsNull() {
        new ObjectPropertyFrame(
                subject,
                annotations,
                domains,
                null,
                inverses,
                characteristics
        );
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfInversesIsNull() {
        new ObjectPropertyFrame(
                subject,
                annotations,
                domains,
                ranges,
                null,
                characteristics
        );
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfCharacteristicsIsNull() {
        new ObjectPropertyFrame(
                subject,
                annotations,
                domains,
                ranges,
                inverses,
                null
        );
    }


    @Test
    public void shouldBeEqualToSelf() {
        assertThat(objectPropertyFrame, is(equalTo(objectPropertyFrame)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(objectPropertyFrame, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(objectPropertyFrame, is(equalTo(otherObjectPropertyFrame)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(objectPropertyFrame.hashCode(), is(otherObjectPropertyFrame.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(objectPropertyFrame.toString(), startsWith("ObjectPropertyFrame"));
    }

    @Test
    public void shouldReturnSuppliedSubject() {
        assertThat(objectPropertyFrame.getSubject(), is(subject));
    }

    @Test
    public void shouldReturnSuppliedAnnotations() {
        assertThat(objectPropertyFrame.getAnnotationPropertyValues(), is(annotations));
    }

    @Test
    public void shouldReturnSuppliedDomains() {
        assertThat(objectPropertyFrame.getDomains(), is(domains));
    }

    @Test
    public void shouldReturnSuppliedRanges() {
        assertThat(objectPropertyFrame.getRanges(), is(ranges));
    }

    @Test
    public void shouldReturnSuppliedCharacteristics() {
        assertThat(objectPropertyFrame.getCharacteristics(), is(characteristics));
    }

    @Test
    public void shouldReturnSignature() {
        when(annotationValue.getProperty()).thenReturn(annotationProperty);
        OWLAnnotationValue value = mock(OWLAnnotationValue.class);
        when(annotationValue.getValue()).thenReturn(value);
        when(value.getSignature()).thenReturn(Collections.<OWLEntity>emptySet());
        assertThat(objectPropertyFrame.getSignature(), containsInAnyOrder(
                subject,
                annotationProperty,
                domain,
                range,
                inverse));
    }



}