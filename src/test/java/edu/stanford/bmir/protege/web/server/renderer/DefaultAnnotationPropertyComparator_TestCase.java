package edu.stanford.bmir.protege.web.server.renderer;

import edu.stanford.bmir.protege.web.server.render.DefaultAnnotationPropertyComparator;
import edu.stanford.bmir.protege.web.server.render.IRIIndexProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 04/10/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAnnotationPropertyComparator_TestCase {

    @Mock
    private ShortFormProvider sfp;

    @Mock
    private IRIIndexProvider iriIndexProvider;

    @Mock
    private OWLAnnotationProperty indexedPropertyA, indexedPropertyB, otherProperty;

    @Mock
    private IRI indexedIRIA, indexedIRIB, otherPropertyIRI;

    private DefaultAnnotationPropertyComparator comparator;

    @Before
    public void setUp() throws Exception {
        comparator = new DefaultAnnotationPropertyComparator(sfp, iriIndexProvider);

        when(indexedPropertyA.getIRI()).thenReturn(indexedIRIA);
        when(iriIndexProvider.getIndex(indexedIRIA)).thenReturn(2);

        when(indexedPropertyB.getIRI()).thenReturn(indexedIRIB);
        when(iriIndexProvider.getIndex(indexedIRIB)).thenReturn(3);

        when(otherProperty.getIRI()).thenReturn(otherPropertyIRI);
    }

    @Test
    public void shouldComeBefore() {
        int diff = comparator.compare(indexedPropertyA, indexedPropertyB);
        assertThat(diff, is(-1));
    }

    @Test
    public void shouldComeAfter() {
        int diff = comparator.compare(indexedPropertyB, indexedPropertyA);
        assertThat(diff, is(1));
    }

    @Test
    public void shouldPlaceRDFSLabelFirstEvenIfNotIndexed() {
        when(otherProperty.getIRI()).thenReturn(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        assertThat(comparator.compare(otherProperty, indexedPropertyA), is(lessThan(0)));
    }

    @Test
    public void shouldPlaceAnnotationPropertyWithShortFormContainingLabelFirst() {
        when(sfp.getShortForm(otherProperty)).thenReturn("myLabel");
        assertThat(comparator.compare(otherProperty, indexedPropertyA), is(lessThan(0)));
    }

    @Test
    public void shouldPlaceAnnotationPropertyWithShortFormContainingDefinitionFirst() {
        when(sfp.getShortForm(otherProperty)).thenReturn("myDefinition");
        assertThat(comparator.compare(otherProperty, indexedPropertyA), is(lessThan(0)));
    }

    @Test
    public void shouldCompareByShortFormCaseInsensitiveWhenIndexesAreTied() {
        when(iriIndexProvider.getIndex(otherPropertyIRI)).thenReturn(2);
        when(sfp.getShortForm(indexedPropertyB)).thenReturn("B");
        when(sfp.getShortForm(otherProperty)).thenReturn("a");
        assertThat(comparator.compare(otherProperty, indexedPropertyB), is(lessThan(0)));
        assertThat(comparator.compare(indexedPropertyB, otherProperty), is(greaterThan(0)));
    }
}
