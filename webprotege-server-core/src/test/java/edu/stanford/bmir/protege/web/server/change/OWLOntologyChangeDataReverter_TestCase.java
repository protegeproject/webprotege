
package edu.stanford.bmir.protege.web.server.change;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class OWLOntologyChangeDataReverter_TestCase {

    private OWLOntologyChangeDataReverter reverter;

    private OWLOntologyID owlOntologyID = new OWLOntologyID(), newOntologyId = new OWLOntologyID();

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLImportsDeclaration importDeclaration;

    @Mock
    private OWLAnnotation annotation;


    @Before
    public void setUp() {
        reverter = new OWLOntologyChangeDataReverter();
    }

    @Test
    public void shouldRevertAddAxiom() {
        OWLOntologyChangeData reverse = reverter.getRevertingChange(new OWLOntologyChangeRecord(owlOntologyID, new AddAxiomData(axiom)));
        assertThat(reverse, is(instanceOf(RemoveAxiomData.class)));
        RemoveAxiomData reverseData = (RemoveAxiomData) reverse;
        assertThat(reverseData.getAxiom(), is(axiom));
    }
    
    @Test
    public void shouldRevertRemoveAxiom() {
        OWLOntologyChangeData reverse = reverter.getRevertingChange(new OWLOntologyChangeRecord(owlOntologyID, new RemoveAxiomData(axiom)));
        assertThat(reverse, is(instanceOf(AddAxiomData.class)));
        AddAxiomData addAxiomData = (AddAxiomData) reverse;
        assertThat(addAxiomData.getAxiom(), is(axiom));
    }
    
    
    @Test
    public void shouldRevertAddImport() {
        OWLOntologyChangeData reverse = reverter.getRevertingChange(new OWLOntologyChangeRecord(owlOntologyID, new AddImportData(importDeclaration)));
        assertThat(reverse, is(instanceOf(RemoveImportData.class)));
        RemoveImportData reverseData = (RemoveImportData) reverse;
        assertThat(reverseData.getDeclaration(), is(importDeclaration));
    }
    
    @Test
    public void shouldRevertRemoveImport() {
        OWLOntologyChangeData reverse = reverter.getRevertingChange(new OWLOntologyChangeRecord(owlOntologyID, new RemoveImportData(importDeclaration)));
        assertThat(reverse, is(instanceOf(AddImportData.class)));
        AddImportData addImportData = (AddImportData) reverse;
        assertThat(addImportData.getDeclaration(), is(importDeclaration));
    }


    @Test
    public void shouldRevertAddOntologyAnnotation() {
        OWLOntologyChangeData reverse = reverter.getRevertingChange(new OWLOntologyChangeRecord(owlOntologyID, new AddOntologyAnnotationData(annotation)));
        assertThat(reverse, is(instanceOf(RemoveOntologyAnnotationData.class)));
        RemoveOntologyAnnotationData reverseData = (RemoveOntologyAnnotationData) reverse;
        assertThat(reverseData.getAnnotation(), is(annotation));
    }

    @Test
    public void shouldRevertRemoveOntologyAnnotation() {
        OWLOntologyChangeData reverse = reverter.getRevertingChange(new OWLOntologyChangeRecord(owlOntologyID, new RemoveOntologyAnnotationData(annotation)));
        assertThat(reverse, is(instanceOf(AddOntologyAnnotationData.class)));
        AddOntologyAnnotationData addOntologyAnnotationData = (AddOntologyAnnotationData) reverse;
        assertThat(addOntologyAnnotationData.getAnnotation(), is(annotation));
    }

    @Test
    public void shouldRevertSetOntologyID() {
        OWLOntologyChangeData reverse = reverter.getRevertingChange(new OWLOntologyChangeRecord(owlOntologyID, new SetOntologyIDData(newOntologyId)));
        assertThat(reverse, is(instanceOf(SetOntologyIDData.class)));
        SetOntologyIDData reverseData = (SetOntologyIDData) reverse;
        assertThat(reverseData.getNewId(), is(owlOntologyID));
    }
}
