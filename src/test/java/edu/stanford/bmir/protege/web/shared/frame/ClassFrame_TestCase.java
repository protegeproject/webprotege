package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.Sets;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.Collections;
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
 * 12/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ClassFrame_TestCase {


    private ClassFrame classFrame;

    private ClassFrame otherClassFrame;

    @Mock
    private OWLClass subject;

    @Mock
    private OWLClass cls;

    @Mock
    private PropertyValue propertyValue;


    private Set<OWLClass> classes;

    private Set<PropertyValue> propertyValues;

    @Before
    public void setUp() throws Exception {
        classes = Sets.newHashSet(cls);
        propertyValues = Sets.newHashSet(propertyValue);
        classFrame = new ClassFrame(subject, classes, propertyValues);
        otherClassFrame = new ClassFrame(subject, classes, propertyValues);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new ClassFrame(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfSubjectIsNull() {
        new ClassFrame(null, classes, propertyValues);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfClassesIsNull() {
        new ClassFrame(subject, null, propertyValues);
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfPropertyValuesIsNull() {
        new ClassFrame(subject, classes, null);
    }


    @Test
    public void shouldBeEqualToSelf() {
        assertThat(classFrame, is(equalTo(classFrame)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(classFrame, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(classFrame, is(equalTo(otherClassFrame)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(classFrame.hashCode(), is(otherClassFrame.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(classFrame.toString(), startsWith("ClassFrame"));
    }

    @Test
    public void shouldReturnSuppliedSubject() {
        assertThat(classFrame.getSubject(), is(subject));
    }

    @Test
    public void shouldReturnSuppliedPropertyValues() {
        assertThat(classFrame.getPropertyValues(), is(propertyValues));
    }

    @Test
    public void shouldReturnSignature() {
        OWLObjectProperty property = mock(OWLObjectProperty.class);
        when(property.getSignature()).thenReturn(Collections.<OWLEntity>singleton(property));
        when(propertyValue.getProperty()).thenReturn(property);
        OWLClass value = mock(OWLClass.class);
        when(value.getSignature()).thenReturn(Collections.<OWLEntity>singleton(value));
        when(propertyValue.getValue()).thenReturn(value);
        assertThat(classFrame.getSignature(),
                Matchers.<OWLEntity>containsInAnyOrder(
                        subject, cls, property, value));
    }
}