package edu.stanford.bmir.protege.web.shared.metrics;

import edu.stanford.bmir.protege.web.server.metrics.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityCountMetricTestCase {


    public static final int SIGNATURE_SIZE = 3;
    @Mock
    private OWLOntology ontology;

    @Mock
    private Set signature;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        when(signature.size()).thenReturn(SIGNATURE_SIZE);
        when(ontology.getClassesInSignature(true)).thenReturn(signature);
        when(ontology.getObjectPropertiesInSignature(true)).thenReturn(signature);
        when(ontology.getDataPropertiesInSignature(true)).thenReturn(signature);
        when(ontology.getAnnotationPropertiesInSignature()).thenReturn(signature);
        when(ontology.getIndividualsInSignature(true)).thenReturn(signature);
    }

    @Test
    public void shouldReturnSizeOfClassesInImportsClosure() {
        ClassCountMetricCalculator metric = new ClassCountMetricCalculator(ontology);
        IntegerMetricValue integerMetricValue = metric.computeValue();
        assertThat(integerMetricValue.getValue(), is(SIGNATURE_SIZE));
    }

    @Test
    public void shouldReturnSizeOfObjectPropertiesInImportsClosure() {
        ObjectPropertyCountMetricCalculator metric = new ObjectPropertyCountMetricCalculator(ontology);
        IntegerMetricValue integerMetricValue = metric.computeValue();
        assertThat(integerMetricValue.getValue(), is(SIGNATURE_SIZE));
    }

    @Test
    public void shouldReturnSizeOfDataPropertiesInImportsClosure() {
        DataPropertyCountMetricCalculator metric = new DataPropertyCountMetricCalculator(ontology);
        IntegerMetricValue integerMetricValue = metric.computeValue();
        assertThat(integerMetricValue.getValue(), is(SIGNATURE_SIZE));
    }

    @Test
    public void shouldReturnSizeOfAnnotationPropertiesInImportsClosure() {
        AnnotationPropertyCountMetricCalculator metric = new AnnotationPropertyCountMetricCalculator(ontology);
        IntegerMetricValue integerMetricValue = metric.computeValue();
        assertThat(integerMetricValue.getValue(), is(SIGNATURE_SIZE));
    }

    @Test
    public void shouldReturnSizeOfNamedIndividualsInImportsClosure() {
        NamedIndividualCountMetricCalculator metric = new NamedIndividualCountMetricCalculator(ontology);
        IntegerMetricValue integerMetricValue = metric.computeValue();
        assertThat(integerMetricValue.getValue(), is(SIGNATURE_SIZE));
    }
}
