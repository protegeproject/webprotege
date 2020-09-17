package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.server.index.*;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.AxiomAnnotations;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.model.parameters.Navigation;
import org.semanticweb.owlapi.util.OWLAxiomSearchFilter;

import javax.annotation.Nonnull;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-31
 *
 * A facade to the indexes for a project through the eyes of {@link OWLOntology}.  The
 * facade only supports methods for rendering manchester syntax.  Consequently, some methods
 * are not supported.
 */
public class OwlOntologyFacade implements OWLOntology {

    private static final String METHOD_NOT_AVAILABLE_WITH_OWL_ONTOLOGY_FACADE = "Method not available with OwlOntologyFacade";

    @Nonnull
    private final OWLOntologyID ontologyID;

    @Nonnull
    private final OntologyAnnotationsIndex ontologyAnnotationsIndex;

    @Nonnull
    private final OntologySignatureIndex ontologySignatureIndex;

    @Nonnull
    private final OntologyAxiomsIndex ontologyAxiomsIndex;
    
    @Nonnull
    private final EntitiesInOntologySignatureByIriIndex entitiesInOntologySignatureByIriIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @AutoFactory
    public OwlOntologyFacade(@Nonnull OWLOntologyID ontologyID,
                             @Provided @Nonnull OntologyAnnotationsIndex ontologyAnnotationsIndex,
                             @Provided @Nonnull OntologySignatureIndex ontologySignatureIndex,
                             @Provided @Nonnull OntologyAxiomsIndex ontologyAxiomsIndex,
                             @Provided @Nonnull EntitiesInOntologySignatureByIriIndex entitiesInOntologySignatureByIriIndex,
                             @Provided @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex,
                             @Provided @Nonnull OWLDataFactory dataFactory,
                             @Provided @Nonnull SubAnnotationPropertyAxiomsBySubPropertyIndex subAnnotationPropertyAxiomsBySubPropertyIndex,
                             @Provided @Nonnull AnnotationPropertyDomainAxiomsIndex annotationPropertyDomainAxiomsIndex,
                             @Provided @Nonnull AnnotationPropertyRangeAxiomsIndex annotationPropertyRangeAxiomsIndex,
                             @Provided @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex,
                             @Provided @Nonnull AxiomsByReferenceIndex axiomsByReferenceIndex,
                             @Provided @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex,
                             @Provided @Nonnull DisjointClassesAxiomsIndex disjointClassesAxiomsIndex,
                             @Provided @Nonnull AxiomsByTypeIndex axiomsByTypeIndex,
                             @Provided @Nonnull SubObjectPropertyAxiomsBySubPropertyIndex subObjectPropertyAxiomsBySubPropertyIndex,
                             @Provided @Nonnull ObjectPropertyDomainAxiomsIndex objectPropertyDomainAxiomsIndex,
                             @Provided @Nonnull ObjectPropertyRangeAxiomsIndex objectPropertyRangeAxiomsIndex,
                             @Provided @Nonnull InverseObjectPropertyAxiomsIndex inverseObjectPropertyAxiomsIndex,
                             @Provided @Nonnull EquivalentObjectPropertiesAxiomsIndex equivalentObjectPropertiesAxiomsIndex,
                             @Provided @Nonnull DisjointObjectPropertiesAxiomsIndex disjointObjectPropertiesAxiomsIndex,
                             @Provided @Nonnull SubDataPropertyAxiomsBySubPropertyIndex subDataPropertyAxiomsBySubPropertyIndex,
                             @Provided @Nonnull DataPropertyDomainAxiomsIndex dataPropertyDomainAxiomsIndex,
                             @Provided @Nonnull DataPropertyRangeAxiomsIndex dataPropertyRangeAxiomsIndex,
                             @Provided @Nonnull EquivalentDataPropertiesAxiomsIndex equivalentDataPropertiesAxiomsIndex,
                             @Provided @Nonnull DisjointDataPropertiesAxiomsIndex disjointDataPropertiesAxiomsIndex,
                             @Provided @Nonnull ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex,
                             @Provided @Nonnull ClassAssertionAxiomsByClassIndex classAssertionAxiomsByClassIndex,
                             @Provided @Nonnull DataPropertyAssertionAxiomsBySubjectIndex dataPropertyAssertionAxiomsBySubjectIndex,
                             @Provided @Nonnull ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertionAxiomsBySubjectIndex,
                             @Provided @Nonnull SameIndividualAxiomsIndex sameIndividualAxiomsIndex,
                             @Provided @Nonnull DifferentIndividualsAxiomsIndex differentIndividualsAxiomsIndex) {
        this.ontologyID = ontologyID;
        this.ontologyAnnotationsIndex = ontologyAnnotationsIndex;
        this.ontologySignatureIndex = ontologySignatureIndex;
        this.ontologyAxiomsIndex = ontologyAxiomsIndex;
        this.entitiesInOntologySignatureByIriIndex = entitiesInOntologySignatureByIriIndex;
        this.annotationAssertionAxiomsBySubjectIndex = annotationAssertionAxiomsBySubjectIndex;
        this.dataFactory = dataFactory;
        this.subAnnotationPropertyAxiomsBySubPropertyIndex = subAnnotationPropertyAxiomsBySubPropertyIndex;
        this.annotationPropertyDomainAxiomsIndex = annotationPropertyDomainAxiomsIndex;
        this.annotationPropertyRangeAxiomsIndex = annotationPropertyRangeAxiomsIndex;
        this.subClassOfAxiomsBySubClassIndex = subClassOfAxiomsBySubClassIndex;
        this.axiomsByReferenceIndex = axiomsByReferenceIndex;
        this.equivalentClassesAxiomsIndex = equivalentClassesAxiomsIndex;
        this.disjointClassesAxiomsIndex = disjointClassesAxiomsIndex;
        this.axiomsByTypeIndex = axiomsByTypeIndex;
        this.subObjectPropertyAxiomsBySubPropertyIndex = subObjectPropertyAxiomsBySubPropertyIndex;
        this.objectPropertyDomainAxiomsIndex = objectPropertyDomainAxiomsIndex;
        this.objectPropertyRangeAxiomsIndex = objectPropertyRangeAxiomsIndex;
        this.inverseObjectPropertyAxiomsIndex = inverseObjectPropertyAxiomsIndex;
        this.equivalentObjectPropertiesAxiomsIndex = equivalentObjectPropertiesAxiomsIndex;
        this.disjointObjectPropertiesAxiomsIndex = disjointObjectPropertiesAxiomsIndex;
        this.subDataPropertyAxiomsBySubPropertyIndex = subDataPropertyAxiomsBySubPropertyIndex;
        this.dataPropertyDomainAxiomsIndex = dataPropertyDomainAxiomsIndex;
        this.dataPropertyRangeAxiomsIndex = dataPropertyRangeAxiomsIndex;
        this.equivalentDataPropertiesAxiomsIndex = equivalentDataPropertiesAxiomsIndex;
        this.disjointDataPropertiesAxiomsIndex = disjointDataPropertiesAxiomsIndex;
        this.classAssertionAxiomsByIndividualIndex = classAssertionAxiomsByIndividualIndex;
        this.classAssertionAxiomsByClassIndex = classAssertionAxiomsByClassIndex;
        this.dataPropertyAssertionAxiomsBySubjectIndex = dataPropertyAssertionAxiomsBySubjectIndex;
        this.objectPropertyAssertionAxiomsBySubjectIndex = objectPropertyAssertionAxiomsBySubjectIndex;
        this.sameIndividualAxiomsIndex = sameIndividualAxiomsIndex;
        this.differentIndividualsAxiomsIndex = differentIndividualsAxiomsIndex;
    }

    @Override
    public void accept(@Nonnull OWLNamedObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Nonnull
    @Override
    public <O> O accept(@Nonnull OWLNamedObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public OWLOntologyManager getOWLOntologyManager() {
        throw new RuntimeException("Cannot retrieve OWLOntologyManager for OwlOntologyFacade");
    }

    @Override
    public void setOWLOntologyManager(OWLOntologyManager manager) {
        throw new RuntimeException("Cannot set OWLOntologyManager for OwlOntologyFacade");
    }

    @Nonnull
    @Override
    public OWLOntologyID getOntologyID() {
        return ontologyID;
    }

    @Override
    public boolean isAnonymous() {
        return ontologyID.isAnonymous();
    }

    @Nonnull
    @Override
    public Set<OWLAnnotation> getAnnotations() {
        return ontologyAnnotationsIndex.getOntologyAnnotations(ontologyID)
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<IRI> getDirectImportsDocuments() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLOntology> getDirectImports() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLOntology> getImports() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLOntology> getImportsClosure() {
        return Collections.singleton(this);
    }

    @Nonnull
    @Override
    public Set<OWLImportsDeclaration> getImportsDeclarations() {
        return Collections.emptySet();
    }

    @Override
    public boolean isEmpty() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getTBoxAxioms(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getABoxAxioms(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getRBoxAxioms(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    public static RuntimeException notAvailableException() {
        return new RuntimeException(METHOD_NOT_AVAILABLE_WITH_OWL_ONTOLOGY_FACADE);
    }

    @Nonnull
    @Override
    public Set<OWLClassAxiom> getGeneralClassAxioms() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLEntity> getSignature() {
        return ontologySignatureIndex.getEntitiesInSignature(ontologyID)
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<OWLEntity> getSignature(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean isDeclared(@Nonnull OWLEntity owlEntity) {
        var declAx = dataFactory.getOWLDeclarationAxiom(owlEntity);
        return ontologyAxiomsIndex.containsAxiomIgnoreAnnotations(declAx, ontologyID);
    }

    @Override
    public boolean isDeclared(@Nonnull OWLEntity owlEntity, @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public void saveOntology() {
        throw notAvailableException();
    }

    @Override
    public void saveOntology(@Nonnull IRI documentIRI) {
        throw notAvailableException();
    }

    @Override
    public void saveOntology(@Nonnull OutputStream outputStream) {
        throw notAvailableException();
    }

    @Override
    public void saveOntology(@Nonnull OWLDocumentFormat ontologyFormat) {
        throw notAvailableException();
    }

    @Override
    public void saveOntology(@Nonnull OWLDocumentFormat ontologyFormat,
                             @Nonnull IRI documentIRI) {
        throw notAvailableException();
    }

    @Override
    public void saveOntology(@Nonnull OWLDocumentFormat ontologyFormat,
                             @Nonnull OutputStream outputStream) {
        throw notAvailableException();
    }

    @Override
    public void saveOntology(@Nonnull OWLOntologyDocumentTarget documentTarget) {
        throw notAvailableException();
    }

    @Override
    public void saveOntology(@Nonnull OWLDocumentFormat ontologyFormat,
                             @Nonnull OWLOntologyDocumentTarget documentTarget) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLEntity> getEntitiesInSignature(@Nonnull IRI entityIRI) {
        return entitiesInOntologySignatureByIriIndex.getEntitiesInSignature(entityIRI, ontologyID)
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxioms(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public int getAxiomCount(@Nonnull Imports includeImportsClosure) {
        return 0;
    }

    @Nonnull
    @Override
    public Set<OWLLogicalAxiom> getLogicalAxioms(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public int getLogicalAxiomCount(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public <T extends OWLAxiom> Set<T> getAxioms(@Nonnull AxiomType<T> axiomType,
                                                 @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public <T extends OWLAxiom> int getAxiomCount(@Nonnull AxiomType<T> axiomType,
                                                  @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsAxiom(@Nonnull OWLAxiom axiom,
                                 @Nonnull Imports includeImportsClosure,
                                 @Nonnull AxiomAnnotations ignoreAnnotations) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxiomsIgnoreAnnotations(@Nonnull OWLAxiom axiom, @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getReferencingAxioms(@Nonnull OWLPrimitive owlEntity, @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLClassAxiom> getAxioms(@Nonnull OWLClass cls, @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLObjectPropertyAxiom> getAxioms(@Nonnull OWLObjectPropertyExpression property,
                                                 @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDataPropertyAxiom> getAxioms(@Nonnull OWLDataProperty property,
                                               @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLIndividualAxiom> getAxioms(@Nonnull OWLIndividual individual,
                                             @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAnnotationAxiom> getAxioms(@Nonnull OWLAnnotationProperty property,
                                             @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDatatypeDefinitionAxiom> getAxioms(@Nonnull OWLDatatype datatype,
                                                     @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxioms(boolean b) {
        throw notAvailableException();
    }

    @Override
    public int getAxiomCount(boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLLogicalAxiom> getLogicalAxioms(boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public int getLogicalAxiomCount(boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public <T extends OWLAxiom> Set<T> getAxioms(@Nonnull AxiomType<T> axiomType, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public <T extends OWLAxiom> int getAxiomCount(@Nonnull AxiomType<T> axiomType, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsAxiom(@Nonnull OWLAxiom axiom, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsAxiomIgnoreAnnotations(@Nonnull OWLAxiom axiom, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxiomsIgnoreAnnotations(@Nonnull OWLAxiom axiom, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getReferencingAxioms(@Nonnull OWLPrimitive owlEntity, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLClassAxiom> getAxioms(@Nonnull OWLClass cls, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLObjectPropertyAxiom> getAxioms(@Nonnull OWLObjectPropertyExpression property,
                                                 boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDataPropertyAxiom> getAxioms(@Nonnull OWLDataProperty property, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLIndividualAxiom> getAxioms(@Nonnull OWLIndividual individual, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAnnotationAxiom> getAxioms(@Nonnull OWLAnnotationProperty property, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDatatypeDefinitionAxiom> getAxioms(@Nonnull OWLDatatype datatype, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public int getAxiomCount() {
        return 0;
    }

    @Override
    public int getLogicalAxiomCount() {
        return 0;
    }

    @Override
    public <T extends OWLAxiom> int getAxiomCount(@Nonnull AxiomType<T> axiomType) {
        return 0;
    }

    @Override
    public boolean containsAxiomIgnoreAnnotations(@Nonnull OWLAxiom axiom) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxiomsIgnoreAnnotations(@Nonnull OWLAxiom axiom) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getReferencingAxioms(@Nonnull OWLPrimitive owlEntity) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLClassAxiom> getAxioms(@Nonnull OWLClass cls) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLObjectPropertyAxiom> getAxioms(@Nonnull OWLObjectPropertyExpression property) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDataPropertyAxiom> getAxioms(@Nonnull OWLDataProperty property) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLIndividualAxiom> getAxioms(@Nonnull OWLIndividual individual) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAnnotationAxiom> getAxioms(@Nonnull OWLAnnotationProperty property) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDatatypeDefinitionAxiom> getAxioms(@Nonnull OWLDatatype datatype) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxioms() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public <T extends OWLAxiom> Set<T> getAxioms(@Nonnull AxiomType<T> axiomType) {
        return axiomsByTypeIndex.getAxiomsByType(axiomType, ontologyID)
                .collect(toSet());
    }

    @Override
    public boolean containsAxiom(@Nonnull OWLAxiom axiom) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLLogicalAxiom> getLogicalAxioms() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public <T extends OWLAxiom> Set<T> getAxioms(@Nonnull Class<T> type,
                                                 @Nonnull OWLObject entity,
                                                 @Nonnull Imports includeImports,
                                                 @Nonnull Navigation forSubPosition) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public <T extends OWLAxiom> Collection<T> filterAxioms(@Nonnull OWLAxiomSearchFilter filter,
                                                           @Nonnull Object key,
                                                           @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean contains(@Nonnull OWLAxiomSearchFilter filter,
                            @Nonnull Object key,
                            @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public <T extends OWLAxiom> Set<T> getAxioms(@Nonnull Class<T> type,
                                                 @Nonnull Class<? extends OWLObject> explicitClass,
                                                 @Nonnull OWLObject entity,
                                                 @Nonnull Imports includeImports,
                                                 @Nonnull Navigation forSubPosition) {
        throw notAvailableException();
    }

    @Nonnull
    private final SubAnnotationPropertyAxiomsBySubPropertyIndex subAnnotationPropertyAxiomsBySubPropertyIndex;

    @Nonnull
    @Override
    public Set<OWLSubAnnotationPropertyOfAxiom> getSubAnnotationPropertyOfAxioms(@Nonnull OWLAnnotationProperty subProperty) {
        return subAnnotationPropertyAxiomsBySubPropertyIndex.getSubPropertyOfAxioms(subProperty, ontologyID)
        .collect(toSet());
    }

    @Nonnull
    private final AnnotationPropertyDomainAxiomsIndex annotationPropertyDomainAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLAnnotationPropertyDomainAxiom> getAnnotationPropertyDomainAxioms(@Nonnull OWLAnnotationProperty property) {
        return annotationPropertyDomainAxiomsIndex.getAnnotationPropertyDomainAxioms(property, ontologyID)
                .collect(toSet());
    }

    private final AnnotationPropertyRangeAxiomsIndex annotationPropertyRangeAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLAnnotationPropertyRangeAxiom> getAnnotationPropertyRangeAxioms(@Nonnull OWLAnnotationProperty property) {
        return annotationPropertyRangeAxiomsIndex.getAnnotationPropertyRangeAxioms(property, ontologyID)
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<OWLDeclarationAxiom> getDeclarationAxioms(@Nonnull OWLEntity subject) {
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.DECLARATION, ontologyID)
                .filter(ax -> ax.getEntity().equals(subject))
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull OWLAnnotationSubject entity) {
        return annotationAssertionAxiomsBySubjectIndex.getAxiomsForSubject(entity,
                                                                           ontologyID)
                .collect(toSet());
    }

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex;

    @Nonnull
    @Override
    public Set<OWLSubClassOfAxiom> getSubClassAxiomsForSubClass(@Nonnull OWLClass cls) {
        return subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(cls, ontologyID)
                .collect(toSet());
    }

    private final AxiomsByReferenceIndex axiomsByReferenceIndex;

    @Nonnull
    @Override
    public Set<OWLSubClassOfAxiom> getSubClassAxiomsForSuperClass(@Nonnull OWLClass cls) {
        // Don't put this into the editor view.  Could result in crashes on the front-end
        // for large ontologies
        return Collections.emptySet();
    }

    @Nonnull
    private final EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLEquivalentClassesAxiom> getEquivalentClassesAxioms(@Nonnull OWLClass cls) {
        return equivalentClassesAxiomsIndex.getEquivalentClassesAxioms(cls, ontologyID)
                .collect(toSet());
    }

    @Nonnull
    private final DisjointClassesAxiomsIndex disjointClassesAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLDisjointClassesAxiom> getDisjointClassesAxioms(@Nonnull OWLClass cls) {
        return disjointClassesAxiomsIndex.getDisjointClassesAxioms(cls, ontologyID)
                                  .collect(toSet());
    }


    @Nonnull
    @Override
    public Set<OWLDisjointUnionAxiom> getDisjointUnionAxioms(@Nonnull OWLClass owlClass) {
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.DISJOINT_UNION, ontologyID)
                .filter(ax -> ax.getOWLClass().equals(owlClass))
                .collect(toSet());
    }

    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Nonnull
    @Override
    public Set<OWLHasKeyAxiom> getHasKeyAxioms(@Nonnull OWLClass cls) {
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.HAS_KEY,
                                                 ontologyID)
                .filter(ax -> ax.getClassExpression().equals(cls))
                .collect(toSet());
    }


    @Nonnull
    private final SubObjectPropertyAxiomsBySubPropertyIndex subObjectPropertyAxiomsBySubPropertyIndex;

    @Nonnull
    @Override
    public Set<OWLSubObjectPropertyOfAxiom> getObjectSubPropertyAxiomsForSubProperty(@Nonnull OWLObjectPropertyExpression subProperty) {
        if(subProperty.isAnonymous()) {
            return Collections.emptySet();
        }
        return subObjectPropertyAxiomsBySubPropertyIndex.getSubPropertyOfAxioms(subProperty.asOWLObjectProperty(),
                                                                                ontologyID)
                .collect(toSet());
    }


    @Nonnull
    @Override
    public Set<OWLSubObjectPropertyOfAxiom> getObjectSubPropertyAxiomsForSuperProperty(@Nonnull OWLObjectPropertyExpression superProperty) {
        // Don't display sub properties in the editor
        return Collections.emptySet();
    }

    @Nonnull
    private final ObjectPropertyDomainAxiomsIndex objectPropertyDomainAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLObjectPropertyDomainAxiom> getObjectPropertyDomainAxioms(@Nonnull OWLObjectPropertyExpression property) {
        if(property.isAnonymous()) {
            return Collections.emptySet();
        }
        return objectPropertyDomainAxiomsIndex.getObjectPropertyDomainAxioms(property.asOWLObjectProperty(),
                                                                      ontologyID)
                                       .collect(toSet());
    }

    @Nonnull
    private final ObjectPropertyRangeAxiomsIndex objectPropertyRangeAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLObjectPropertyRangeAxiom> getObjectPropertyRangeAxioms(@Nonnull OWLObjectPropertyExpression property) {
        if(property.isAnonymous()) {
            return Collections.emptySet();
        }
        return objectPropertyRangeAxiomsIndex.getObjectPropertyRangeAxioms(property.asOWLObjectProperty(),
                                                                    ontologyID)
                .collect(toSet());
    }

    @Nonnull
    private final InverseObjectPropertyAxiomsIndex inverseObjectPropertyAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLInverseObjectPropertiesAxiom> getInverseObjectPropertyAxioms(@Nonnull OWLObjectPropertyExpression property) {
        if(property.isAnonymous()) {
            return Collections.emptySet();
        }
        return inverseObjectPropertyAxiomsIndex.getInverseObjectPropertyAxioms(property.asOWLObjectProperty(),
                                                                        ontologyID)
                .collect(toSet());
    }

    @Nonnull
    private final EquivalentObjectPropertiesAxiomsIndex equivalentObjectPropertiesAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLEquivalentObjectPropertiesAxiom> getEquivalentObjectPropertiesAxioms(@Nonnull OWLObjectPropertyExpression property) {
        if(property.isAnonymous()) {
            return Collections.emptySet();
        }
        return equivalentObjectPropertiesAxiomsIndex.getEquivalentObjectPropertiesAxioms(property.asOWLObjectProperty(),
                                                                                         ontologyID)
                .collect(Collectors.toSet());
    }

    @Nonnull
    private final DisjointObjectPropertiesAxiomsIndex disjointObjectPropertiesAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLDisjointObjectPropertiesAxiom> getDisjointObjectPropertiesAxioms(@Nonnull OWLObjectPropertyExpression property) {
        return disjointObjectPropertiesAxiomsIndex.getDisjointObjectPropertiesAxioms(property.asOWLObjectProperty(),
                                                                                     ontologyID)
                .collect(toSet());
    }


    private <A> Set<A> getPropertyCharacteristicAxioms(@Nonnull OWLObjectPropertyExpression property,
                                                       @Nonnull Class<A> cls) {
        if(property.isAnonymous()) {
            return Collections.emptySet();
        }
        return axiomsByReferenceIndex.getReferencingAxioms(Collections.singleton(property.asOWLObjectProperty()),
                                                           ontologyID)
                .filter(cls::isInstance)
                .map(cls::cast)
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<OWLFunctionalObjectPropertyAxiom> getFunctionalObjectPropertyAxioms(@Nonnull OWLObjectPropertyExpression property) {
        return getPropertyCharacteristicAxioms(property, OWLFunctionalObjectPropertyAxiom.class);
    }

    @Nonnull
    @Override
    public Set<OWLInverseFunctionalObjectPropertyAxiom> getInverseFunctionalObjectPropertyAxioms(@Nonnull OWLObjectPropertyExpression property) {
        return getPropertyCharacteristicAxioms(property, OWLInverseFunctionalObjectPropertyAxiom.class);
    }

    @Nonnull
    @Override
    public Set<OWLSymmetricObjectPropertyAxiom> getSymmetricObjectPropertyAxioms(@Nonnull OWLObjectPropertyExpression property) {
        return getPropertyCharacteristicAxioms(property, OWLSymmetricObjectPropertyAxiom.class);
    }

    @Nonnull
    @Override
    public Set<OWLAsymmetricObjectPropertyAxiom> getAsymmetricObjectPropertyAxioms(@Nonnull OWLObjectPropertyExpression property) {
        return getPropertyCharacteristicAxioms(property, OWLAsymmetricObjectPropertyAxiom.class);
    }

    @Nonnull
    @Override
    public Set<OWLReflexiveObjectPropertyAxiom> getReflexiveObjectPropertyAxioms(@Nonnull OWLObjectPropertyExpression property) {
        return getPropertyCharacteristicAxioms(property, OWLReflexiveObjectPropertyAxiom.class);
    }

    @Nonnull
    @Override
    public Set<OWLIrreflexiveObjectPropertyAxiom> getIrreflexiveObjectPropertyAxioms(@Nonnull OWLObjectPropertyExpression property) {
        return getPropertyCharacteristicAxioms(property, OWLIrreflexiveObjectPropertyAxiom.class);
    }

    @Nonnull
    @Override
    public Set<OWLTransitiveObjectPropertyAxiom> getTransitiveObjectPropertyAxioms(@Nonnull OWLObjectPropertyExpression property) {
        return getPropertyCharacteristicAxioms(property, OWLTransitiveObjectPropertyAxiom.class);
    }

    @Nonnull
    private final SubDataPropertyAxiomsBySubPropertyIndex subDataPropertyAxiomsBySubPropertyIndex;

    @Nonnull
    @Override
    public Set<OWLSubDataPropertyOfAxiom> getDataSubPropertyAxiomsForSubProperty(@Nonnull OWLDataProperty subProperty) {
        return subDataPropertyAxiomsBySubPropertyIndex.getSubPropertyOfAxioms(subProperty,
                                                                              ontologyID)
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<OWLSubDataPropertyOfAxiom> getDataSubPropertyAxiomsForSuperProperty(@Nonnull OWLDataPropertyExpression superProperty) {
        return Collections.emptySet();
    }

    @Nonnull
    private final DataPropertyDomainAxiomsIndex dataPropertyDomainAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLDataPropertyDomainAxiom> getDataPropertyDomainAxioms(@Nonnull OWLDataProperty property) {
        return dataPropertyDomainAxiomsIndex.getDataPropertyDomainAxioms(property,
                                                                         ontologyID)
                .collect(toSet());
    }

    @Nonnull
    private final DataPropertyRangeAxiomsIndex dataPropertyRangeAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLDataPropertyRangeAxiom> getDataPropertyRangeAxioms(@Nonnull OWLDataProperty property) {
        return dataPropertyRangeAxiomsIndex.getDataPropertyRangeAxioms(property,
                                                                ontologyID)
                                    .collect(toSet());
    }

    private final EquivalentDataPropertiesAxiomsIndex equivalentDataPropertiesAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLEquivalentDataPropertiesAxiom> getEquivalentDataPropertiesAxioms(@Nonnull OWLDataProperty property) {
        return equivalentDataPropertiesAxiomsIndex.getEquivalentDataPropertiesAxioms(property,
                                                                                     ontologyID)
                .collect(toSet());
    }

    private final DisjointDataPropertiesAxiomsIndex disjointDataPropertiesAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLDisjointDataPropertiesAxiom> getDisjointDataPropertiesAxioms(@Nonnull OWLDataProperty property) {
        return disjointDataPropertiesAxiomsIndex.getDisjointDataPropertiesAxioms(property,
                                                                                 ontologyID)
                .collect(toSet());
    }


    @Nonnull
    @Override
    public Set<OWLFunctionalDataPropertyAxiom> getFunctionalDataPropertyAxioms(@Nonnull OWLDataPropertyExpression property) {
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.FUNCTIONAL_DATA_PROPERTY, ontologyID)
                              .filter(ax -> ax.getProperty().equals(property))
                .collect(toSet());
    }

    @Nonnull
    private final ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex;

    @Nonnull
    @Override
    public Set<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull OWLIndividual individual) {
        return classAssertionAxiomsByIndividualIndex.getClassAssertionAxioms(individual, ontologyID)
                .collect(toSet());
    }

    private final ClassAssertionAxiomsByClassIndex classAssertionAxiomsByClassIndex;

    @Nonnull
    @Override
    public Set<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull OWLClassExpression ce) {
        if(ce.isAnonymous()) {
            return Collections.emptySet();
        }
        return classAssertionAxiomsByClassIndex.getClassAssertionAxioms(ce.asOWLClass(), ontologyID)
                .collect(toSet());
    }

    @Nonnull
    private final DataPropertyAssertionAxiomsBySubjectIndex dataPropertyAssertionAxiomsBySubjectIndex;

    @Nonnull
    @Override
    public Set<OWLDataPropertyAssertionAxiom> getDataPropertyAssertionAxioms(@Nonnull OWLIndividual individual) {
        return dataPropertyAssertionAxiomsBySubjectIndex.getDataPropertyAssertions(individual, ontologyID)
                                                 .collect(Collectors.toSet());
    }

    @Nonnull
    private final ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertionAxiomsBySubjectIndex;

    @Nonnull
    @Override
    public Set<OWLObjectPropertyAssertionAxiom> getObjectPropertyAssertionAxioms(@Nonnull OWLIndividual individual) {
        return objectPropertyAssertionAxiomsBySubjectIndex.getObjectPropertyAssertions(individual, ontologyID)
                .collect(toSet());
    }
    @Nonnull
    @Override
    public Set<OWLNegativeObjectPropertyAssertionAxiom> getNegativeObjectPropertyAssertionAxioms(@Nonnull OWLIndividual individual) {
        return Collections.emptySet();
    }

    @Nonnull
    @Override
    public Set<OWLNegativeDataPropertyAssertionAxiom> getNegativeDataPropertyAssertionAxioms(@Nonnull OWLIndividual individual) {
        return Collections.emptySet();
    }

    @Nonnull
    private final SameIndividualAxiomsIndex sameIndividualAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLSameIndividualAxiom> getSameIndividualAxioms(@Nonnull OWLIndividual individual) {
        return sameIndividualAxiomsIndex.getSameIndividualAxioms(individual, ontologyID)
                .collect(toSet());
    }

    @Nonnull
    private final DifferentIndividualsAxiomsIndex differentIndividualsAxiomsIndex;

    @Nonnull
    @Override
    public Set<OWLDifferentIndividualsAxiom> getDifferentIndividualAxioms(@Nonnull OWLIndividual individual) {
        return differentIndividualsAxiomsIndex.getDifferentIndividualsAxioms(individual, ontologyID)
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<OWLDatatypeDefinitionAxiom> getDatatypeDefinitions(@Nonnull OWLDatatype datatype) {
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.DATATYPE_DEFINITION, ontologyID)
                .filter(ax -> ax.getDatatype().equals(datatype))
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<OWLClassExpression> getNestedClassExpressions() {
        throw notAvailableException();
    }

    @Override
    public void accept(@Nonnull OWLObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Nonnull
    @Override
    public <O> O accept(@Nonnull OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isTopEntity() {
        return false;
    }

    @Override
    public boolean isBottomEntity() {
        return false;
    }

    @Override
    public int compareTo(@Nonnull OWLObject o) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAnnotationProperty> getAnnotationPropertiesInSignature() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAnonymousIndividual> getAnonymousIndividuals() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLClass> getClassesInSignature(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLObjectProperty> getObjectPropertiesInSignature(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDataProperty> getDataPropertiesInSignature(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLNamedIndividual> getIndividualsInSignature(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAnonymousIndividual> getReferencedAnonymousIndividuals(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDatatype> getDatatypesInSignature(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAnnotationProperty> getAnnotationPropertiesInSignature(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull OWLEntity owlEntity, @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull IRI entityIRI, @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsClassInSignature(@Nonnull IRI owlClassIRI, @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsObjectPropertyInSignature(@Nonnull IRI owlObjectPropertyIRI,
                                                     @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsDataPropertyInSignature(@Nonnull IRI owlDataPropertyIRI,
                                                   @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsAnnotationPropertyInSignature(@Nonnull IRI owlAnnotationPropertyIRI,
                                                         @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsDatatypeInSignature(@Nonnull IRI owlDatatypeIRI, @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsIndividualInSignature(@Nonnull IRI owlIndividualIRI,
                                                 @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsDatatypeInSignature(@Nonnull IRI owlDatatypeIRI) {
        throw notAvailableException();
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull IRI entityIRI) {
        throw notAvailableException();
    }

    @Override
    public boolean containsClassInSignature(@Nonnull IRI owlClassIRI) {
        throw notAvailableException();
    }

    @Override
    public boolean containsObjectPropertyInSignature(@Nonnull IRI owlObjectPropertyIRI) {
        throw notAvailableException();
    }

    @Override
    public boolean containsDataPropertyInSignature(@Nonnull IRI owlDataPropertyIRI) {
        throw notAvailableException();
    }

    @Override
    public boolean containsAnnotationPropertyInSignature(@Nonnull IRI owlAnnotationPropertyIRI) {
        throw notAvailableException();
    }

    @Override
    public boolean containsIndividualInSignature(@Nonnull IRI owlIndividualIRI) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLEntity> getEntitiesInSignature(@Nonnull IRI iri, @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public Set<IRI> getPunnedIRIs(@Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsReference(@Nonnull OWLEntity entity, @Nonnull Imports includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsReference(@Nonnull OWLEntity entity) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLClass> getClassesInSignature(boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLObjectProperty> getObjectPropertiesInSignature(boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDataProperty> getDataPropertiesInSignature(boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLNamedIndividual> getIndividualsInSignature(boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAnonymousIndividual> getReferencedAnonymousIndividuals(boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDatatype> getDatatypesInSignature(boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLAnnotationProperty> getAnnotationPropertiesInSignature(boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull OWLEntity owlEntity, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull IRI entityIRI, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsClassInSignature(@Nonnull IRI owlClassIRI, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsObjectPropertyInSignature(@Nonnull IRI owlObjectPropertyIRI, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsDataPropertyInSignature(@Nonnull IRI owlDataPropertyIRI, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsAnnotationPropertyInSignature(@Nonnull IRI owlAnnotationPropertyIRI,
                                                         boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsDatatypeInSignature(@Nonnull IRI owlDatatypeIRI, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsIndividualInSignature(@Nonnull IRI owlIndividualIRI, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLEntity> getEntitiesInSignature(@Nonnull IRI iri, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Override
    public boolean containsReference(@Nonnull OWLEntity entity, boolean includeImportsClosure) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLClass> getClassesInSignature() {
        throw notAvailableException();
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull OWLEntity owlEntity) {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDataProperty> getDataPropertiesInSignature() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLDatatype> getDatatypesInSignature() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLNamedIndividual> getIndividualsInSignature() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public Set<OWLObjectProperty> getObjectPropertiesInSignature() {
        throw notAvailableException();
    }

    @Nonnull
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(OwlOntologyFacade.class)
                .addValue(ontologyID)
                .toString();
    }
}
