package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.project.ChangeManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.obo.OBORelationship;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermRelationships;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class TermRelationshipsManager {


    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final RelationshipConverter relationshipConverter;

    @Inject
    public TermRelationshipsManager(@Nonnull OWLDataFactory dataFactory,
                                    @Nonnull OWLOntology rootOntology,
                                    @Nonnull RenderingManager renderingManager,
                                    @Nonnull ChangeManager changeManager,
                                    @Nonnull RelationshipConverter relationshipConverter) {
        this.dataFactory = dataFactory;
        this.rootOntology = rootOntology;
        this.renderingManager = renderingManager;
        this.changeManager = changeManager;
        this.relationshipConverter = relationshipConverter;
    }

    public OBOTermRelationships getRelationships(OWLClass term) {
        OWLClass cls = dataFactory.getOWLClass(term.getIRI());
        Set<OWLSubClassOfAxiom> subClassOfAxioms = rootOntology.getSubClassAxiomsForSubClass(cls);
        Set<OBORelationship> rels = new HashSet<OBORelationship>();
        for (OWLSubClassOfAxiom ax : subClassOfAxioms) {
            Set<OWLObjectSomeValuesFrom> relationships = new HashSet<OWLObjectSomeValuesFrom>();
            Set<OWLClassExpression> conjuncts = ax.getSuperClass().asConjunctSet();
            for (OWLClassExpression conjunct : conjuncts) {
                if (conjunct instanceof OWLObjectSomeValuesFrom) {
                    OWLObjectSomeValuesFrom svf = (OWLObjectSomeValuesFrom) conjunct;
                    if (!svf.getProperty().isAnonymous() && !svf.getFiller().isAnonymous()) {
                        relationships.add((OWLObjectSomeValuesFrom) conjunct);
                    }
                }
            }
            if (relationships.size() == conjuncts.size()) {
                for (OWLObjectSomeValuesFrom rel : relationships) {
                    OWLObjectPropertyData property = renderingManager.getRendering(rel.getProperty().asOWLObjectProperty());
                    OWLClassData filler = renderingManager.getRendering(rel.getFiller().asOWLClass());
                    OBORelationship oboRel = new OBORelationship(property, filler);
                    rels.add(oboRel);
                }
            }
        }
        return new OBOTermRelationships(rels);
    }

    public void setRelationships(@Nonnull UserId userId,
                                 @Nonnull OWLClass lastEntity,
                                 @Nonnull OBOTermRelationships relationships) {
        Set<OWLObjectSomeValuesFrom> superClsesToSet = new HashSet<>();
        for (OBORelationship relationship : relationships.getRelationships()) {
            OWLObjectSomeValuesFrom someValuesFrom = relationshipConverter.toSomeValuesFrom(relationship);
            superClsesToSet.add(someValuesFrom);
        }


        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        OWLClass cls = dataFactory.getOWLClass(lastEntity.getIRI());
        Set<OWLObjectSomeValuesFrom> existingSuperClsesToReplace = new HashSet<OWLObjectSomeValuesFrom>();
        for (OWLSubClassOfAxiom ax : rootOntology.getSubClassAxiomsForSubClass(cls)) {
            if (ax.getSuperClass() instanceof OWLObjectSomeValuesFrom) {
                OWLObjectSomeValuesFrom existing = (OWLObjectSomeValuesFrom) ax.getSuperClass();
                existingSuperClsesToReplace.add(existing);
            }
        }
        // What's changed?

        StringBuilder description = new StringBuilder();
        for (OWLObjectSomeValuesFrom toReplace : existingSuperClsesToReplace) {
            if (!superClsesToSet.contains(toReplace)) {
                // Was there but not any longer
                changes.add(new RemoveAxiom(rootOntology, dataFactory.getOWLSubClassOfAxiom(cls, toReplace)));
                description.append("Removed ");
                description.append(renderingManager.getBrowserText(toReplace.getProperty()));
                description.append(" relationship to ");
                description.append(renderingManager.getBrowserText(toReplace.getFiller()));
                description.append("    ");
            }
        }
        // What do we add?
        for (OWLObjectSomeValuesFrom toSet : superClsesToSet) {
            if (!existingSuperClsesToReplace.contains(toSet)) {
                // Not already there - we're adding it.
                changes.add(new AddAxiom(rootOntology, dataFactory.getOWLSubClassOfAxiom(cls, toSet)));
                description.append("Added ");
                description.append(renderingManager.getBrowserText(toSet.getProperty()));
                description.append(" relationship to ");
                description.append(renderingManager.getBrowserText(toSet.getFiller()));
                description.append("    ");
            }
        }


        if (!changes.isEmpty()) {
            changeManager.applyChanges(userId,
                                       new FixedChangeListGenerator<>(changes),
                                       new FixedMessageChangeDescriptionGenerator<>("Edited relationship values: " + description.toString()));
        }
    }
}
