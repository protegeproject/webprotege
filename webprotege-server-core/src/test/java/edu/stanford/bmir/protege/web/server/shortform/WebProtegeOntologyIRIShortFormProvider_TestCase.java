package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@SuppressWarnings("Guava")
@RunWith(MockitoJUnitRunner.class)
public class WebProtegeOntologyIRIShortFormProvider_TestCase {


    private IRI iri;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private DefaultOntologyIdManager defaultOntologyIdManager;

    @Before
    public void setUp() throws Exception {
        iri = IRI.create("http://stuff.com/OntologyA");
        when(defaultOntologyIdManager.getDefaultOntologyId()).thenReturn(ontologyId);
        when(ontologyId.getOntologyIRI())
                .thenReturn(Optional.of(iri));
    }

    @Test
    public void shouldReturnStandardShortFormForRootOntology() {
        var sfp = new WebProtegeOntologyIRIShortFormProvider(defaultOntologyIdManager);
        var shortForm = sfp.getShortForm(ontologyId);
        assertThat(shortForm, is(equalTo("root-ontology")));
    }

    @Test
    public void shouldReturnStandardShortFormForRootOntologyIRI() {
        var sfp = new WebProtegeOntologyIRIShortFormProvider(defaultOntologyIdManager);
        var shortForm = sfp.getShortForm(iri);
        assertThat(shortForm, is(equalTo("root-ontology")));
    }
}
