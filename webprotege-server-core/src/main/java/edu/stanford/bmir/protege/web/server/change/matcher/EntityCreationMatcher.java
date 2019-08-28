package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.description.*;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.AxiomChangeData;
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

    @Nonnull
    private final EntityCreationAxiomSubjectProvider entityCreationAxiomSubjectProvider = new EntityCreationAxiomSubjectProvider();

    @Inject
    public EntityCreationMatcher(@Nonnull OWLObjectStringFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Optional<ChangeSummary> getDescription(List<OntologyChange> changes) {
        // Make sure that we only have axiom additions.  Axiom removals indicate something
        // other kind of edit
        var changeDataContainsNonAxiomAddition = changes
                .stream()
                .anyMatch(chg -> !(chg.isAddAxiom() && isEntityCreationAxiom(chg.getAxiomOrThrow())));
        if(changeDataContainsNonAxiomAddition) {
            return Optional.empty();
        }
        // Here, Entity Creation Consists of:
        //   1) a Declaration axiom
        //   2) possibly a positioning axiom e.g. SubClassOf, Sub*PropertyOf, ClassAssertion
        // First group axioms by their subject
        var axiomsBySubject = changes
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
        final StructuredChangeDescription description;
        if(onlyClassesAreDeclared(declaredEntityTypes)) {
            description = CreatedClasses.get(to(declaredEntities), to(declaredEntityParents));
        }
        else if(onlyIndividualsAreDeclared(declaredEntityTypes)) {
            description = CreatedIndividuals.get(to(declaredEntities), to(declaredEntityParents));
        }
        else if(onlyObjectPropertiesAreDeclared(declaredEntityTypes)) {
            description = CreatedObjectProperties.get(to(declaredEntities), to(declaredEntityParents));
        }
        else if(onlyDataPropertiesAreDeclared(declaredEntityTypes)) {
            description = CreatedDataProperties.get(to(declaredEntities), to(declaredEntityParents));
        }
        else if(onlyAnnotationPropertiesAreDeclared(declaredEntityTypes)) {
            description = CreatedAnnotationProperties.get(to(declaredEntities), to(declaredEntityParents));
        }
        else {
            // Mixed stuff created
            return Optional.empty();
        }
        return Optional.of(ChangeSummary.get(description));
    }

    private boolean isEntityCreationAxiom(OWLAxiom axiom) {
        return entityCreationAxiomSubjectProvider
                .getEntityCreationAxiomSubject(axiom)
                .isPresent();
    }

    private IRI getSubject(OWLAxiom axiom) {
        return entityCreationAxiomSubjectProvider
                .getEntityCreationAxiomSubject(axiom)
                .orElseThrow();
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

    @SuppressWarnings("unchecked")
    private static <E extends OWLEntity> ImmutableSet<E> to(Collection<OWLEntity> from) {
        return from.stream()
                .map(e -> (E) e)
                .collect(toImmutableSet());
    }

    @Nullable
    private static OWLEntity getStatedParent(@Nonnull OWLAxiom axiom) {
        return axiom.accept(new OWLAxiomVisitorExAdapter<>(null) {
            @Nonnull
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

            @Nonnull
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

            @Nonnull
            @Override
            public OWLEntity visit(OWLSubAnnotationPropertyOfAxiom axiom) {
                return axiom.getSuperProperty();
            }

            @Nonnull
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

            @Nonnull
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
