package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnotationMatcher_TestCase {

    private AnnotationMatcher matcher;

    @Mock
    private Matcher<OWLAnnotationProperty> propertyMatcher;

    @Mock
    private Matcher<OWLAnnotationValue> valueMatcher;

    @Mock
    private OWLAnnotationProperty theProperty;

    @Mock
    private OWLAnnotationValue theValue;

    @Mock
    private OWLAnnotation theAnnotation;

    @Before
    public void setUp() {
        matcher = new AnnotationMatcher(propertyMatcher, valueMatcher);

        when(theAnnotation.getProperty()).thenReturn(theProperty);
        when(theAnnotation.getValue()).thenReturn(theValue);

        when(propertyMatcher.matches(theProperty)).thenReturn(true);
        when(valueMatcher.matches(theValue)).thenReturn(true);
    }

    @Test
    public void shouldMatchPropertyAndValue() {
        assertThat(matcher.matches(theAnnotation), is(true));
    }

    @Test
    public void shouldMatchProperty() {
        when(propertyMatcher.matches(theProperty)).thenReturn(false);
        assertThat(matcher.matches(theAnnotation), is(false));
    }

    @Test
    public void shouldMatchValue() {
        when(valueMatcher.matches(theValue)).thenReturn(false);
        assertThat(matcher.matches(theAnnotation), is(false));
    }
}
