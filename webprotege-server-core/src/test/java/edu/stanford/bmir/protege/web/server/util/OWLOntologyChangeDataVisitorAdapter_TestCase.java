package edu.stanford.bmir.protege.web.server.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
@RunWith(MockitoJUnitRunner.class)
public class OWLOntologyChangeDataVisitorAdapter_TestCase {

    @Spy
    private OWLOntologyChangeDataVisitorAdapter adapter = new OWLOntologyChangeDataVisitorAdapter();

    @Before
    public void setUp() {
    }

    @Test
    public void shouldVisitAddAxiomData() {
        var addAxiomData = new AddAxiomData(mock(OWLAxiom.class));
        addAxiomData.accept(adapter);
        verify(adapter, times(1)).visitAddAxiomData(addAxiomData);
    }

    @Test
    public void shouldVisitRemoveAxiomData() {
        var removeAxiomData = new RemoveAxiomData(mock(OWLAxiom.class));
        removeAxiomData.accept(adapter);
        verify(adapter, times(1)).visitRemoveAxiomData(removeAxiomData);
    }

    @Test
    public void shouldVisitAddImportData() {
        var addImportData = new AddImportData(mock(OWLImportsDeclaration.class));
        addImportData.accept(adapter);
        verify(adapter, times(1)).visitAddImportData(addImportData);
    }

    @Test
    public void shouldVisitRemoveImportData() {
        var removeImportData = new RemoveImportData(mock(OWLImportsDeclaration.class));
        removeImportData.accept(adapter);
        verify(adapter, times(1)).visitRemoveImportData(removeImportData);
    }

    @Test
    public void shouldVisitSetOntologyId() {
        var setOntologyId = new SetOntologyIDData(mock(OWLOntologyID.class));
        setOntologyId.accept(adapter);
        verify(adapter, times(1)).visitSetOntologyIDData(setOntologyId);
    }

    @Test
    public void shouldVisitAddOntologyAnnotationData() {
        var addOntologyAnnotationData = new AddOntologyAnnotationData(mock(OWLAnnotation.class));
        addOntologyAnnotationData.accept(adapter);
        verify(adapter, times(1)).visitAddOntologyAnnotationData(addOntologyAnnotationData);
    }

    @Test
    public void shouldVisitRemoveOntologyAnnotationData() {
        var removeOntologyAnnotationData = new RemoveOntologyAnnotationData(mock(OWLAnnotation.class));
        removeOntologyAnnotationData.accept(adapter);
        verify(adapter, times(1)).visitRemoveOntologyAnnotationData(removeOntologyAnnotationData);
    }
}
