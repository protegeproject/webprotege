package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDatatypeImpl;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomsByEntityReferenceIndexImpl_TestCase {

    AxiomsByEntityReferenceIndexImpl impl;

    @Mock
    private OntologyDocumentId ontologyDocumentId;

    private OWLClass cls = createClass();

    private OWLObjectProperty objectProperty = createObjectProperty();

    private OWLDataProperty dataProperty = createDataProperty();

    private OWLAnnotationProperty annotationProperty = createAnnotationProperty();

    private OWLNamedIndividual namedIndividual = createIndividual();

    private OWLDatatype datatype = new OWLDatatypeImpl(mock(IRI.class));

    private OWLDeclarationAxiom clsDecl = Declaration(cls);

    private OWLDeclarationAxiom objectPropertyDecl = Declaration(objectProperty);

    private OWLDeclarationAxiom dataPropertyDecl = Declaration(dataProperty);

    private OWLDeclarationAxiom annotationPropertyDecl = Declaration(annotationProperty);

    private OWLDeclarationAxiom namedIndividualDecl = Declaration(namedIndividual);

    private OWLDeclarationAxiom datatypeDecl = Declaration(datatype);

    @Mock
    private OWLEntityProvider entityProvider;

    @Before
    public void setUp() {
        when(entityProvider.getOWLClass(cls.getIRI()))
                .thenReturn(cls);
        when(entityProvider.getOWLObjectProperty(objectProperty.getIRI()))
                .thenReturn(objectProperty);
        when(entityProvider.getOWLDataProperty(dataProperty.getIRI()))
                .thenReturn(dataProperty);
        when(entityProvider.getOWLAnnotationProperty(annotationProperty.getIRI()))
                .thenReturn(annotationProperty);
        when(entityProvider.getOWLNamedIndividual(namedIndividual.getIRI()))
                .thenReturn(namedIndividual);
        when(entityProvider.getOWLDatatype(datatype.getIRI()))
                .thenReturn(datatype);

        impl = new AxiomsByEntityReferenceIndexImpl(entityProvider);
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyDocumentId, clsDecl),
                                           AddAxiomChange.of(ontologyDocumentId, objectPropertyDecl),
                                           AddAxiomChange.of(ontologyDocumentId, dataPropertyDecl),
                                           AddAxiomChange.of(ontologyDocumentId, annotationPropertyDecl),
                                           AddAxiomChange.of(ontologyDocumentId, namedIndividualDecl),
                                           AddAxiomChange.of(ontologyDocumentId, datatypeDecl)
        ));
    }

    @Test
    public void shouldRetrieveAxiomByClassReference() {
        var axiomStream = impl.getReferencingAxioms(cls, ontologyDocumentId);
        var axioms = axiomStream.collect(toSet());
        assertThat(axioms, hasItem(clsDecl));
    }

    @Test
    public void shouldRetrieveAxiomByObjectPropertyReference() {
        var axiomStream = impl.getReferencingAxioms(objectProperty, ontologyDocumentId);
        var axioms = axiomStream.collect(toSet());
        assertThat(axioms, hasItem(objectPropertyDecl));
    }

    @Test
    public void shouldRetrieveAxiomByDataPropertyReference() {
        var axiomStream = impl.getReferencingAxioms(dataProperty, ontologyDocumentId);
        var axioms = axiomStream.collect(toSet());
        assertThat(axioms, hasItem(dataPropertyDecl));
    }

    @Test
    public void shouldRetrieveAxiomByAnnotationPropertyReference() {
        var axiomStream = impl.getReferencingAxioms(annotationProperty, ontologyDocumentId);
        var axioms = axiomStream.collect(toSet());
        assertThat(axioms, hasItem(annotationPropertyDecl));
    }


    @Test
    public void shouldRetrieveAxiomByDataypeReference() {
        var axiomStream = impl.getReferencingAxioms(datatype, ontologyDocumentId);
        var axioms = axiomStream.collect(toSet());
        assertThat(axioms, hasItem(datatypeDecl));
    }

    @Test
    public void shouldRetrieveAxiomByIndividualReference() {
        var axiomStream = impl.getReferencingAxioms(namedIndividual, ontologyDocumentId);
        var axioms = axiomStream.collect(toSet());
        assertThat(axioms, hasItem(namedIndividualDecl));
    }

    @Test
    public void shouldRetrieveEmptyStreamForUnknownOntology() {
        var axiomStream = impl.getReferencingAxioms(cls, mock(OntologyDocumentId.class));
        var axioms = axiomStream.collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullOntology() {
        impl.getReferencingAxioms(null, ontologyDocumentId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldNPEForNullOntologyId() {
        impl.getReferencingAxioms(cls, null);
    }

    @Test
    public void shouldGetClassInSignatureWithIri() {
        var signature = impl.getEntitiesInSignatureWithIri(cls.getIRI(), ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(cls));
    }

    @Test
    public void shouldGetObjectPropertyInSignatureWithIri() {
        var signature = impl.getEntitiesInSignatureWithIri(objectProperty.getIRI(), ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(objectProperty));
    }

    @Test
    public void shouldGetDataPropertyInSignatureWithIri() {
        var signature = impl.getEntitiesInSignatureWithIri(dataProperty.getIRI(), ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(dataProperty));
    }

    @Test
    public void shouldGetAnnotationPropertyInSignatureWithIri() {
        var signature = impl.getEntitiesInSignatureWithIri(annotationProperty.getIRI(), ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(annotationProperty));
    }

    @Test
    public void shouldGetIndividualInSignatureWithIri() {
        var signature = impl.getEntitiesInSignatureWithIri(namedIndividual.getIRI(), ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(namedIndividual));
    }


    @Test
    public void shouldGetDatatypeInSignatureWithIri() {
        var signature = impl.getEntitiesInSignatureWithIri(datatype.getIRI(), ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(datatype));
    }

    @Test
    public void shouldContainEntity_Class_InOntologyAxiomsSignature() {
        var inSignature = impl.containsEntityInOntologyAxiomsSignature(cls, ontologyDocumentId);
        assertThat(inSignature, is(true));
    }

    @Test
    public void shouldContainEntity_ObjectProperty_InOntologyAxiomsSignature() {
        var inSignature = impl.containsEntityInOntologyAxiomsSignature(objectProperty, ontologyDocumentId);
        assertThat(inSignature, is(true));
    }

    @Test
    public void shouldContainEntity_DataProperty_InOntologyAxiomsSignature() {
        var inSignature = impl.containsEntityInOntologyAxiomsSignature(dataProperty, ontologyDocumentId);
        assertThat(inSignature, is(true));
    }

    @Test
    public void shouldContainEntity_AnnotationProperty_InOntologyAxiomsSignature() {
        var inSignature = impl.containsEntityInOntologyAxiomsSignature(annotationProperty, ontologyDocumentId);
        assertThat(inSignature, is(true));
    }

    @Test
    public void shouldContainEntity_NamedIndividual_InOntologyAxiomsSignature() {
        var inSignature = impl.containsEntityInOntologyAxiomsSignature(namedIndividual, ontologyDocumentId);
        assertThat(inSignature, is(true));
    }

    @Test
    public void shouldContainEntity_Datatype_InOntologyAxiomsSignature() {
        var inSignature = impl.containsEntityInOntologyAxiomsSignature(datatype, ontologyDocumentId);
        assertThat(inSignature, is(true));
    }

    @Test
    public void should_getOntologyAxiomsSignature_ForClass() {
        var signature = impl.getOntologyAxiomsSignature(EntityType.CLASS, ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(cls));
    }

    @Test
    public void should_getOntologyAxiomsSignature_ForObjectProperty() {
        var signature = impl.getOntologyAxiomsSignature(EntityType.OBJECT_PROPERTY, ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(objectProperty));
    }

    @Test
    public void should_getOntologyAxiomsSignature_ForDataProperty() {
        var signature = impl.getOntologyAxiomsSignature(EntityType.DATA_PROPERTY, ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(dataProperty));
    }

    @Test
    public void should_getOntologyAxiomsSignature_ForAnnotationProperty() {
        var signature = impl.getOntologyAxiomsSignature(EntityType.ANNOTATION_PROPERTY, ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(annotationProperty));
    }

    @Test
    public void should_getOntologyAxiomsSignature_ForNamedIndivdual() {
        var signature = impl.getOntologyAxiomsSignature(EntityType.NAMED_INDIVIDUAL, ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(namedIndividual));
    }

    @Test
    public void should_getOntologyAxiomsSignature_ForDatatype() {
        var signature = impl.getOntologyAxiomsSignature(EntityType.DATATYPE, ontologyDocumentId).collect(toSet());
        assertThat(signature, contains(datatype));
    }

    @Test
    public void should_GetProjectAxiomsSignature_ForClasses() {
        var signature = impl.getProjectAxiomsSignature(EntityType.CLASS).collect(toSet());
        assertThat(signature, contains(cls));
    }

    @Test
    public void should_GetProjectAxiomsSignature_ForObjectProperties() {
        var signature = impl.getProjectAxiomsSignature(EntityType.OBJECT_PROPERTY).collect(toSet());
        assertThat(signature, contains(objectProperty));
    }

    @Test
    public void should_GetProjectAxiomsSignature_ForDataProperties() {
        var signature = impl.getProjectAxiomsSignature(EntityType.DATA_PROPERTY).collect(toSet());
        assertThat(signature, contains(dataProperty));
    }

    @Test
    public void should_GetProjectAxiomsSignature_ForAnnotationProperties() {
        var signature = impl.getProjectAxiomsSignature(EntityType.ANNOTATION_PROPERTY).collect(toSet());
        assertThat(signature, contains(annotationProperty));
    }

    @Test
    public void should_GetProjectAxiomsSignature_ForDatatypes() {
        var signature = impl.getProjectAxiomsSignature(EntityType.DATATYPE).collect(toSet());
        assertThat(signature, contains(datatype));
    }

    @Test
    public void should_GetProjectAxiomsSignature_ForIndividuals() {
        var signature = impl.getProjectAxiomsSignature(EntityType.NAMED_INDIVIDUAL).collect(toSet());
        assertThat(signature, contains(namedIndividual));
    }

    @Test
    public void shouldRemoveClassAxiom() {
        impl.applyChanges(ImmutableList.of(RemoveAxiomChange.of(ontologyDocumentId, clsDecl)));
        assertThat(impl.getReferencingAxioms(cls, ontologyDocumentId).count(), is(0L));
    }
}
