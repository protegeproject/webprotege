package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.project.ChangeManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.obo.OBORelationship;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermCrossProduct;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermRelationships;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class TermCrossProductsManager {

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final OWLDataFactory df;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final RelationshipConverter relationshipConverter;

    @Inject
    public TermCrossProductsManager(@Nonnull OWLOntology rootOntology,
                                    @Nonnull RenderingManager renderingManager,
                                    @Nonnull OWLDataFactory df,
                                    @Nonnull ChangeManager changeManager,
                                    @Nonnull RelationshipConverter relationshipConverter) {
        this.rootOntology = rootOntology;
        this.renderingManager = renderingManager;
        this.df = df;
        this.changeManager = changeManager;
        this.relationshipConverter = relationshipConverter;
    }

    @Nonnull
    public OBOTermCrossProduct getCrossProduct(@Nonnull OWLClass term) {
        Optional<OWLEquivalentClassesAxiom> axiom = getCrossProductEquivalentClassesAxiom(term);
        if (!axiom.isPresent()) {
            return OBOTermCrossProduct.emptyOBOTermCrossProduct();
        }
        Set<OWLObjectSomeValuesFrom> relationships = new HashSet<>();
        Optional<OWLClass> genus = Optional.empty();
        for (OWLClassExpression operand : axiom.get().getClassExpressionsMinus(term)) {
            Set<OWLClassExpression> conjuncts = operand.asConjunctSet();
            for (OWLClassExpression conjunct : conjuncts) {
                if (conjunct instanceof OWLObjectSomeValuesFrom) {
                    OWLObjectSomeValuesFrom svf = (OWLObjectSomeValuesFrom) conjunct;
                    if (!svf.getProperty().isAnonymous() && !svf.getFiller().isAnonymous()) {
                        relationships.add((OWLObjectSomeValuesFrom) conjunct);
                    }
                }
                else if (conjunct instanceof OWLClass) {
                    genus = Optional.of((OWLClass) conjunct);
                }
            }
        }
        Optional<OWLClassData> visualCls = genus.map(renderingManager::getRendering);
        Set<OBORelationship> discriminatingRelationships =
                relationships.stream()
                             .map(relationshipConverter::toOboRelationship)
                             .collect(toSet());
        return new OBOTermCrossProduct(visualCls, new OBOTermRelationships(discriminatingRelationships));
    }

    /**
     * Gets an equivalent classes axiom that corresponds to an OBO Cross Product.  An equivalent classes axiom AX
     * corresponds to a cross product for a class C if AX contains C as an operand, and AX contains one other class
     * which is either an ObjectSomeValuesFrom restriction, or an intersection of ObjectSomeValuesFrom restrictions
     * plus an optional named class.  i.e.   AX = EquivalentClasses(C ObjectIntersectionOf(A
     * ObjectSomeValuesFrom(..)...
     * ObjectSomeValuesFrom(..))
     *
     * @param cls The subject of the cross product
     * @return An {@link OWLEquivalentClassesAxiom} that corresponds to a cross product for the class, or
     * <code>null</code> if the ontology doesn't contain an equivalent classes axiom that corresponds to a
     * cross
     * product.
     */
    @Nonnull
    private Optional<OWLEquivalentClassesAxiom> getCrossProductEquivalentClassesAxiom(@Nonnull OWLClass cls) {
        Set<OWLEquivalentClassesAxiom> candidates = new TreeSet<>();
        for (OWLEquivalentClassesAxiom axiom : rootOntology.getEquivalentClassesAxioms(cls)) {
            Set<OWLClassExpression> equivalentClasses = axiom.getClassExpressionsMinus(cls);
            int namedCount = 0;
            int someValuesFromCount = 0;
            int otherCount = 0;
            for (OWLClassExpression operand : equivalentClasses) {
                for (OWLClassExpression ce : operand.asConjunctSet()) {
                    if (ce instanceof OWLClass) {
                        namedCount++;
                    }
                    else if (ce instanceof OWLObjectSomeValuesFrom) {
                        OWLObjectSomeValuesFrom svf = (OWLObjectSomeValuesFrom) ce;
                        if (!svf.getProperty().isAnonymous() && !svf.getFiller().isAnonymous()) {
                            someValuesFromCount++;
                        }
                    }
                    else {
                        otherCount++;
                    }
                }
            }
            if (namedCount <= 1 && someValuesFromCount > 0 && otherCount == 0) {
                candidates.add(axiom.getAxiomWithoutAnnotations());
            }
        }
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        if (candidates.size() == 1) {
            return Optional.of(candidates.iterator().next());
        }
        // More than one
        // Return the first one (they are ordered by the OWLObject comparator, so for a given set of class expression this
        // is consistent
        return Optional.of(candidates.iterator().next());
    }


    public void setCrossProduct(@Nonnull UserId userId,
                                @Nonnull OWLClass term,
                                @Nonnull OBOTermCrossProduct crossProduct) {
        Set<OWLClassExpression> intersectionOperands = new HashSet<>();
        Optional<OWLClassData> genus = crossProduct.getGenus();
        if (genus.isPresent()) {
            OWLClass cls = genus.get().getEntity();
            intersectionOperands.add(cls);
        }

        for (OBORelationship relationship : crossProduct.getRelationships().getRelationships()) {
            OWLObjectSomeValuesFrom someValuesFrom = relationshipConverter.toSomeValuesFrom(relationship);
            intersectionOperands.add(someValuesFrom);
        }
        OWLObjectIntersectionOf intersectionOf = df.getOWLObjectIntersectionOf(intersectionOperands);

        OWLEquivalentClassesAxiom newXPAxiom = df.getOWLEquivalentClassesAxiom(term, intersectionOf);

        Optional<OWLEquivalentClassesAxiom> existingXPAxiom = getCrossProductEquivalentClassesAxiom(term);

        List<OWLOntologyChange> changes = new ArrayList<>();
        changes.add(new AddAxiom(rootOntology, newXPAxiom));
        existingXPAxiom.ifPresent(ax -> changes.add(new RemoveAxiom(rootOntology, ax)));
        changeManager.applyChanges(userId,
                                   new FixedChangeListGenerator<>(changes),
                                   new FixedMessageChangeDescriptionGenerator<>("Set cross product values"));

    }

}
