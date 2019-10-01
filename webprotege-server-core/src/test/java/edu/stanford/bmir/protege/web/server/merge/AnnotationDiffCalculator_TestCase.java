package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.project.Ontology;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnotationDiffCalculator_TestCase {


    private AnnotationDiffCalculator calculator;

    @Mock
    private Ontology fromOnt;

    @Mock
    private Ontology toOnt;

    @Mock
    private OWLAnnotation annotationA, annotationB, annotationC;

    @Before
    public void setUp() {
        calculator = new AnnotationDiffCalculator();
        when(fromOnt.getAnnotations())
                .thenReturn(ImmutableSet.of(annotationA, annotationB));
        when(toOnt.getAnnotations())
                .thenReturn(ImmutableSet.of(annotationB, annotationC));
    }

    @Test
    public void shouldGetAddedAnnotations() {
        var diff = calculator.computeDiff(fromOnt, toOnt);
        var addedAnnotations = diff.getAdded();
        assertThat(addedAnnotations, contains(annotationC));
    }

    @Test
    public void shouldGetRemovedAnnotations() {
        var diff = calculator.computeDiff(fromOnt, toOnt);
        var removedAnnotations = diff.getRemoved();
        assertThat(removedAnnotations, contains(annotationA));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfFromOntologyIsNull() {
        calculator.computeDiff(null, toOnt);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfToOntologyIsNull() {
        calculator.computeDiff(fromOnt, null);
    }
}
