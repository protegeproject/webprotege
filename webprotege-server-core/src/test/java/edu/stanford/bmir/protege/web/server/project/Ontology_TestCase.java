package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@RunWith(MockitoJUnitRunner.class)
public class Ontology_TestCase {

    private Ontology ontology;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotation annotation;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLImportsDeclaration importsDeclaration;

    @Before
    public void setUp() {
        ontology = Ontology.get(ontologyId,
                                ImmutableSet.of(importsDeclaration),
                                ImmutableSet.of(annotation),
                                ImmutableSet.of(axiom));
    }

    @Test
    public void shouldGetSuppliedOntologyId() {
        assertThat(ontology.getOntologyId(), is(ontologyId));
    }

    @Test
    public void shouldGetSuppliedImportsDeclaration() {
        assertThat(ontology.getImportsDeclarations(), contains(importsDeclaration));
    }

    @Test
    public void shouldGetSuppliedAnnotations() {
        assertThat(ontology.getAnnotations(), contains(annotation));
    }

    @Test
    public void shouldGetSuppliedAxioms() {
        assertThat(ontology.getAxioms(), contains(axiom));
    }

}
