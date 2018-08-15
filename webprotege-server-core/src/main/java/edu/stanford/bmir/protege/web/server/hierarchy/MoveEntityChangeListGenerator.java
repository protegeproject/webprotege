package edu.stanford.bmir.protege.web.server.hierarchy;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.MoveHierarchyNodeAction;
import edu.stanford.protege.gwt.graphtree.shared.DropType;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.util.ProtegeStreams.ontologyStream;
import static java.util.Collections.emptySet;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
@AutoFactory
public class MoveEntityChangeListGenerator implements ChangeListGenerator<Boolean> {

    private final OWLOntology rootOntology;

    private final OWLDataFactory dataFactory;

    private final MoveHierarchyNodeAction action;

    private final MessageFormatter msg;

    @Inject
    public MoveEntityChangeListGenerator(@Provided @Nonnull OWLDataFactory dataFactory,
                                         @Provided @Nonnull OWLOntology rootOntology,
                                         @Provided @Nonnull MessageFormatter msg,
                                         @Nonnull MoveHierarchyNodeAction action) {
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
        this.action = checkNotNull(action);
        this.msg = msg;
    }

    private static OntologyChangeList<Boolean> notMoved() {
        return OntologyChangeList.<Boolean>builder().build(false);
    }

    private static boolean isClassHierarchyMove(OWLEntity moveEntity, Optional<OWLEntity> fromParent, Optional<OWLEntity> toParent) {
        return moveEntity.isOWLClass()
                && fromParent.map(OWLEntity::isOWLClass).orElse(true)
                && toParent.map(OWLEntity::isOWLClass).orElse(true);
    }

    private static boolean isObjectPropertyHierarchyMove(OWLEntity moveEntity, Optional<OWLEntity> fromParent, Optional<OWLEntity> toParent) {
        return moveEntity.isOWLObjectProperty()
                && fromParent.map(OWLEntity::isOWLObjectProperty).orElse(true)
                && toParent.map(OWLEntity::isOWLObjectProperty).orElse(true);
    }

    private static boolean isDataPropertyHierarchyMove(OWLEntity moveEntity, Optional<OWLEntity> fromParent, Optional<OWLEntity> toParent) {
        return moveEntity.isOWLDataProperty()
                && fromParent.map(OWLEntity::isOWLDataProperty).orElse(true)
                && toParent.map(OWLEntity::isOWLDataProperty).orElse(true);
    }

    private static boolean isAnnotationPropertyHierarchyMove(OWLEntity moveEntity, Optional<OWLEntity> fromParent, Optional<OWLEntity> toParent) {
        return moveEntity.isOWLAnnotationProperty()
                && fromParent.map(OWLEntity::isOWLAnnotationProperty).orElse(true)
                && toParent.map(OWLEntity::isOWLAnnotationProperty).orElse(true);
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        Path<EntityNode> fromPath = action.getFromNodePath();
        Optional<OWLEntity> move = fromPath.getLast().map(EntityNode::getEntity);
        if (!move.isPresent()) {
            return notMoved();
        }
        Optional<EntityNode> lastPredecessor = fromPath.getLastPredecessor();
        OWLEntity moveEntity = move.get();
        Optional<OWLEntity> fromParent = lastPredecessor.map(EntityNode::getEntity);
        Optional<OWLEntity> toParent = action.getToNodeParentPath().getLast().map(EntityNode::getEntity);
        if (isClassHierarchyMove(moveEntity, fromParent, toParent)) {
            return moveClass(moveEntity.asOWLClass(),
                             fromParent.map(OWLEntity::asOWLClass),
                             toParent.map(OWLEntity::asOWLClass),
                             action.getDropType());
        }
        else if (isObjectPropertyHierarchyMove(moveEntity, fromParent, toParent)) {
            return moveObjectProperty(moveEntity.asOWLObjectProperty(),
                                      fromParent.map(OWLEntity::asOWLObjectProperty),
                                      toParent.map(OWLEntity::asOWLObjectProperty),
                                      action.getDropType());
        }
        else if (isDataPropertyHierarchyMove(moveEntity, fromParent, toParent)) {
            return moveDataProperty(moveEntity.asOWLDataProperty(),
                                    fromParent.map(OWLEntity::asOWLDataProperty),
                                    toParent.map(OWLEntity::asOWLDataProperty),
                                    action.getDropType());
        }
        else if (isAnnotationPropertyHierarchyMove(moveEntity, fromParent, toParent)) {
            return moveAnnotationProperty(moveEntity.asOWLAnnotationProperty(),
                                          fromParent.map(OWLEntity::asOWLAnnotationProperty),
                                          toParent.map(OWLEntity::asOWLAnnotationProperty),
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
                                                  @Nonnull Optional<OWLClass> fromParentClass,
                                                  @Nonnull Optional<OWLClass> toParentClass,
                                                  @Nonnull DropType dropType) {
        if (fromParentClass.isPresent() && isPlacedByEquivalentClassesAxiom(moveClass, fromParentClass.get())) {
            return notMoved();
        }
        return moveEntity(moveClass,
                          fromParentClass,
                          toParentClass,
                          dropType,
                          OWLOntology::getSubClassAxiomsForSubClass,
                          ax -> Optional.of(ax.getSuperClass()).equals(fromParentClass),
                          dataFactory::getOWLSubClassOfAxiom
        );
    }

    private OntologyChangeList<Boolean> moveObjectProperty(@Nonnull OWLObjectProperty moveProperty,
                                                           @Nonnull Optional<OWLObjectProperty> fromParent,
                                                           @Nonnull Optional<OWLObjectProperty> toParent,
                                                           @Nonnull DropType dropType) {
        return moveEntity(moveProperty,
                          fromParent,
                          toParent,
                          dropType, OWLOntology::getObjectSubPropertyAxiomsForSubProperty,
                          ax -> Optional.of(ax.getSuperProperty()).equals(fromParent),
                          dataFactory::getOWLSubObjectPropertyOfAxiom
        );
    }

    private OntologyChangeList<Boolean> moveDataProperty(@Nonnull OWLDataProperty moveProperty,
                                                         @Nonnull Optional<OWLDataProperty> fromParent,
                                                         @Nonnull Optional<OWLDataProperty> toParent,
                                                         @Nonnull DropType dropType) {
        return moveEntity(moveProperty,
                          fromParent,
                          toParent,
                          dropType, OWLOntology::getDataSubPropertyAxiomsForSubProperty,
                          ax -> Optional.of(ax.getSuperProperty()).equals(fromParent),
                          dataFactory::getOWLSubDataPropertyOfAxiom
        );
    }

    private OntologyChangeList<Boolean> moveAnnotationProperty(@Nonnull OWLAnnotationProperty moveProperty,
                                                               @Nonnull Optional<OWLAnnotationProperty> fromParent,
                                                               @Nonnull Optional<OWLAnnotationProperty> toParent,
                                                               @Nonnull DropType dropType) {
        return moveEntity(moveProperty,
                          fromParent,
                          toParent,
                          dropType, OWLOntology::getSubAnnotationPropertyOfAxioms,
                          ax -> ax.getSubProperty().equals(moveProperty) && Optional.of(ax.getSuperProperty()).equals(fromParent),
                          dataFactory::getOWLSubAnnotationPropertyOfAxiom
        );
    }

    /**
     * Move/copy an entity from one parent to another parent.
     * @param move The entity to move/copy (the child).
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
                                           @Nonnull Optional<E> fromParent,
                                           @Nonnull Optional<E> toParent,
                                           @Nonnull DropType dropType,
                                           @Nonnull BiFunction<OWLOntology, E, Set<A>> axiomExtractor,
                                           @Nonnull RemoveAxiomFilter<A> axiomFilter,
                                           @Nonnull ReparentingAxiomFactory<A, E> reparentingAxiomFactory
    ) {
        OntologyChangeList.Builder<Boolean> changeList = OntologyChangeList.builder();
        Set<OWLAxiom> removedAxioms = new HashSet<>();
        if (dropType == DropType.MOVE && fromParent.isPresent()) {
            ontologyStream(rootOntology, Imports.INCLUDED)
                          .forEach(ont -> {
                        axiomExtractor.apply(ont, move).stream()
                                      .filter(axiomFilter::test)
                                      .forEach(ax -> {
                                          changeList.removeAxiom(ont, ax);
                                          removedAxioms.add(ax);
                                          toParent.ifPresent(par -> {
                                              A parAx = reparentingAxiomFactory.createReparentingAxiom(move,
                                                                                                       par,
                                                                                                       ax.getAnnotations());
                                              changeList.addAxiom(ont, parAx);
                                          });
                                      });
                    });
        }
        if (removedAxioms.isEmpty()) {
            toParent.ifPresent(par -> {
                changeList.addAxiom(rootOntology, reparentingAxiomFactory.createReparentingAxiom(move,
                                                                                                 par,
                                                                                                 emptySet()));
            });
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

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        String type = action.getFromNodePath().getLast().map(node -> node.getEntity().getEntityType().getPrintName().toLowerCase()).orElse("entity");
        String entity = action.getFromNodePath().getLast().map(EntityNode::getBrowserText).orElse("");
        String from = action.getFromNodePath().getLastPredecessor().map(EntityNode::getBrowserText).orElse("root");
        String to = action.getToNodeParentPath().getLast().map(EntityNode::getBrowserText).orElse("root");
        return msg.format("Moved {0} {1} from {2} to {3}", type, entity, from, to);
    }
}
