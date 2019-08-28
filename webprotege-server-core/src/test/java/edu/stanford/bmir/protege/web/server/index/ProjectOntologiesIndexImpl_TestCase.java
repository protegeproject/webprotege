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
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectOntologiesIndexImpl_TestCase {

    @Mock
    private OWLOntologyManager manager;

    @Mock
    private OWLOntology rootOntology;

    @Mock
    private OWLOntologyID rootOntologyId;

    private ProjectOntologiesIndexImpl impl;

    @Before
    public void setUp() {
        when(rootOntology.getOntologyID()).thenReturn(rootOntologyId);
        when(rootOntology.getOWLOntologyManager()).thenReturn(manager);
        when(manager.getOntologies()).thenReturn(Collections.singleton(rootOntology));
        when(manager.getOntology(rootOntologyId)).thenReturn(rootOntology);
        impl = new ProjectOntologiesIndexImpl(rootOntology);
    }

    @Test
    public void shouldReturnStreamOfRootOntologyId() {
        var ontologyIdStream = impl.getOntologyIds();
        var ontologyIds = ontologyIdStream.collect(Collectors.toSet());
        assertThat(ontologyIds, contains(rootOntologyId));
    }
}
