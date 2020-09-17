package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.mansyntax.render.OwlOntologyFacade;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxFrameRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.io.StringWriter;
import java.util.Collections;

import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-31
 */
public class OwlOntologyFacade_ManchesterSyntaxAxioms_TestCase {

    private ManchesterOWLSyntaxFrameRenderer frameRenderer;

    @Before
    public void setUp() throws Exception {
        var ontologyFacade = new OwlOntologyFacade(mock(OWLOntologyID.class),
                                                   mock(OntologyAnnotationsIndex.class),
                                                   mock(OntologySignatureIndex.class),
                                                   mock(OntologyAxiomsIndex.class),
                                                   mock(EntitiesInOntologySignatureByIriIndex.class),
                                                   mock(AnnotationAssertionAxiomsBySubjectIndex.class),
                                                   mock(OWLDataFactory.class),
                                                   mock(SubAnnotationPropertyAxiomsBySubPropertyIndex.class),
                                                   mock(AnnotationPropertyDomainAxiomsIndex.class),
                                                   mock(AnnotationPropertyRangeAxiomsIndex.class),
                                                   mock(SubClassOfAxiomsBySubClassIndex.class),
                                                   mock(AxiomsByReferenceIndex.class),
                                                   mock(EquivalentClassesAxiomsIndex.class),
                                                   mock(DisjointClassesAxiomsIndex.class),
                                                   mock(AxiomsByTypeIndex.class),
                                                   mock(SubObjectPropertyAxiomsBySubPropertyIndex.class),
                                                   mock(ObjectPropertyDomainAxiomsIndex.class),
                                                   mock(ObjectPropertyRangeAxiomsIndex.class),
                                                   mock(InverseObjectPropertyAxiomsIndex.class),
                                                   mock(EquivalentObjectPropertiesAxiomsIndex.class),
                                                   mock(DisjointObjectPropertiesAxiomsIndex.class),
                                                   mock(SubDataPropertyAxiomsBySubPropertyIndex.class),
                                                   mock(DataPropertyDomainAxiomsIndex.class),
                                                   mock(DataPropertyRangeAxiomsIndex.class),
                                                   mock(EquivalentDataPropertiesAxiomsIndex.class),
                                                   mock(DisjointDataPropertiesAxiomsIndex.class),
                                                   mock(ClassAssertionAxiomsByIndividualIndex.class),
                                                   mock(ClassAssertionAxiomsByClassIndex.class),
                                                   mock(DataPropertyAssertionAxiomsBySubjectIndex.class),
                                                   mock(ObjectPropertyAssertionAxiomsBySubjectIndex.class),
                                                   mock(SameIndividualAxiomsIndex.class),
                                                   mock(DifferentIndividualsAxiomsIndex.class));
        frameRenderer = new ManchesterOWLSyntaxFrameRenderer(Collections.singleton(ontologyFacade),
                                                             mock(StringWriter.class),
                                                             mock(ShortFormProvider.class));
    }

    @Test
    public void shouldRenderClassWithoutError() {
        frameRenderer.writeFrame(MockingUtils.mockOWLClass());
    }

    @Test
    public void shouldWriteObjectPropertyWithoutError() {
        frameRenderer.writeFrame(MockingUtils.mockOWLObjectProperty());
    }

    @Test
    public void shouldWriteDataPropertyWithoutError() {
        frameRenderer.writeFrame(MockingUtils.mockOWLDataProperty());
    }

    @Test
    public void shouldWriteAnnotationPropertyWithoutError() {
        frameRenderer.writeFrame(MockingUtils.mockOWLAnnotationProperty());
    }

    @Test
    public void shouldWriteNamedIndividualWithoutError() {
        frameRenderer.writeFrame(MockingUtils.mockOWLNamedIndividual());
    }

    @Test
    public void shouldWriteDatatypeWithoutError() {
        frameRenderer.writeFrame(MockingUtils.mockOWLDatatype());
    }
}
