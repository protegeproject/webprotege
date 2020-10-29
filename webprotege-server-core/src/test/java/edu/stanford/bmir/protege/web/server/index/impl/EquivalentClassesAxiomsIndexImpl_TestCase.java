package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Collections;

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
    private OntologyDocumentId ontologyDocumentId;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLEquivalentClassesAxiom axiom;

    @Before
    public void setUp() {
        when(axiom.getNamedClasses())
                .thenReturn(Collections.singleton(cls));
        impl = new EquivalentClassesAxiomsIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyDocumentId, axiom)));
    }

    @Test
    public void shouldGetEquivalentClassesAxiomForSubClass() {
        var axioms = impl.getEquivalentClassesAxioms(cls, ontologyDocumentId).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getEquivalentClassesAxioms(cls, mock(OntologyDocumentId.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownClass() {
        var axioms = impl.getEquivalentClassesAxioms(mock(OWLClass.class), ontologyDocumentId).collect(toSet());
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
        impl.getEquivalentClassesAxioms(null, ontologyDocumentId);
    }

}
