package edu.stanford.bmir.protege.web.server.mansyntax;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyID;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-22
 */
@RunWith(MockitoJUnitRunner.class)
public class ShellOwlOntology_TestCase {

    private ShellOwlOntology ontology;

    @Mock
    private OWLOntologyID ontologyId;

    @Before
    public void setUp() {
        ontology = ShellOwlOntology.get(ontologyId);
    }

    @Test
    public void shouldGetSuppliedOntologyId() {
        assertThat(ontology.getOntologyID(), is(ontologyId));
    }

    @Test
    public void shouldBeEqualToShellOntologyWithSameId() {
        var otherOntology = ShellOwlOntology.get(ontologyId);
        assertThat(ontology, is(equalTo(otherOntology)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOtherShellOntologyWithSameOntologyId() {
        var otherOntology = ShellOwlOntology.get(ontologyId);
        assertThat(ontology.hashCode(), is(equalTo(otherOntology.hashCode())));
    }

    @Test
    public void shouldBeEqualToOwlApiImplementationWithSameOntologyId() throws Exception {
        var owlapiOntology = new OWLOntologyImpl(OWLManager.createOWLOntologyManager(), ontologyId);
        assertThat(owlapiOntology, is(equalTo(ontology)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOwlApiImplementationWithSameOntologyId() throws Exception {
        var owlapiOntology = new OWLOntologyImpl(OWLManager.createOWLOntologyManager(), ontologyId);
        assertThat(owlapiOntology.hashCode(), is(equalTo(ontology.hashCode())));
    }
}
