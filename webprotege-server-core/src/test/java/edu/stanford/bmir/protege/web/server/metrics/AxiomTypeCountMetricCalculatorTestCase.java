package edu.stanford.bmir.protege.web.server.metrics;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.metrics.IntegerMetricValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 27/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomTypeCountMetricCalculatorTestCase {

    public static final int AXIOM_COUNT = 33;

    @Mock
    protected OWLOntology ontology, importedOntology;

    private AxiomType axiomType;

    @Before
    public void setUp() {
        axiomType = AxiomType.SUBCLASS_OF;
        when(ontology.getImportsClosure()).thenReturn(Sets.newHashSet(ontology, importedOntology));
        when(ontology.getAxiomCount(axiomType)).thenReturn(AXIOM_COUNT - 5);
        when(importedOntology.getAxiomCount(axiomType)).thenReturn(5);
    }

    @Test
    public void shouldReturnAxiomTypeCount() {
        AxiomTypeCountMetricCalculator calculator = new AxiomTypeCountMetricCalculator(ontology, axiomType);
        IntegerMetricValue value = calculator.computeValue();
        assertThat(value.getValue(), is(AXIOM_COUNT));
    }

}
