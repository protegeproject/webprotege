package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.server.shortform.WebProtegeOntologyIRIShortFormProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class WebProtegeOntologyIRIShortFormProvider_TestCase {


    private IRI iri;

    @Mock
    private OWLOntology rootOntology;

    private OWLOntologyID ontologyId;

    @Before
    public void setUp() throws Exception {
        iri = IRI.create("http://stuff");
        ontologyId = new OWLOntologyID(iri);
        when(rootOntology.getOntologyID()).thenReturn(ontologyId);
    }

    @Test
    public void shouldReturnStandardShortFormForRootOntology() {
        WebProtegeOntologyIRIShortFormProvider sfp = new WebProtegeOntologyIRIShortFormProvider(rootOntology);
        String shortForm = sfp.getShortForm(rootOntology);
        assertThat(shortForm, is(equalTo("root-ontology")));
    }

    @Test
    public void shouldReturnStandardShortFormForRootOntologyIRI() {
        WebProtegeOntologyIRIShortFormProvider sfp = new WebProtegeOntologyIRIShortFormProvider(rootOntology);
        String shortForm = sfp.getShortForm(iri);
        assertThat(shortForm, is(equalTo("root-ontology")));
    }
}
