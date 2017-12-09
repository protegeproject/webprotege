package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.MoveHierarchyNodeAction;
import edu.stanford.protege.gwt.graphtree.shared.DropType;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

import static edu.stanford.bmir.protege.web.server.util.ProtegeStreams.ontologyStream;
import static java.util.Collections.emptySet;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
public class MoveEntityChangeListGenerator implements ChangeListGenerator<Boolean> {

    private final OWLOntology rootOntology;

    private final OWLDataFactory dataFactory;

    private final MoveHierarchyNodeAction action;

    private final EventManager<ProjectEvent<?>> eventManager;


    public MoveEntityChangeListGenerator(OWLOntology rootOntology, OWLDataFactory dataFactory, MoveHierarchyNodeAction action, EventManager<ProjectEvent<?>> eventManager) {
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
        this.action = action;
        this.eventManager = eventManager;
    }

    private static OntologyChangeList<Boolean> notMoved() {
        return OntologyChangeList.<Boolean>builder().build(false);
    }

    private static boolean isClassHierarchyMove(OWLEntity moveEntity, OWLEntity fromParent, OWLEntity toParent) {
        return moveEntity.isOWLClass() && fromParent.isOWLClass() && toParent.isOWLClass();
    }

    private static boolean isObjectPropertyHierarchyMove(OWLEntity moveEntity, OWLEntity fromParent, OWLEntity toParent) {
        return moveEntity.isOWLObjectProperty() && fromParent.isOWLObjectProperty() && toParent.isOWLObjectProperty();
    }

    private static boolean isDataPropertyHierarchyMove(OWLEntity moveEntity, OWLEntity fromParent, OWLEntity toParent) {
        return moveEntity.isOWLDataProperty() && fromParent.isOWLDataProperty() && toParent.isOWLDataProperty();
    }

    private static boolean isAnnotationPropertyHierarchyMove(OWLEntity moveEntity, OWLEntity fromParent, OWLEntity toParent) {
        return moveEntity.isOWLAnnotationProperty() && fromParent.isOWLAnnotationProperty() && toParent.isOWLAnnotationProperty();
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        Path<EntityHierarchyNode> fromPath = action.getFromNodePath();
        Optional<OWLEntity> move = fromPath.getLast().map(EntityHierarchyNode::getEntity);
        if (!move.isPresent()) {
            return notMoved();
        }
        Optional<EntityHierarchyNode> lastPredecessor = fromPath.getLastPredecessor();
        if (!lastPredecessor.isPresent()) {
            return notMoved();
        }
        OWLEntity moveEntity = move.get();
        OWLEntity fromParent = lastPredecessor.map(EntityHierarchyNode::getEntity).orElse(dataFactory.getOWLThing());
        OWLEntity toParent = action.getToNodeParentPath().getLast().map(EntityHierarchyNode::getEntity).orElse(dataFactory.getOWLThing());
        if (isClassHierarchyMove(moveEntity, fromParent, toParent)) {
            return moveClass(moveEntity.asOWLClass(),
                             fromParent.asOWLClass(),
                             toParent.asOWLClass(),
                             action.getDropType());
        }
        else if (isObjectPropertyHierarchyMove(moveEntity, fromParent, toParent)) {
            return moveObjectProperty(moveEntity.asOWLObjectProperty(),
                                      fromParent.asOWLObjectProperty(),
                                      toParent.asOWLObjectProperty(),
                                      action.getDropType());
        }
        else if (isDataPropertyHierarchyMove(moveEntity, fromParent, toParent)) {
            return moveDataProperty(moveEntity.asOWLDataProperty(),
                                    fromParent.asOWLDataProperty(),
                                    toParent.asOWLDataProperty(),
                                    action.getDropType());
        }
        else if (isAnnotationPropertyHierarchyMove(moveEntity, fromParent, toParent)) {
            return moveAnnotationProperty(moveEntity.asOWLAnnotationProperty(),
                                          fromParent.asOWLAnnotationProperty(),
                                          toParent.asOWLAnnotationProperty(),
                                          action.getDropType());
        }
        else {
            return notMoved();
        }

    }

    /**
     * Move or copy the specified class from the specified parent to the the specified parent.
     *
     * @param moveClass       The class to move/copy.
     * @param fromParentClass The parent that the class will be removed/copied from.
     * @param toParentClass   The parent that the class will be moved/copied to.
     * @return The changes that are required to move/copy the class.
     */
    private OntologyChangeList<Boolean> moveClass(@Nonnull OWLClass moveClass,
                                                  @Nonnull OWLClass fromParentClass,
                                                  @Nonnull OWLClass toParentClass,
                                                  @Nonnull DropType dropType) {
        if (isPlacedByEquivalentClassesAxiom(moveClass, fromParentClass)) {
            return notMoved();
        }
        return moveEntity(moveClass,
                          fromParentClass,
                          toParentClass,
                          dropType, OWLOntology::getSubClassAxiomsForSubClass,
                          ax -> ax.getSuperClass().equals(fromParentClass),
                          dataFactory::getOWLSubClassOfAxiom
        );
    }

    private OntologyChangeList<Boolean> moveObjectProperty(@Nonnull OWLObjectProperty moveProperty,
                                                           @Nonnull OWLObjectProperty fromParent,
                                                           @Nonnull OWLObjectProperty toParent,
                                                           @Nonnull DropType dropType) {
        return moveEntity(moveProperty,
                          fromParent,
                          toParent,
                          dropType, OWLOntology::getObjectSubPropertyAxiomsForSubProperty,
                          ax -> ax.getSuperProperty().equals(fromParent),
                          dataFactory::getOWLSubObjectPropertyOfAxiom
        );
    }

    private OntologyChangeList<Boolean> moveDataProperty(@Nonnull OWLDataProperty moveProperty,
                                                         @Nonnull OWLDataProperty fromParent,
                                                         @Nonnull OWLDataProperty toParent,
                                                         @Nonnull DropType dropType) {
        return moveEntity(moveProperty,
                          fromParent,
                          toParent,
                          dropType, OWLOntology::getDataSubPropertyAxiomsForSubProperty,
                          ax -> ax.getSuperProperty().equals(fromParent),
                          dataFactory::getOWLSubDataPropertyOfAxiom
        );
    }

    private OntologyChangeList<Boolean> moveAnnotationProperty(@Nonnull OWLAnnotationProperty moveProperty,
                                                               @Nonnull OWLAnnotationProperty fromParent,
                                                               @Nonnull OWLAnnotationProperty toParent,
                                                               @Nonnull DropType dropType) {
        return moveEntity(moveProperty,
                          fromParent,
                          toParent,
                          dropType, OWLOntology::getSubAnnotationPropertyOfAxioms,
                          ax -> ax.getSubProperty().equals(moveProperty) && ax.getSuperProperty().equals(fromParent),
                          dataFactory::getOWLSubAnnotationPropertyOfAxiom
        );
    }

    /**
     * Move/copy an entity from one parent to another parent.
     * @param move The entity to move/copy (the child).
     * @param fromParent The parent to move/copy the entity from.
     * @param toParent The parent to move/copy the entity to.
     * @param dropType Whether the operation is a move or a copy
     * @param axiomExtractor An extractor that can select candidate axioms for removal from an ontology (axioms that
     *                       potentially specify the current parent).
     * @param axiomFilter A filter that filters axioms for remove (to remove the child from its existing parent).
     * @param reparentingAxiomFactory A factory that can create axioms to reposition the child under its new parent.
     * @param <A> The axiom type.
     * @param <E> The entity type.
     * @return A list of changes for moving or copying the entity.
     */
    private <A extends OWLAxiom, E extends OWLEntity>
    OntologyChangeList<Boolean> moveEntity(@Nonnull E move,
                                           @Nonnull E fromParent,
                                           @Nonnull E toParent,
                                           @Nonnull DropType dropType,
                                           @Nonnull BiFunction<OWLOntology, E, Set<A>> axiomExtractor,
                                           @Nonnull RemoveAxiomFilter<A> axiomFilter,
                                           @Nonnull ReparentingAxiomFactory<A, E> reparentingAxiomFactory
    ) {
        OntologyChangeList.Builder<Boolean> changeList = OntologyChangeList.builder();
        Set<OWLAxiom> removedAxioms = new HashSet<>();
        if (dropType == DropType.MOVE) {
            ontologyStream(rootOntology, Imports.INCLUDED)
                    .forEach(ont -> {
                        axiomExtractor.apply(ont, move).stream()
                                      .filter(axiomFilter::test)
                                      .forEach(ax -> {
                                          changeList.removeAxiom(ont, ax);
                                          removedAxioms.add(ax);
                                          A parAx = reparentingAxiomFactory.createReparentingAxiom(move,
                                                                                                   toParent,
                                                                                                   ax.getAnnotations());
                                          changeList.addAxiom(ont, parAx);
                                      });
                    });
        }
        if (removedAxioms.isEmpty()) {
            changeList.addAxiom(rootOntology, reparentingAxiomFactory.createReparentingAxiom(move,
                                                                                             toParent,
                                                                                             emptySet()));
        }
        return changeList.build(!changeList.isEmpty());
    }

    @Override
    public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
        return result;
    }

    private boolean isPlacedByEquivalentClassesAxiom(@Nonnull OWLClass subClass,
                                                     @Nonnull OWLClass superClass) {
        return ontologyStream(rootOntology, Imports.INCLUDED)
                .flatMap(o -> o.getEquivalentClassesAxioms(subClass).stream())
                .flatMap(ax -> ax.getClassExpressions().stream())
                .flatMap(ce -> ce.asConjunctSet().stream())
                .anyMatch(ce -> ce.equals(superClass));
    }


    private interface ReparentingAxiomFactory<A extends OWLAxiom, E extends OWLEntity> {
        A createReparentingAxiom(E move, E toParent, Set<OWLAnnotation> annotations);
    }

    private interface RemoveAxiomFilter<A extends OWLAxiom> {
        boolean test(A axiom);
    }

}
