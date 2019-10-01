package edu.stanford.bmir.protege.web.server.change;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@RunWith(MockitoJUnitRunner.class)
public class OwlOntologyChangeTranslatorVisitor_TestCase {

    private OwlOntologyChangeTranslatorVisitor visitor;


    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLImportsDeclaration importsDecl;

    @Mock
    private OWLAnnotation ontologyAnnotation;

    @Before
    public void setUp() {
        when(ontology.getOntologyID())
                .thenReturn(ontologyId);
        visitor = new OwlOntologyChangeTranslatorVisitor();
    }

    @Test
    public void shouldVisitAddAxiomChange() {
        var addAxiom = new AddAxiom(ontology, axiom);
        var change = addAxiom.accept(visitor);
        assertThat(change, is(instanceOf(AddAxiomChange.class)));
        var addAxiomChange = (AddAxiomChange) change;
        assertThat(addAxiomChange.getOntologyId(), is(ontologyId));
        assertThat(addAxiomChange.getAxiom(), is(axiom));
    }

    @Test
    public void shouldVisitRemoveAxiomChange() {
        var removeAxiom = new RemoveAxiom(ontology, axiom);
        var change = removeAxiom.accept(visitor);
        assertThat(change, is(instanceOf(RemoveAxiomChange.class)));
        var removeAxiomChange = (RemoveAxiomChange) change;
        assertThat(removeAxiomChange.getOntologyId(), is(ontologyId));
        assertThat(removeAxiomChange.getAxiom(), is(axiom));
    }

    @Test
    public void shouldVisitAddOntologyAnnotationChange() {
        var addOntologyAnnotation = new AddOntologyAnnotation(ontology, ontologyAnnotation);
        var change = addOntologyAnnotation.accept(visitor);
        assertThat(change, is(instanceOf(AddOntologyAnnotationChange.class)));
        var addOntologyAnnotationChange = (AddOntologyAnnotationChange) change;
        assertThat(addOntologyAnnotationChange.getOntologyId(), is(ontologyId));
        assertThat(addOntologyAnnotationChange.getAnnotation(), is(ontologyAnnotation));
    }

    @Test
    public void shouldVisitRemoveOntologyAnnotationChange() {
        var removeOntologyAnnotation = new RemoveOntologyAnnotation(ontology, ontologyAnnotation);
        var change = removeOntologyAnnotation.accept(visitor);
        assertThat(change, is(instanceOf(RemoveOntologyAnnotationChange.class)));
        var removeOntologyAnnotationChange = (RemoveOntologyAnnotationChange) change;
        assertThat(removeOntologyAnnotationChange.getOntologyId(), is(ontologyId));
        assertThat(removeOntologyAnnotationChange.getAnnotation(), is(ontologyAnnotation));
    }

    @Test
    public void shouldVisitAddImportsChange() {
        var addImports = new AddImport(ontology, importsDecl);
        var change = addImports.accept(visitor);
        assertThat(change, is(instanceOf(AddImportChange.class)));
        var addImportsChange = (AddImportChange) change;
        assertThat(addImportsChange.getOntologyId(), is(ontologyId));
        assertThat(addImportsChange.getImportsDeclaration(), is(importsDecl));
    }

    @Test
    public void shouldVisitRemoveImportsChange() {
        var removeImports = new RemoveImport(ontology, importsDecl);
        var change = removeImports.accept(visitor);
        assertThat(change, is(instanceOf(RemoveImportChange.class)));
        var removeImportsChange = (RemoveImportChange) change;
        assertThat(removeImportsChange.getOntologyId(), is(ontologyId));
        assertThat(removeImportsChange.getImportsDeclaration(), is(importsDecl));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowUnsupportedOperationExceptionForSetOntologyId() {
        var setOntologyId = new SetOntologyID(ontology, mock(OWLOntologyID.class));
        setOntologyId.accept(visitor);
    }
}
