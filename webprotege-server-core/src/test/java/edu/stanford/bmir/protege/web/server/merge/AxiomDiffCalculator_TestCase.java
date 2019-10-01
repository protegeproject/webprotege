package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.project.Ontology;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomDiffCalculator_TestCase {

    private AxiomDiffCalculator calculator;

    @Mock
    private Ontology fromOnt;

    @Mock
    private Ontology toOnt;

    @Mock
    private OWLAxiom axiomA, axiomB, axiomC;

    @Before
    public void setUp() {
        calculator = new AxiomDiffCalculator();
        when(fromOnt.getAxioms())
                .thenReturn(ImmutableSet.of(axiomA, axiomB));
        when(toOnt.getAxioms())
                .thenReturn(ImmutableSet.of(axiomB, axiomC));
    }

    @Test
    public void shouldGetAddedAxioms() {
        var diff = calculator.computeDiff(fromOnt, toOnt);
        var addedAxioms = diff.getAdded();
        assertThat(addedAxioms, contains(axiomC));
    }

    @Test
    public void shouldGetRemovedAxioms() {
        var diff = calculator.computeDiff(fromOnt, toOnt);
        var removedAxioms = diff.getRemoved();
        assertThat(removedAxioms, contains(axiomA));
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
