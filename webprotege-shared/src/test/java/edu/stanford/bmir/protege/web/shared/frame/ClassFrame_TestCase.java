package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

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
    private OWLClassData subject;

    @Mock
    private OWLClassData cls;

    @Mock
    private PropertyValue propertyValue;


    private ImmutableSet<OWLClassData> classes;

    private ImmutableSet<PropertyValue> propertyValues;

    @Before
    public void setUp() throws Exception {
        classes = ImmutableSet.of(cls);
        propertyValues = ImmutableSet.of(propertyValue);
        classFrame = ClassFrame.get(subject, classes, propertyValues);
        otherClassFrame = ClassFrame.get(subject, classes, propertyValues);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfSubjectIsNull() {
        ClassFrame.get(null, classes, propertyValues);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfClassesIsNull() {
        ClassFrame.get(subject, null, propertyValues);
    }


    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfPropertyValuesIsNull() {
        ClassFrame.get(subject, classes, null);
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
        assertThat(classFrame.getPropertyValues(), hasItems(propertyValue));
    }
}