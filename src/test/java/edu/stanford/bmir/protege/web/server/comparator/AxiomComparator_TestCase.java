package edu.stanford.bmir.protege.web.server.comparator;

import edu.stanford.bmir.protege.web.shared.axiom.AxiomIRISubjectProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;
import org.semanticweb.owlapi.util.IRIShortFormProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomComparator_TestCase {

    @Mock
    private OWLAxiom axiomA, axiomB;

    @Mock
    private AxiomIRISubjectProvider axiomSubjectProvider;

    @Mock
    private IRIShortFormProvider iriShortFormProvider;

    private AxiomComparator axiomComparator;

    private List<AxiomType<?>> axiomTypeOrdering;

    @Before
    public void setUp() throws Exception {
        axiomTypeOrdering = new ArrayList<>();
        axiomComparator = new AxiomComparator(axiomSubjectProvider, iriShortFormProvider, axiomTypeOrdering);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldPlaceDeclarationsFirst() {
        when(axiomA.getAxiomType()).thenReturn((AxiomType) AxiomType.DECLARATION);
        when(axiomB.getAxiomType()).thenReturn((AxiomType) AxiomType.ANNOTATION_ASSERTION);
        assertThat(axiomComparator.compare(axiomA, axiomB), is(greaterThan(0)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldPlaceAnnotationAssertionsNext() {
        when(axiomA.getAxiomType()).thenReturn((AxiomType) AxiomType.ANNOTATION_ASSERTION);
        when(axiomB.getAxiomType()).thenReturn((AxiomType) AxiomType.CLASS_ASSERTION);
        assertThat(axiomComparator.compare(axiomA, axiomB), is(greaterThan(0)));
    }
}
