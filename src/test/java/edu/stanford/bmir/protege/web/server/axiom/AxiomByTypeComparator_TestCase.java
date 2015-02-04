package edu.stanford.bmir.protege.web.server.axiom;

import edu.stanford.bmir.protege.web.shared.axiom.AxiomByTypeComparator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doReturn;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomByTypeComparator_TestCase {

    private AxiomByTypeComparator comparator;

    @Mock
    private OWLAxiom axiom1, axiom2, axiom3;

    @Mock
    private AxiomType<?> firstAxiomType, secondAxiomType, thirdAxiomType;

    @Before
    public void setUp() throws Exception {
        List<AxiomType<?>> ordering = new ArrayList<>();
        ordering.add(firstAxiomType);
        ordering.add(secondAxiomType);
        Mockito.doReturn(firstAxiomType).when(axiom1).getAxiomType();
        Mockito.doReturn(secondAxiomType).when(axiom2).getAxiomType();
        comparator = new AxiomByTypeComparator(ordering);
    }

    @Test
    public void shouldReturnMinusOne() {
        assertThat(comparator.compare(axiom1, axiom2), is(-1));
    }

    @Test
    public void shouldReturnPlusOne() {
        assertThat(comparator.compare(axiom2, axiom1), is(1));
    }

    @Test
    public void shouldReturnZero() {
        assertThat(comparator.compare(axiom1, axiom1), is(0));
    }

    @Test
    public void shouldReturnGreaterThanZeroForUnknownType() {
        doReturn(thirdAxiomType).when(axiom3).getAxiomType();
        assertThat(comparator.compare(axiom3, axiom1), is(greaterThan(0)));
    }

    @Test
    public void shouldReturnLessThanZeroForUnknownType() {
        doReturn(thirdAxiomType).when(axiom3).getAxiomType();
        assertThat(comparator.compare(axiom1, axiom3), is(lessThan(0)));
    }
}
