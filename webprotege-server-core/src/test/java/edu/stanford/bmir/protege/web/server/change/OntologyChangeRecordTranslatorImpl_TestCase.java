package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.index.OntologyIndex;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyChangeRecordTranslatorImpl_TestCase {

    private OntologyChangeRecordTranslatorImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLAnnotation annotation;

    @Mock
    private OWLImportsDeclaration importsDeclaration;

    @Mock
    private OWLOntologyID otherOntologyId;

    @Before
    public void setUp() {
        impl = new OntologyChangeRecordTranslatorImpl(ontologyIndex);
        when(ontologyIndex.getOntology(ontologyId))
                .thenReturn(Optional.of(ontology));
        when(ontology.getOntologyID())
                .thenReturn(ontologyId);
    }

    @Test
    public void shouldTranslateAddAxiom() {
        var change = impl.getOntologyChange(new OWLOntologyChangeRecord(ontologyId, new AddAxiomData(axiom)));
        assertThat(change.getOntology(), is(ontology));
        assertThat(change.getAxiom(), is(axiom));
        assertThat(change, is(instanceOf(AddAxiom.class)));
    }

    @Test
    public void shouldTranslateRemoveAxiom() {
        var change = impl.getOntologyChange(new OWLOntologyChangeRecord(ontologyId, new RemoveAxiomData(axiom)));
        assertThat(change.getOntology(), is(ontology));
        assertThat(change.getAxiom(), is(axiom));
        assertThat(change, is(instanceOf(RemoveAxiom.class)));
    }

    @Test
    public void shouldTranslateAddOntologyAnnotation() {
        var change = impl.getOntologyChange(new OWLOntologyChangeRecord(ontologyId, new AddOntologyAnnotationData(annotation)));
        assertThat(change.getOntology(), is(ontology));
        assertThat(change, is(instanceOf(AddOntologyAnnotation.class)));
        assertThat(((AddOntologyAnnotation) change).getAnnotation(), is(annotation));
    }

    @Test
    public void shouldTranslateRemoveOntologyAnnotation() {
        var change = impl.getOntologyChange(new OWLOntologyChangeRecord(ontologyId, new RemoveOntologyAnnotationData(annotation)));
        assertThat(change.getOntology(), is(ontology));
        assertThat(change, is(instanceOf(RemoveOntologyAnnotation.class)));
        assertThat(((RemoveOntologyAnnotation) change).getAnnotation(), is(annotation));
    }

    @Test
    public void shouldTranslateAddImport() {
        var change = impl.getOntologyChange(new OWLOntologyChangeRecord(ontologyId, new AddImportData(importsDeclaration)));
        assertThat(change.getOntology(), is(ontology));
        assertThat(change, is(instanceOf(AddImport.class)));
        assertThat(((AddImport) change).getImportDeclaration(), is(importsDeclaration));
    }

    @Test
    public void shouldTranslateRemoveImport() {
        var change = impl.getOntologyChange(new OWLOntologyChangeRecord(ontologyId, new RemoveImportData(importsDeclaration)));
        assertThat(change.getOntology(), is(ontology));
        assertThat(change, is(instanceOf(RemoveImport.class)));
        assertThat(((RemoveImport) change).getImportDeclaration(), is(importsDeclaration));
    }

    @Test
    public void shouldTranslateSetOntologyID() {
        var change = impl.getOntologyChange(new OWLOntologyChangeRecord(ontologyId, new SetOntologyIDData(otherOntologyId)));
        assertThat(change.getOntology(), is(ontology));
        assertThat(change, is(instanceOf(SetOntologyID.class)));
        assertThat(((SetOntologyID) change).getNewOntologyID(), is(otherOntologyId));
    }
}
