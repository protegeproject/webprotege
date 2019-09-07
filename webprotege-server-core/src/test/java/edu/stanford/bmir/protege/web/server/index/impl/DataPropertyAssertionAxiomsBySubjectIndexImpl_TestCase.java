package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.index.impl.DataPropertyAssertionAxiomsBySubjectIndexImpl;
import edu.stanford.bmir.protege.web.server.index.impl.OntologyIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
 * 2019-08-12
 */
@RunWith(MockitoJUnitRunner.class)
public class DataPropertyAssertionAxiomsBySubjectIndexImpl_TestCase {

    private DataPropertyAssertionAxiomsBySubjectIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyID;

    @Mock
    private OWLIndividual subject;

    @Mock
    private OWLDataPropertyAssertionAxiom axiom;

    @Before
    public void setUp() {
        when(axiom.getSubject())
                .thenReturn(subject);
        impl = new DataPropertyAssertionAxiomsBySubjectIndexImpl();
        impl.handleOntologyChanges(List.of(AddAxiomChange.of(ontologyID, axiom)));
    }

    @Test
    public void shouldGetDataPropertyDomainAxiomForProperty() {
        var axioms = impl.getDataPropertyAssertions(subject, ontologyID).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getDataPropertyAssertions(subject, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownSubject() {
        var axioms = impl.getDataPropertyAssertions(mock(OWLIndividual.class), ontologyID).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getDataPropertyAssertions(subject, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullProperty() {
        impl.getDataPropertyAssertions(null, ontologyID);
    }

}
