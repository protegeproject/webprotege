package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByIndividualIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.impl.ProjectClassAssertionAxiomsByIndividualIndexImpl;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectClassAssertionAxiomsByIndividualIndexImpl_TestCase {

    private ProjectClassAssertionAxiomsByIndividualIndexImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLClassAssertionAxiom classAssertionAxiom;

    @Mock
    private OWLIndividual individual;

    @Before
    public void setUp() {
        impl = new ProjectClassAssertionAxiomsByIndividualIndexImpl(projectOntologiesIndex,
                                                                    classAssertionAxiomsIndex);
        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontologyId));
        when(classAssertionAxiomsIndex.getClassAssertionAxioms(any(), any()))
                .thenReturn(Stream.empty());
        when(classAssertionAxiomsIndex.getClassAssertionAxioms(individual, ontologyId))
                .thenReturn(Stream.of(classAssertionAxiom));
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(projectOntologiesIndex, classAssertionAxiomsIndex));
    }

    @Test
    public void shouldGetClassAssertionAxioms() {
        var axioms = impl.getClassAssertionAxioms(individual).collect(toSet());
        assertThat(axioms, Matchers.hasItem(classAssertionAxiom));
    }

    @Test
    public void shouldNotGetAxiomsForUnknowIndividual() {
        var axioms = impl.getClassAssertionAxioms(mock(OWLIndividual.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfIndividualIsNull() {
        impl.getClassAssertionAxioms(null);
    }
}
