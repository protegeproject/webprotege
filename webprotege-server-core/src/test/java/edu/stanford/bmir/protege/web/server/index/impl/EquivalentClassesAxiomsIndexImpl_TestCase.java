package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AxiomsByEntityReferenceIndex;
import edu.stanford.bmir.protege.web.server.index.impl.EquivalentClassesAxiomsIndexImpl;
import edu.stanford.bmir.protege.web.server.index.impl.OntologyIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
@RunWith(MockitoJUnitRunner.class)
public class EquivalentClassesAxiomsIndexImpl_TestCase {

    private EquivalentClassesAxiomsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyID;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLEquivalentClassesAxiom axiom;

    @Mock
    private AxiomsByEntityReferenceIndex axiomsByEntityReference;

    @Before
    public void setUp() {
        when(axiomsByEntityReference.getReferencingAxioms(any(), any()))
                .thenAnswer(invocation -> Stream.of());
        when(axiomsByEntityReference.getReferencingAxioms(cls, ontologyID))
                .thenAnswer(invocation -> Stream.of(axiom));
        impl = new EquivalentClassesAxiomsIndexImpl(axiomsByEntityReference);
    }

    @Test
    public void shouldGetEquivalentClassesAxiomForSubClass() {
        var axioms = impl.getEquivalentClassesAxioms(cls, ontologyID).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getEquivalentClassesAxioms(cls, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownClass() {
        var axioms = impl.getEquivalentClassesAxioms(mock(OWLClass.class), ontologyID).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getEquivalentClassesAxioms(cls, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullCls() {
        impl.getEquivalentClassesAxioms(null, ontologyID);
    }

}
