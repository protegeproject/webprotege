package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.AxiomChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.EntityType.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-11-30
 */
public class EntityCreationMatcher implements ChangeMatcher {


    @Nonnull
    private final OWLObjectStringFormatter formatter;

    private final OWLAxiomVisitorExAdapter<IRI> entityCreationAxiomVisitor = new OWLAxiomVisitorExAdapter<>(null) {
        @Override
        public IRI visit(OWLDeclarationAxiom axiom) {
            return axiom.getEntity().getIRI();
        }

        @Override
        public IRI visit(OWLClassAssertionAxiom axiom) {
            var individual = axiom.getIndividual();
            if(individual.isAnonymous()) {
                return null;
            }
            else {
                return individual.asOWLNamedIndividual().getIRI();
            }
        }

        @Override
        public IRI visit(OWLSubClassOfAxiom axiom) {
            if(axiom.getSubClass().isAnonymous()) {
                return null;
            }
            if(axiom.getSuperClass().isAnonymous()) {
                return null;
            }
            return axiom.getSubClass().asOWLClass().getIRI();
        }

        @Override
        public IRI visit(OWLSubAnnotationPropertyOfAxiom axiom) {
            return axiom.getSubProperty().getIRI();
        }

        @Override
        public IRI visit(OWLSubObjectPropertyOfAxiom axiom) {
            if(axiom.getSubProperty().isAnonymous()) {
                return null;
            }
            if(axiom.getSuperProperty().isAnonymous()) {
                return null;
            }
            return axiom.getSubProperty().asOWLObjectProperty().getIRI();
        }

        @Override
        public IRI visit(OWLSubDataPropertyOfAxiom axiom) {
            if(axiom.getSubProperty().isAnonymous()) {
                return null;
            }
            if(axiom.getSuperProperty().isAnonymous()) {
                return null;
            }
            return axiom.getSubProperty().asOWLDataProperty().getIRI();
        }

        @Override
        public IRI visit(OWLAnnotationAssertionAxiom axiom) {
            if(!(axiom.getSubject() instanceof IRI)) {
                return null;
            }
            return (IRI) axiom.getSubject();
        }
    };

    @Inject
    public EntityCreationMatcher(@Nonnull OWLObjectStringFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Optional<String> getDescription(List<OWLOntologyChangeData> changeData) {
        // Make sure that we only have axiom additions.  Axiom removals indicate something
        // other kind of edit
        var changeDataContainsNonAxiomAddition = changeData
                .stream()
                .anyMatch(data -> !((data instanceof AddAxiomData) && isEntityCreationAxiom((OWLAxiom) data.getItem())));
        if(changeDataContainsNonAxiomAddition) {
            return Optional.empty();
        }
        // Here, Entity Creation Consists of:
        //   1) a Declaration axiom
        //   2) possibly a positioning axiom e.g. SubClassOf, Sub*PropertyOf, ClassAssertion
        // First group axioms by their subject
        var axiomsBySubject = changeData
                .stream()
                .filter(data -> data instanceof AddAxiomData)
                .map(data -> (AddAxiomData) data)
                .map(AxiomChangeData::getAxiom)
                .filter(this::isEntityCreationAxiom)
                .collect(groupingBy(this::getSubject));
        // Now figure out whether the set of axioms for each subject is an entity creation set of axioms
        // and if so extract the created entities
        var declaredEntities = axiomsBySubject
                .values()
                .stream()
                .filter(this::isEntityDeclaringAxiomSet)
                .flatMap(this::extractDeclaredEntitiesFromAxiomSet)
                .collect(toSet());

        // Each subject must correspond to a declared entity
        var numberOfDeclaredEntities = declaredEntities.size();
        if(numberOfDeclaredEntities != axiomsBySubject.size()) {
            return Optional.empty();
        }

        // Get the parents of the declared entities
        var declaredEntityParents = axiomsBySubject
                .values()
                .stream()
                .flatMap(Collection::stream)
                .map(EntityCreationMatcher::getStatedParent)
                .filter(Objects::nonNull)
                .collect(toImmutableSet());
        var mixedDeclaredParents = declaredEntityParents.size() > 1;

        if(mixedDeclaredParents) {
            // Possibly other stuff going on
            return Optional.empty();
        }

        // Now get the types of the declared entities
        var declaredEntityTypes = declaredEntities
                .stream()
                .map(OWLEntity::getEntityType)
                .collect(ImmutableSet.<EntityType<?>>toImmutableSet());

        var singleEntityDeclaration = numberOfDeclaredEntities == 1;
        final String relationshipLabel;
        if(onlyClassesAreDeclared(declaredEntityTypes)) {
            if(singleEntityDeclaration) {
                relationshipLabel = "a subclass";
            }
            else {
                relationshipLabel = "subclasses";
            }
        }
        else if(onlyIndividualsAreDeclared(declaredEntityTypes)) {
            if(singleEntityDeclaration) {
                relationshipLabel = "an instance";
            }
            else {
                relationshipLabel = "instances";
            }
        }
        else if(onlyObjectPropertiesAreDeclared(declaredEntityTypes)) {
            if(singleEntityDeclaration) {
                relationshipLabel = "a sub-property";
            }
            else {
                relationshipLabel = "sub-properties";
            }
        }
        else if(onlyDataPropertiesAreDeclared(declaredEntityTypes)) {
            if(singleEntityDeclaration) {
                relationshipLabel = "a sub-property";
            }
            else {
                relationshipLabel = "sub-properties";
            }
        }
        else if(onlyAnnotationPropertiesAreDeclared(declaredEntityTypes)) {
            if(singleEntityDeclaration) {
                relationshipLabel = "a sub-property";
            }
            else {
                relationshipLabel = "sub-properties";
            }
        }
        else {
            // Mixed stuff created
            return Optional.empty();
        }
        if(declaredEntityParents.isEmpty()) {
            var entityTypePrintName = declaredEntityTypes
                    .stream()
                    .map(entityType -> toEntityTypePrintName(singleEntityDeclaration, entityType))
                    .map(String::toLowerCase)
                    .findFirst()
                    .orElse(getFallbackName(singleEntityDeclaration));
            return formatter.format("Created %s %s", entityTypePrintName, declaredEntities);
        }
        else {
            var parent = declaredEntityParents.iterator().next();
            return formatter.format("Created %s as %s of %s", declaredEntities, relationshipLabel, parent);
        }
    }

    private boolean isEntityCreationAxiom(OWLAxiom axiom) {
        return axiom.accept(entityCreationAxiomVisitor) != null;
    }

    private IRI getSubject(OWLAxiom axiom) {
        return axiom.accept(entityCreationAxiomVisitor);
    }

    private boolean isEntityDeclaringAxiomSet(List<OWLAxiom> axioms) {
        return axioms.stream().anyMatch(axiom -> axiom instanceof OWLDeclarationAxiom);
    }

    private Stream<OWLEntity> extractDeclaredEntitiesFromAxiomSet(List<OWLAxiom> axioms) {
        return axioms
                .stream()
                .filter(ax -> ax instanceof OWLDeclarationAxiom)
                .map(ax -> ((OWLDeclarationAxiom) ax).getEntity());
    }

    @Nullable
    private static OWLEntity getStatedParent(@Nonnull OWLAxiom axiom) {
        return axiom.accept(new OWLAxiomVisitorExAdapter<>(null) {
            @Override
            public OWLEntity visit(OWLSubClassOfAxiom axiom) {
                var superClass = axiom.getSuperClass();
                if(superClass.isAnonymous()) {
                    return null;
                }
                else {
                    return superClass.asOWLClass();
                }
            }

            @Override
            public OWLEntity visit(OWLClassAssertionAxiom axiom) {
                var type = axiom.getClassExpression();
                if(type.isAnonymous()) {
                    return null;
                }
                else {
                    return type.asOWLClass();
                }
            }

            @Override
            public OWLEntity visit(OWLSubAnnotationPropertyOfAxiom axiom) {
                return axiom.getSuperProperty();
            }

            @Override
            public OWLEntity visit(OWLSubObjectPropertyOfAxiom axiom) {
                var superProperty = axiom.getSuperProperty();
                if(superProperty.isAnonymous()) {
                    return null;
                }
                else {
                    return superProperty.asOWLObjectProperty();
                }
            }

            @Override
            public OWLEntity visit(OWLSubDataPropertyOfAxiom axiom) {
                var superProperty = axiom.getSuperProperty();
                return superProperty.asOWLDataProperty();
            }
        });
    }

    private static boolean onlyClassesAreDeclared(Set<EntityType<?>> entityTypes) {
        return entityTypes.equals(Collections.singleton(CLASS));
    }

    private static boolean onlyIndividualsAreDeclared(Set<EntityType<?>> entityTypes) {
        return entityTypes.equals(Collections.singleton(NAMED_INDIVIDUAL));
    }

    private static boolean onlyObjectPropertiesAreDeclared(Set<EntityType<?>> entityTypes) {
        return entityTypes.equals(Collections.singleton(OBJECT_PROPERTY));
    }

    private static boolean onlyDataPropertiesAreDeclared(Set<EntityType<?>> entityTypes) {
        return entityTypes.equals(Collections.singleton(DATA_PROPERTY));
    }

    private static boolean onlyAnnotationPropertiesAreDeclared(Set<EntityType<?>> entityTypes) {
        return entityTypes.equals(Collections.singleton(ANNOTATION_PROPERTY));
    }

    private static String toEntityTypePrintName(boolean singleEntityDeclaration,
                                                EntityType<?> entityType) {
        return singleEntityDeclaration ? entityType.getPrintName() : entityType.getPluralPrintName();
    }

    private static String getFallbackName(boolean singleEntityDeclaration) {
        return singleEntityDeclaration ? "entity" : "entities";
    }
}
