package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomsByTypeIndexImpl_TestCase {

    private AxiomsByTypeIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLSubClassOfAxiom axiom, axiom2;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        impl = new AxiomsByTypeIndexImpl();
        when(axiom.getAxiomType())
                .thenReturn((AxiomType) AxiomType.SUBCLASS_OF);
        when(axiom2.getAxiomType())
                .thenReturn((AxiomType) AxiomType.SUBCLASS_OF);
        impl.handleOntologyChanges(singletonList(AddAxiomChange.of(ontologyId, axiom)));
    }

    @Test
    public void shouldGetSubClassOfAxiom() {
        var axiomsStream = impl.getAxiomsByType(AxiomType.SUBCLASS_OF, ontologyId);
        var axioms = axiomsStream.collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldReturnEmptyStreamIfForUnknownOntologyId() {
        var axiomsStream = impl.getAxiomsByType(AxiomType.SUBCLASS_OF, mock(OWLOntologyID.class));
        var axioms = axiomsStream.collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldHandleAddAxiom() {
        impl.handleOntologyChanges(List.of(AddAxiomChange.of(ontologyId, axiom2)));
        var axioms = impl.getAxiomsByType(AxiomType.SUBCLASS_OF, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(axiom2));
    }

    @Test
    public void shouldHandleRemoveAxiom() {
        impl.handleOntologyChanges(List.of(RemoveAxiomChange.of(ontologyId, axiom)));
        var axioms = impl.getAxiomsByType(AxiomType.SUBCLASS_OF, ontologyId).collect(toSet());
        assertThat(axioms, not(hasItem(axiom)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfAxiomTypeIsNull() {
        impl.getAxiomsByType(null, ontologyId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.getAxiomsByType(AxiomType.SUBCLASS_OF, null);
    }
}
