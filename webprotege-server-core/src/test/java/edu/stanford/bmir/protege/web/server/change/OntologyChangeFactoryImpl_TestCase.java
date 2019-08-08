package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.index.OntologyIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyChangeFactoryImpl_TestCase {

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLAnnotation annotation;

    private OntologyChangeFactoryImpl impl;

    @Before
    public void setUp() {
        when(ontologyIndex.getOntology(ontologyId)).thenReturn(Optional.of(ontology));
        impl = new OntologyChangeFactoryImpl(ontologyIndex);
    }

    @Test
    public void shouldCreateAddAxiom() {
        AddAxiom addAxiom = impl.createAddAxiom(ontologyId, axiom);
        assertThat(addAxiom.getOntology(), is(ontology));
        assertThat(addAxiom.getAxiom(), is(axiom));
    }

    @Test
    public void shouldCreateRemoveAxiom() {
        RemoveAxiom removeAxiom = impl.createRemoveAxiom(ontologyId, axiom);
        assertThat(removeAxiom.getOntology(), is(ontology));
        assertThat(removeAxiom.getAxiom(), is(axiom));
    }

    @Test
    public void shouldCreateAddOntologyAnnotation() {
        AddOntologyAnnotation addOntologyAnnotation = impl.createAddOntologyAnnotation(ontologyId, annotation);
        assertThat(addOntologyAnnotation.getOntology(), is(ontology));
        assertThat(addOntologyAnnotation.getAnnotation(), is(annotation));
    }

    @Test
    public void shouldCreateRemoveOntologyAnnotation() {
        RemoveOntologyAnnotation removeOntologyAnnotation = impl.createRemoveOntologyAnnotation(ontologyId, annotation);
        assertThat(removeOntologyAnnotation.getOntology(), is(ontology));
        assertThat(removeOntologyAnnotation.getAnnotation(), is(annotation));
    }
}
