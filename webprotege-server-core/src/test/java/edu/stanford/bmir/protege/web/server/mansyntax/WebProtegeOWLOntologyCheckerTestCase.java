package edu.stanford.bmir.protege.web.server.mansyntax;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class WebProtegeOWLOntologyCheckerTestCase {

    @Mock
    protected OWLOntology rootOntology;

    @Mock
    protected OntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    private WebProtegeOWLOntologyChecker checker;


    @Before
    public void setUp() {
        checker = new WebProtegeOWLOntologyChecker(rootOntology, ontologyIRIShortFormProvider);
        when(rootOntology.getImportsClosure()).thenReturn(Collections.singleton(rootOntology));
    }

    /**
     * Test for legacy behaviour
     */
    @Test
    public void shouldReturnRootOntologyForNullArgument() {
        OWLOntology ont = checker.getOntology(null);
        assertThat(ont, is(equalTo(rootOntology)));
    }

    @Test
    public void shouldReturnNullForUnknownShortForm() {
        when(ontologyIRIShortFormProvider.getShortForm(rootOntology)).thenReturn("y");
        OWLOntology ont = checker.getOntology("x");
        assertThat(ont, is((OWLOntology) null));
    }

    @Test
    public void shouldReturnOntologyWithGivenShortForm() {
        when(ontologyIRIShortFormProvider.getShortForm(rootOntology)).thenReturn("x");
        OWLOntology ont = checker.getOntology("x");
        assertThat(ont, is(equalTo(rootOntology)));
    }

}
