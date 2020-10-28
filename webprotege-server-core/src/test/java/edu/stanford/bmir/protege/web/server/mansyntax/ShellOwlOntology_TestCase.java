package edu.stanford.bmir.protege.web.server.mansyntax;

import edu.stanford.bmir.protege.web.server.owlapi.OwlApiOntologyDocumentTempOWLOntologyIDTranslator;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.apibinding.OWLManager;
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

    private OntologyDocumentId ontologyDocumentId = OntologyDocumentId.generate();

    @Before
    public void setUp() {
        ontology = ShellOwlOntology.get(ontologyDocumentId);
    }

    @Test
    public void shouldGetSuppliedOntologyId() {
        assertThat(ontology.getOntologyID(), is(ontologyDocumentId));
    }

    @Test
    public void shouldBeEqualToShellOntologyWithSameId() {
        var otherOntology = ShellOwlOntology.get(ontologyDocumentId);
        assertThat(ontology, is(equalTo(otherOntology)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOtherShellOntologyWithSameOntologyId() {
        var otherOntology = ShellOwlOntology.get(ontologyDocumentId);
        assertThat(ontology.hashCode(), is(equalTo(otherOntology.hashCode())));
    }

    @Test
    public void shouldBeEqualToOwlApiImplementationWithSameOntologyId() throws Exception {
        var ontologyId = OwlApiOntologyDocumentTempOWLOntologyIDTranslator.toOWLOntologyID(ontologyDocumentId);
        var owlapiOntology = new OWLOntologyImpl(OWLManager.createOWLOntologyManager(), ontologyId);
        assertThat(owlapiOntology, is(equalTo(ontology)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOwlApiImplementationWithSameOntologyId() throws Exception {
        var ontologyId = OwlApiOntologyDocumentTempOWLOntologyIDTranslator.toOWLOntologyID(ontologyDocumentId);
        var owlapiOntology = new OWLOntologyImpl(OWLManager.createOWLOntologyManager(), ontologyId);
        assertThat(owlapiOntology.hashCode(), is(equalTo(ontology.hashCode())));
    }
}
