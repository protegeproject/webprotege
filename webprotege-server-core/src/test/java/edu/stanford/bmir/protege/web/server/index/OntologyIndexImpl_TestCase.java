package edu.stanford.bmir.protege.web.server.index;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-08
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyIndexImpl_TestCase {

    @Mock
    private OWLOntologyManager manager;

    @Mock
    private OWLOntology rootOntology;

    @Mock
    private OWLOntologyID rootOntologyId;

    private OntologyIndexImpl impl;


    @Before
    public void setUp() {
        when(rootOntology.getOntologyID()).thenReturn(rootOntologyId);
        when(rootOntology.getOWLOntologyManager()).thenReturn(manager);
        when(manager.getOntologies()).thenReturn(Collections.singleton(rootOntology));
        when(manager.getOntology(rootOntologyId)).thenReturn(rootOntology);
        impl = new OntologyIndexImpl(rootOntology);
    }


    @Test
    public void shouldGetRootOntology() {
        var ontology = impl.getOntology(rootOntologyId);
        assertThat(ontology, is(Optional.of(rootOntology)));
    }

    @Test
    public void shouldNotReturnNonExistentOntology() {
        var ontology = impl.getOntology(mock(OWLOntologyID.class));
        assertThat(ontology.isEmpty(), is(true));
    }
}
