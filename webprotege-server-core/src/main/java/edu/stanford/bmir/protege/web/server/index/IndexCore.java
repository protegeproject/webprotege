package edu.stanford.bmir.protege.web.server.index;

import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.server.change.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import uk.ac.manchester.cs.owl.owlapi.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-03
 */
public class IndexCore implements SubClassOfAxiomsBySubClassIndex, AxiomsByEntityReferenceIndex, EntitiesInProjectSignatureByIriIndex, ProjectSignatureByTypeIndex {

    private final ChangeProcessor changeProcessor = new ChangeProcessor(new AxiomIndexer(new ObjectProcessor()));

    private final AxiomsByEntityReferenceIndexInserter axiomsByEntityReferenceIndexInserter = new AxiomsByEntityReferenceIndexInserter();

    private final AxiomsByEntityReferenceIndexRemover axiomsByEntityReferenceIndexRemover = new AxiomsByEntityReferenceIndexRemover();

    private EntityInSignatureChecker entityInSignatureChecker = new EntityInSignatureChecker();

    private Entity2ReferencingAxiomsVisitor entity2ReferencingAxiomsVisitor = new Entity2ReferencingAxiomsVisitor();


    private final ListMultimap<OWLClass, OWLSubClassOfAxiom> subClassOfAxiomsBySubClass = ArrayListMultimap.create(200, 2);

    private final Multimap<OWLClass, OWLSubClassOfAxiom> subClassOfAxiomBySuperClass = ArrayListMultimap.create(200, 2);

    private final Multimap<OWLAnnotationSubject, OWLAnnotationAssertionAxiom> annotationAssertionsBySubject = ArrayListMultimap.create(
            200,
            3);

    private final Multimap<OWLAnnotationObject, OWLAnnotationAssertionAxiom> annotationAssertionsByObject = ArrayListMultimap.create();

    private Multimap<AxiomType, OWLAxiom> axiomsByType = HashMultimap.create();

    private Multimap<OWLClass, OWLAxiom> axiomsByClassSignature = ArrayListMultimap.create();

    private Multimap<OWLNamedIndividual, OWLAxiom> axiomsByIndividualSignature = ArrayListMultimap.create();

    private Multimap<OWLObjectProperty, OWLAxiom> axiomsByObjectPropertySignature = ArrayListMultimap.create();

    private Multimap<OWLDataProperty, OWLAxiom> axiomsByDataPropertySignature = ArrayListMultimap.create();

    private Multimap<OWLAnnotationProperty, OWLAxiom> axiomsByAnnotationPropertySignature = ArrayListMultimap.create();

    private Multimap<OWLDatatype, OWLAxiom> axiomsByDatatypeSignature = ArrayListMultimap.create();


    public void apply(List<OntologyChange> changes) {
        changes.forEach(chg -> chg.accept(changeProcessor));
    }

    public Stream<OWLEntity> getEntitiesInSignatureWithIri(@Nonnull IRI iri) {
        var resultBuilder = Stream.<OWLEntity>builder();
        var cls = new OWLClassImpl(iri);
        if(axiomsByClassSignature.containsKey(cls)) {
            resultBuilder.add(cls);
        }
        var objectProperty = new OWLObjectPropertyImpl(iri);
        if(axiomsByObjectPropertySignature.containsKey(objectProperty)) {
            resultBuilder.add(objectProperty);
        }
        var dataProperty = new OWLDataPropertyImpl(iri);
        if(axiomsByDataPropertySignature.containsKey(dataProperty)) {
            resultBuilder.add(dataProperty);
        }
        var annotationProperty = new OWLAnnotationPropertyImpl(iri);
        if(axiomsByAnnotationPropertySignature.containsKey(annotationProperty)) {
            resultBuilder.add(annotationProperty);
        }
        var individual = new OWLNamedIndividualImpl(iri);
        if(axiomsByIndividualSignature.containsKey(individual)) {
            resultBuilder.add(individual);
        }
        var datatype = new OWLDatatypeImpl(iri);
        if(axiomsByDatatypeSignature.containsKey(iri)) {
            resultBuilder.add(datatype);
        }
        return resultBuilder.build();
    }

    public boolean containsEntityInSignature(@Nonnull OWLEntity entity) {
        return entity.accept(entityInSignatureChecker);
    }

    public Stream<OWLEntity> getSignature() {
        return ImmutableList.<OWLEntity>builder()
                            .addAll(axiomsByClassSignature.keySet())
                            .addAll(axiomsByIndividualSignature.keySet())
                            .addAll(axiomsByObjectPropertySignature.keySet())
                            .addAll(axiomsByDataPropertySignature.keySet())
                            .addAll(axiomsByAnnotationPropertySignature.keySet())
                            .addAll(axiomsByDataPropertySignature.keySet())
                .build().stream();
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getEntitiesInSignature(@Nonnull IRI entityIri) {
        return getEntitiesInSignatureWithIri(entityIri);
    }

    @Override
    public Stream<OWLSubClassOfAxiom> getSubClassOfAxiomsForSubClass(@Nonnull OWLClass subClass,
                                                                     @Nonnull OWLOntologyID ontologyID) {
        return ImmutableList.copyOf(subClassOfAxiomsBySubClass.get(subClass)).stream();
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <E extends OWLEntity> Stream<E> getSignature(@Nonnull EntityType<E> entityType) {
        if(entityType.equals(EntityType.CLASS)) {
            return ImmutableList.copyOf((Set<E>) axiomsByClassSignature.keySet()).stream();
        }
        else if(entityType.equals(EntityType.OBJECT_PROPERTY)) {
            return ImmutableList.copyOf((Set<E>) axiomsByObjectPropertySignature.keys()).stream();
        }
        else if(entityType.equals(EntityType.DATA_PROPERTY)) {
            return ImmutableList.copyOf((Set<E>) axiomsByDataPropertySignature.keys()).stream();
        }
        else if(entityType.equals(EntityType.ANNOTATION_PROPERTY)) {
            return ImmutableList.copyOf((Set<E>) axiomsByAnnotationPropertySignature.keys()).stream();
        }
        else if(entityType.equals(EntityType.NAMED_INDIVIDUAL)) {
            return ImmutableList.copyOf((Set<E>) axiomsByIndividualSignature.keys()).stream();
        }
        else if(entityType.equals(EntityType.DATATYPE)) {
            return ImmutableList.copyOf((Set<E>) axiomsByDatatypeSignature.keys()).stream();
        }
        else {
            throw new RuntimeException("Unknown Entity Type " + entityType);
        }
    }

    @Override
    public Stream<OWLAxiom> getReferencingAxioms(@Nonnull OWLEntity entity, @Nonnull OWLOntologyID ontologyId) {
        return ImmutableList.copyOf(entity.accept(entity2ReferencingAxiomsVisitor)).stream();
    }

    private class ChangeProcessor implements OntologyChangeVisitor {

        @Nonnull
        private final AxiomIndexer axiomIndexer;

        public ChangeProcessor(@Nonnull AxiomIndexer axiomIndexer) {
            this.axiomIndexer = checkNotNull(axiomIndexer);
        }

        @Override
        public void visit(@Nonnull AddAxiomChange addAxiomChange) {
            OWLAxiom axiom = addAxiomChange.getAxiom();
            if(!axiomsByType.put(axiom.getAxiomType(), axiom)) {
                return;
            }
            axiomIndexer.setAdd(true);
            axiomIndexer.setEntityConsumer((ax, ent) -> {
                axiomsByEntityReferenceIndexInserter.setAxiom(ax);
                ent.accept(axiomsByEntityReferenceIndexInserter);
            });
            axiom.accept(axiomIndexer);
        }

        @Override
        public void visit(@Nonnull RemoveAxiomChange removeAxiomChange) {
            var axiom = removeAxiomChange.getAxiom();
            if(axiomsByType.remove(axiom.getAxiomType(), axiom)) {
                return;
            }
            axiomIndexer.setAdd(false);
            axiomIndexer.setEntityConsumer((ax, ent) -> {
                axiomsByEntityReferenceIndexRemover.setAxiom(ax);
                ent.accept(axiomsByEntityReferenceIndexRemover);
            });
            axiom.accept(axiomIndexer);
        }

        @Override
        public void visit(@Nonnull AddOntologyAnnotationChange addOntologyAnnotationChange) {

        }

        @Override
        public void visit(@Nonnull RemoveOntologyAnnotationChange removeOntologyAnnotationChange) {

        }

        @Override
        public void visit(@Nonnull AddImportChange addImportChange) {

        }

        @Override
        public void visit(@Nonnull RemoveImportChange removeImportChange) {

        }
    }

    private class AxiomIndexer implements OWLAxiomVisitor {

        private final ObjectProcessor objectProcessor;

        private boolean add = true;

        @Nonnull
        private BiConsumer<OWLAxiom, OWLEntity> entityConsumer = (ax, entity) -> {};

        @Nullable
        private OWLAxiom currentAxiom = null;

        private AxiomIndexer(ObjectProcessor objectProcessor) {
            this.objectProcessor = checkNotNull(objectProcessor);
        }

        public void setEntityConsumer(@Nonnull BiConsumer<OWLAxiom, OWLEntity> entityConsumer) {
            this.entityConsumer = checkNotNull(entityConsumer);
            objectProcessor.setEntityConsumer(entity -> {
                if(currentAxiom != null) {
                    entityConsumer.accept(currentAxiom, entity);
                }
            });
        }

        public void setAdd(boolean add) {
            this.add = add;
        }

        private void handleAxiom(@Nonnull OWLAxiom axiom) {
            currentAxiom = axiom;
            objectProcessor.processAnnotations(axiom);
        }

        private void handleUnaryPropertyAxiom(OWLUnaryPropertyAxiom<?> axiom) {
            axiom.getProperty().accept(objectProcessor);
        }

        private void handlePropertyAssertionAxioms(@Nonnull OWLPropertyAssertionAxiom<?, ?> axiom) {
            axiom.getSubject().accept(objectProcessor);
            axiom.getProperty().accept(objectProcessor);
            axiom.getObject().accept(objectProcessor);
        }

        private void handleNaryClassAxiom(@Nonnull OWLNaryClassAxiom axiom) {
            axiom.getClassExpressions().forEach(ce -> ce.accept(objectProcessor));
        }

        private void handleNaryPropertyAxiom(@Nonnull OWLNaryPropertyAxiom<?> axiom) {
            axiom.getProperties().forEach(prop -> prop.accept(objectProcessor));
        }

        private void handleNaryIndividualsAxiom(@Nonnull OWLNaryIndividualAxiom axiom) {
            axiom.getIndividuals().forEach(individual -> individual.accept(objectProcessor));
        }

        private void handleDomainAxiom(@Nonnull OWLPropertyDomainAxiom<?> axiom) {
            handleAxiom(axiom);
            axiom.getProperty().accept(objectProcessor);
            axiom.getDomain().accept(objectProcessor);
        }

        private void handleRangeAxiom(@Nonnull OWLPropertyRangeAxiom<?, ?> axiom) {
            handleAxiom(axiom);
            axiom.getProperty().accept(objectProcessor);
            axiom.getRange().accept(objectProcessor);
        }

        private void handleSubPropertyOfAxiom(@Nonnull OWLSubPropertyAxiom<?> axiom) {
            axiom.getSubProperty().accept(objectProcessor);
            axiom.getSuperProperty().accept(objectProcessor);
        }

        @Override
        public void visit(@Nonnull OWLAsymmetricObjectPropertyAxiom axiom) {
            handleAxiom(axiom);
            handleUnaryPropertyAxiom(axiom);

        }

        @Override
        public void visit(@Nonnull OWLClassAssertionAxiom axiom) {
            handleAxiom(axiom);
            axiom.getClassExpression().accept(objectProcessor);
            axiom.getIndividual().accept(objectProcessor);
        }

        @Override
        public void visit(@Nonnull OWLDataPropertyAssertionAxiom axiom) {
            handleAxiom(axiom);
            handlePropertyAssertionAxioms(axiom);
        }

        @Override
        public void visit(@Nonnull OWLDataPropertyDomainAxiom axiom) {
            handleAxiom(axiom);
            handleDomainAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLDataPropertyRangeAxiom axiom) {
            handleAxiom(axiom);
            handleRangeAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLDeclarationAxiom axiom) {
            handleAxiom(axiom);
            axiom.getEntity().accept(objectProcessor);
        }

        @Override
        public void visit(@Nonnull OWLDifferentIndividualsAxiom axiom) {
            handleAxiom(axiom);
            handleNaryIndividualsAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLDisjointClassesAxiom axiom) {
            handleAxiom(axiom);
            handleNaryClassAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLDisjointDataPropertiesAxiom axiom) {
            handleAxiom(axiom);
            handleNaryPropertyAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLDisjointObjectPropertiesAxiom axiom) {
            handleAxiom(axiom);
            handleNaryPropertyAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLDisjointUnionAxiom axiom) {
            axiom.getOWLClass().accept(objectProcessor);
            axiom.getClassExpressions().forEach(ce -> ce.accept(objectProcessor));
        }

        @Override
        public void visit(@Nonnull OWLEquivalentClassesAxiom axiom) {
            handleAxiom(axiom);
            handleNaryClassAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLEquivalentDataPropertiesAxiom axiom) {
            handleAxiom(axiom);
            handleNaryPropertyAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLEquivalentObjectPropertiesAxiom axiom) {
            handleAxiom(axiom);
            handleNaryPropertyAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLFunctionalDataPropertyAxiom axiom) {
            handleAxiom(axiom);
            handleUnaryPropertyAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLFunctionalObjectPropertyAxiom axiom) {
            handleAxiom(axiom);
            handleUnaryPropertyAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLHasKeyAxiom axiom) {
            handleAxiom(axiom);
            axiom.getClassExpression().accept(objectProcessor);
            axiom.getPropertyExpressions().forEach(prop -> prop.accept(objectProcessor));
        }

        @Override
        public void visit(@Nonnull OWLInverseFunctionalObjectPropertyAxiom axiom) {
            handleAxiom(axiom);
            handleUnaryPropertyAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLInverseObjectPropertiesAxiom axiom) {
            handleAxiom(axiom);
            axiom.getFirstProperty().accept(objectProcessor);
            axiom.getSecondProperty().accept(objectProcessor);
        }

        @Override
        public void visit(@Nonnull OWLIrreflexiveObjectPropertyAxiom axiom) {
            handleAxiom(axiom);
            handleUnaryPropertyAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLNegativeDataPropertyAssertionAxiom axiom) {
            handleAxiom(axiom);
            handlePropertyAssertionAxioms(axiom);
        }

        @Override
        public void visit(@Nonnull OWLNegativeObjectPropertyAssertionAxiom axiom) {
            handleAxiom(axiom);
            handlePropertyAssertionAxioms(axiom);
        }

        @Override
        public void visit(@Nonnull OWLObjectPropertyAssertionAxiom axiom) {
            handleAxiom(axiom);
            handlePropertyAssertionAxioms(axiom);
        }

        @Override
        public void visit(@Nonnull OWLSubPropertyChainOfAxiom axiom) {
            handleAxiom(axiom);
            axiom.getSuperProperty().accept(objectProcessor);
            axiom.getPropertyChain().forEach(prop -> prop.accept(objectProcessor));
        }

        @Override
        public void visit(@Nonnull OWLObjectPropertyDomainAxiom axiom) {
            handleAxiom(axiom);
            handleDomainAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLObjectPropertyRangeAxiom axiom) {
            handleAxiom(axiom);
            handleRangeAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLReflexiveObjectPropertyAxiom axiom) {
            handleAxiom(axiom);
            handleUnaryPropertyAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLSameIndividualAxiom axiom) {
            handleAxiom(axiom);
            handleNaryIndividualsAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLSubClassOfAxiom axiom) {
            handleAxiom(axiom);
            var subClass = axiom.getSubClass();
            subClass
                 .accept(objectProcessor);
            var superClass = axiom.getSuperClass();
            superClass
                 .accept(objectProcessor);

            if(add) {
                if(subClass.isNamed()) {
                    subClassOfAxiomsBySubClass.put(subClass.asOWLClass(), axiom);
                }
                if(superClass.isNamed()) {
                    subClassOfAxiomBySuperClass.put(superClass.asOWLClass(), axiom);
                }
            }
            else {
                if(subClass.isNamed()) {
                    subClassOfAxiomsBySubClass.remove(subClass.asOWLClass(), axiom);
                }
                if(superClass.isNamed()) {
                    subClassOfAxiomBySuperClass.remove(superClass.asOWLClass(), axiom);
                }
            }
        }

        @Override
        public void visit(@Nonnull OWLSubDataPropertyOfAxiom axiom) {
            handleAxiom(axiom);
            handleSubPropertyOfAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLSubObjectPropertyOfAxiom axiom) {
            handleAxiom(axiom);
            handleSubPropertyOfAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLSymmetricObjectPropertyAxiom axiom) {
            handleAxiom(axiom);
            handleUnaryPropertyAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull OWLTransitiveObjectPropertyAxiom axiom) {
            handleAxiom(axiom);
            handleUnaryPropertyAxiom(axiom);
        }

        @Override
        public void visit(@Nonnull SWRLRule rule) {
            handleAxiom(rule);
            rule.getBody().forEach(atom -> atom.accept(objectProcessor));
            rule.getHead().forEach(atom -> atom.accept(objectProcessor));
        }

        @Override
        public void visit(@Nonnull OWLAnnotationAssertionAxiom axiom) {
            handleAxiom(axiom);
            axiom.getSubject().accept(objectProcessor);
            axiom.getProperty().accept(objectProcessor);
            axiom.getValue().accept(objectProcessor);

            if(add) {
                annotationAssertionsBySubject.put(axiom.getSubject(), axiom);
                if(!(axiom.getValue() instanceof OWLLiteral)) {
                    annotationAssertionsByObject.put(axiom.getValue(), axiom);
                }
            }
            else {
                annotationAssertionsBySubject.remove(axiom.getSubject(), axiom);
                if(!(axiom.getValue() instanceof OWLLiteral)) {
                    annotationAssertionsByObject.remove(axiom.getValue(), axiom);
                }
            }
        }

        @Override
        public void visit(@Nonnull OWLAnnotationPropertyDomainAxiom axiom) {
            handleAxiom(axiom);
            axiom.getProperty().accept(objectProcessor);
            axiom.getDomain().accept(objectProcessor);
        }

        @Override
        public void visit(@Nonnull OWLAnnotationPropertyRangeAxiom axiom) {
            handleAxiom(axiom);
            axiom.getProperty().accept(objectProcessor);
            axiom.getRange().accept(objectProcessor);
        }

        @Override
        public void visit(@Nonnull OWLSubAnnotationPropertyOfAxiom axiom) {
            handleAxiom(axiom);
            axiom.getSubProperty().accept(objectProcessor);
            axiom.getSuperProperty().accept(objectProcessor);
        }

        @Override
        public void visit(@Nonnull OWLDatatypeDefinitionAxiom axiom) {
            handleAxiom(axiom);
            axiom.getDatatype().accept(objectProcessor);
            axiom.getDataRange().accept(objectProcessor);
        }
    }


    private class ObjectProcessor extends OWLObjectVisitorAdapter {

        private Consumer<OWLEntity> entityConsumer = entity -> {};

        private Consumer<IRI> iriConsumer = iri -> {};

        public void setEntityConsumer(@Nonnull Consumer<OWLEntity> entityConsumer) {
            this.entityConsumer = checkNotNull(entityConsumer);
        }

        public void setIriConsumer(@Nonnull Consumer<IRI> iriConsumer) {
            this.iriConsumer = checkNotNull(iriConsumer);
        }

        private void processAnnotations(@Nonnull HasAnnotations object) {
            object.getAnnotations()
                  .forEach(annotation -> {
                      annotation.getProperty().accept((OWLEntityVisitor) this);
                      annotation.getValue().accept(this);
                      processAnnotations(annotation);
                  });
        }

        @Override
        public void visit(@Nonnull IRI iri) {
            iriConsumer.accept(iri);
        }

        @Override
        public void visit(@Nonnull OWLClass ce) {
            visit(ce.getIRI());
            entityConsumer.accept(ce);
        }

        @Override
        public void visit(@Nonnull OWLObjectIntersectionOf ce) {
            ce.getOperands()
              .forEach(op -> op.accept(this));
        }

        @Override
        public void visit(@Nonnull OWLObjectUnionOf ce) {
            ce.getOperands()
              .forEach(op -> op.accept(this));
        }

        @Override
        public void visit(@Nonnull OWLObjectComplementOf ce) {
            ce.getOperand()
              .accept(this);
        }

        @Override
        public void visit(@Nonnull OWLObjectSomeValuesFrom ce) {
            processQuantifiedRestriction(ce);
        }

        private void processQuantifiedRestriction(@Nonnull OWLQuantifiedRestriction restriction) {
            restriction.getProperty()
                       .accept(this);
            restriction.getFiller()
                       .accept(this);
        }

        @Override
        public void visit(@Nonnull OWLObjectAllValuesFrom ce) {
            processQuantifiedRestriction(ce);
        }

        @Override
        public void visit(@Nonnull OWLObjectHasValue ce) {
            ce.getProperty()
              .accept(this);
            ce.getFiller()
              .accept(this);
        }

        @Override
        public void visit(@Nonnull OWLObjectMinCardinality ce) {
            processQuantifiedRestriction(ce);
        }

        @Override
        public void visit(@Nonnull OWLObjectExactCardinality ce) {
            processQuantifiedRestriction(ce);
        }

        @Override
        public void visit(@Nonnull OWLObjectMaxCardinality ce) {
            processQuantifiedRestriction(ce);
        }

        @Override
        public void visit(@Nonnull OWLObjectHasSelf ce) {
            ce.getProperty()
              .accept(this);
        }

        @Override
        public void visit(@Nonnull OWLObjectOneOf ce) {
            ce.getIndividuals()
              .forEach(ind -> ind.accept(this));
        }

        @Override
        public void visit(@Nonnull OWLDataSomeValuesFrom ce) {
            processQuantifiedRestriction(ce);
        }

        @Override
        public void visit(@Nonnull OWLDataAllValuesFrom ce) {
            processQuantifiedRestriction(ce);
        }

        @Override
        public void visit(@Nonnull OWLDataHasValue ce) {
            ce.getProperty()
              .accept(this);
            ce.getFiller()
              .accept(this);
        }

        @Override
        public void visit(@Nonnull OWLDataMinCardinality ce) {
            processQuantifiedRestriction(ce);
        }

        @Override
        public void visit(@Nonnull OWLDataExactCardinality ce) {
            processQuantifiedRestriction(ce);
        }

        @Override
        public void visit(@Nonnull OWLDataMaxCardinality ce) {
            processQuantifiedRestriction(ce);
        }

        @Override
        public void visit(@Nonnull OWLLiteral node) {
            node.getDatatype()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull OWLFacetRestriction node) {
            node.getFacetValue()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull OWLDatatype node) {
            entityConsumer.accept(node);
        }

        @Override
        public void visit(@Nonnull OWLDataOneOf node) {
            node.getValues()
                .forEach(val -> val.accept(this));
        }

        @Override
        public void visit(@Nonnull OWLDataComplementOf node) {
            node.getDataRange()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull OWLDataIntersectionOf node) {
            node.getOperands()
                .forEach(dr -> dr.accept(this));
        }

        @Override
        public void visit(@Nonnull OWLDataUnionOf node) {
            node.getOperands()
                .forEach(dr -> dr.accept(this));
        }

        @Override
        public void visit(@Nonnull OWLDatatypeRestriction node) {
            node.getDatatype()
                .accept((OWLEntityVisitor) this);
            node.getFacetRestrictions()
                .forEach(fr -> fr.accept(this));
        }

        @Override
        public void visit(@Nonnull OWLNamedIndividual individual) {
            entityConsumer.accept(individual);
        }

        @Override
        public void visit(@Nonnull OWLAnonymousIndividual individual) {

        }

        @Override
        public void visit(@Nonnull OWLObjectProperty property) {
            entityConsumer.accept(property);
        }

        @Override
        public void visit(@Nonnull OWLObjectInverseOf property) {
            property.getInverse()
                    .accept(this);
        }

        @Override
        public void visit(@Nonnull OWLDataProperty property) {
            entityConsumer.accept(property);
        }

        @Override
        public void visit(@Nonnull OWLAnnotationProperty property) {
            entityConsumer.accept(property);
        }

        @Override
        public void visit(@Nonnull SWRLClassAtom node) {
            node.getPredicate()
                .accept(this);
            node.getArgument()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull SWRLDataRangeAtom node) {
            node.getPredicate()
                .accept(this);
            node.getArgument()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull SWRLObjectPropertyAtom node) {
            node.getPredicate()
                .accept(this);
            node.getFirstArgument()
                .accept(this);
            node.getSecondArgument()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull SWRLDataPropertyAtom node) {
            node.getPredicate()
                .accept(this);
            node.getFirstArgument()
                .accept(this);
            node.getSecondArgument()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull SWRLBuiltInAtom node) {
            node.getArguments()
                .forEach(arg -> arg.accept(this));
            node.getPredicate()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull SWRLVariable node) {
            node.getIRI()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull SWRLIndividualArgument node) {
            node.getIndividual()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull SWRLLiteralArgument node) {
            node.getLiteral()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull SWRLSameIndividualAtom node) {
            node.getFirstArgument()
                .accept(this);
            node.getSecondArgument()
                .accept(this);
        }

        @Override
        public void visit(@Nonnull SWRLDifferentIndividualsAtom node) {
            node.getFirstArgument()
                .accept(this);
            node.getSecondArgument()
                .accept(this);
        }
    }

    private class EntityInSignatureChecker implements OWLEntityVisitorEx<Boolean> {
        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLClass cls) {
            return axiomsByClassSignature.containsKey(cls);
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLObjectProperty property) {
            return axiomsByObjectPropertySignature.containsKey(property);
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLDataProperty property) {
            return axiomsByDataPropertySignature.containsKey(property);
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLNamedIndividual individual) {
            return axiomsByIndividualSignature.containsKey(individual);
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLDatatype datatype) {
            return axiomsByDatatypeSignature.containsKey(datatype);
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLAnnotationProperty property) {
            return axiomsByAnnotationPropertySignature.containsKey(property);
        }
    };

    private class Entity2ReferencingAxiomsVisitor implements OWLEntityVisitorEx<Collection<OWLAxiom>> {
        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLClass cls) {
            return axiomsByClassSignature.get(cls);
        }

        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLObjectProperty property) {
            return axiomsByObjectPropertySignature.get(property);
        }

        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLDataProperty property) {
            return axiomsByDataPropertySignature.get(property);
        }

        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLNamedIndividual individual) {
            return axiomsByIndividualSignature.get(individual);
        }

        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLDatatype datatype) {
            return axiomsByDatatypeSignature.get(datatype);
        }

        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLAnnotationProperty property) {
            return axiomsByAnnotationPropertySignature.get(property);
        }
    };

    private class AxiomsByEntityReferenceIndexInserter implements OWLEntityVisitor {

        @Nullable
        private OWLAxiom axiom;

        public void setAxiom(@Nullable OWLAxiom axiom) {
            this.axiom = checkNotNull(axiom);
        }

        @Override
        public void visit(@Nonnull OWLClass cls) {
            axiomsByClassSignature.put(cls, axiom);
        }

        @Override
        public void visit(@Nonnull OWLObjectProperty property) {
            axiomsByObjectPropertySignature.put(property, axiom);
        }

        @Override
        public void visit(@Nonnull OWLDataProperty property) {
            axiomsByDataPropertySignature.put(property, axiom);
        }

        @Override
        public void visit(@Nonnull OWLNamedIndividual individual) {
            axiomsByIndividualSignature.put(individual, axiom);
        }

        @Override
        public void visit(@Nonnull OWLDatatype datatype) {
            axiomsByDatatypeSignature.put(datatype, axiom);
        }

        @Override
        public void visit(@Nonnull OWLAnnotationProperty property) {
            axiomsByAnnotationPropertySignature.put(property, axiom);
        }
    }

    private class AxiomsByEntityReferenceIndexRemover implements OWLEntityVisitor {

        private OWLAxiom axiom;

        public void setAxiom(OWLAxiom axiom) {
            this.axiom = checkNotNull(axiom);
        }

        @Override
            public void visit(@Nonnull OWLClass cls) {
                axiomsByClassSignature.remove(cls, axiom);
            }

            @Override
            public void visit(@Nonnull OWLObjectProperty property) {
                axiomsByObjectPropertySignature.remove(property, axiom);
            }

            @Override
            public void visit(@Nonnull OWLDataProperty property) {
                axiomsByDataPropertySignature.remove(property, axiom);
            }

            @Override
            public void visit(@Nonnull OWLNamedIndividual individual) {
                axiomsByIndividualSignature.remove(individual, axiom);
            }

            @Override
            public void visit(@Nonnull OWLDatatype datatype) {
                axiomsByDatatypeSignature.remove(datatype, axiom);
            }

            @Override
            public void visit(@Nonnull OWLAnnotationProperty property) {
                axiomsByAnnotationPropertySignature.remove(property, axiom);
            }
    }
}
