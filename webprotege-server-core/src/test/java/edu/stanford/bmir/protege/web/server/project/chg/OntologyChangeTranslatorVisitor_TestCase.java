package edu.stanford.bmir.protege.web.server.project.chg;

import edu.stanford.bmir.protege.web.server.change.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyChangeTranslatorVisitor_TestCase {

    private OntologyChangeTranslatorVisitor visitor;

    @Mock
    private OWLOntologyManager ontologyManager;

    @Mock
    private OWLOntologyID ontologyId, otherOntologyId;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLAnnotation ontologyAnnotation;

    @Mock
    private OWLImportsDeclaration importsDeclaration;

    @Before
    public void setUp() {
        when(ontologyManager.getOntology(ontologyId))
                .thenReturn(ontology);

        visitor = new OntologyChangeTranslatorVisitor(ontologyManager);
    }

    @Test
    public void shouldVisitAddAxiomChangeWithKnownOntology() {
        var addAxiomChange = AddAxiomChange.of(ontologyId, axiom);
        var owlOntologyChange = visitor.visit(addAxiomChange);
        var addAxiomOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, AddAxiom.class);
        assertThat(addAxiomOwlOntologyChange.getOntology(), is(ontology));
        assertThat(addAxiomOwlOntologyChange.getAxiom(), is(axiom));
    }

    @Test(expected = UnknownOWLOntologyException.class)
    public void shouldVisitAddAxiomChangeWithUnknownOntology() {
        var addAxiomChange = AddAxiomChange.of(otherOntologyId, axiom);
        visitor.visit(addAxiomChange);
    }

    @Test
    public void shouldVisitRemoveAxiomChangeWithKnownOntology() {
        var removeAxiomChange = RemoveAxiomChange.of(ontologyId, axiom);
        var owlOntologyChange = visitor.visit(removeAxiomChange);
        var removeAxiomOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, RemoveAxiom.class);
        assertThat(removeAxiomOwlOntologyChange.getOntology(), is(ontology));
        assertThat(removeAxiomOwlOntologyChange.getAxiom(), is(axiom));
    }

    @Test(expected = UnknownOWLOntologyException.class)
    public void shouldVisitRemoveAxiomChangeWithUnknownOntology() {
        var removeAxiomChange = RemoveAxiomChange.of(otherOntologyId, axiom);
        visitor.visit(removeAxiomChange);
    }

    @Test
    public void shouldVisitAddOntologyAnnotationChangeWithKnownOntology() {
        var addOntologyAnnotationChange = AddOntologyAnnotationChange.of(ontologyId, ontologyAnnotation);
        var owlOntologyChange = visitor.visit(addOntologyAnnotationChange);
        var addOntologyAnnotationOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, AddOntologyAnnotation.class);
        assertThat(addOntologyAnnotationOwlOntologyChange.getOntology(), is(ontology));
        assertThat(addOntologyAnnotationOwlOntologyChange.getAnnotation(), is(ontologyAnnotation));
    }

    @Test(expected = UnknownOWLOntologyException.class)
    public void shouldVisitAddOntologyAnnotationChangeWithUnknownOntology() {
        var addOntologyAnnotationChange = AddOntologyAnnotationChange.of(otherOntologyId, ontologyAnnotation);
        visitor.visit(addOntologyAnnotationChange);
    }

    @Test
    public void shouldVisitRemoveOntologyAnnotationChangeWithKnownOntology() {
        var removeOntologyAnnotationChange = RemoveOntologyAnnotationChange.of(ontologyId, ontologyAnnotation);
        var owlOntologyChange = visitor.visit(removeOntologyAnnotationChange);
        var removeOntologyAnnotationOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, RemoveOntologyAnnotation.class);
        assertThat(removeOntologyAnnotationOwlOntologyChange.getOntology(), is(ontology));
        assertThat(removeOntologyAnnotationOwlOntologyChange.getAnnotation(), is(ontologyAnnotation));
    }
    
    

    @Test(expected = UnknownOWLOntologyException.class)
    public void shouldVisitRemoveOntologyAnnotationChangeWithUnknownOntology() {
        var removeOntologyAnnotationChange = RemoveOntologyAnnotationChange.of(otherOntologyId, ontologyAnnotation);
        visitor.visit(removeOntologyAnnotationChange);
    }



    @Test
    public void shouldVisitAddImportsChangeWithKnownOntology() {
        var addImportsChange = AddImportChange.of(ontologyId, importsDeclaration);
        var owlOntologyChange = visitor.visit(addImportsChange);
        var addImportsOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, AddImport.class);
        assertThat(addImportsOwlOntologyChange.getOntology(), is(ontology));
        assertThat(addImportsOwlOntologyChange.getImportDeclaration(), is(importsDeclaration));
    }

    @Test(expected = UnknownOWLOntologyException.class)
    public void shouldVisitAddImportsChangeWithUnknownOntology() {
        var addImportsChange = AddImportChange.of(otherOntologyId, importsDeclaration);
        visitor.visit(addImportsChange);
    }

    @Test
    public void shouldVisitRemoveImportsChangeWithKnownOntology() {
        var removeImportsChange = RemoveImportChange.of(ontologyId, importsDeclaration);
        var owlOntologyChange = visitor.visit(removeImportsChange);
        var removeImportsOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, RemoveImport.class);
        assertThat(removeImportsOwlOntologyChange.getOntology(), is(ontology));
        assertThat(removeImportsOwlOntologyChange.getImportDeclaration(), is(importsDeclaration));
    }

    @Test(expected = UnknownOWLOntologyException.class)
    public void shouldVisitRemoveImportsChangeWithUnknownOntology() {
        var removeImportsChange = RemoveImportChange.of(otherOntologyId, importsDeclaration);
        visitor.visit(removeImportsChange);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionForDefaultValue() {
        visitor.getDefaultReturnValue();
    }

    private <C extends OWLOntologyChange> C assertThatChangeIsOfClass(OWLOntologyChange owlOntologyChange, Class<C> cls) {
        assertThat(owlOntologyChange, is(instanceOf(cls)));
        return cls.cast(owlOntologyChange);
    }
}
