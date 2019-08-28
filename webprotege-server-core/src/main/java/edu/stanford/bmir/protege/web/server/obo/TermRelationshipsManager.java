package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.SubClassOfAxiomsBySubClassIndex;
import edu.stanford.bmir.protege.web.server.project.chg.ChangeManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.util.ClassExpression;
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class TermRelationshipsManager {


    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassAxiomsIndex;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final RelationshipConverter relationshipConverter;

    @Inject
    public TermRelationshipsManager(@Nonnull OWLDataFactory dataFactory,
                                    @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                    @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex,
                                    @Nonnull SubClassOfAxiomsBySubClassIndex subClassAxiomsIndex,
                                    @Nonnull RenderingManager renderingManager,
                                    @Nonnull ChangeManager changeManager,
                                    @Nonnull RelationshipConverter relationshipConverter) {
        this.dataFactory = dataFactory;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.annotationAssertionsIndex = annotationAssertionsIndex;
        this.subClassAxiomsIndex = subClassAxiomsIndex;
        this.renderingManager = renderingManager;
        this.changeManager = changeManager;
        this.relationshipConverter = relationshipConverter;
    }

    public OBOTermRelationships getRelationships(OWLClass term) {
        OWLClass cls = dataFactory.getOWLClass(term.getIRI());
        var relationships = projectOntologiesIndex.getOntologyIds()
                .flatMap(ontId -> subClassAxiomsIndex.getSubClassOfAxiomsForSubClass(cls, ontId))
                .map(OWLSubClassOfAxiom::getSuperClass)
                .map(OWLClassExpression::asConjunctSet)
                .filter(OWLObjectSomeValuesFrom.class::isInstance)
                .map(OWLObjectSomeValuesFrom.class::cast)
                .filter(ClassExpression::isPropertyNamed)
                .filter(ClassExpression::isFillerOwlClass)
                .map(toOboRelationship())
                .collect(Collectors.toSet());
        return new OBOTermRelationships(relationships);
    }

    private Function<OWLObjectSomeValuesFrom, OBORelationship> toOboRelationship() {
        return svf -> new OBORelationship(renderingManager.getObjectPropertyData(svf.getProperty().asOWLObjectProperty()), renderingManager.getClassData(svf.getFiller().asOWLClass()));
    }

    public void setRelationships(@Nonnull UserId userId,
                                 @Nonnull OWLClass lastEntity,
                                 @Nonnull OBOTermRelationships relationships) {
        List<OntologyChange> changes = null;
        OWLClass cls = null;
        StringBuilder description = null;
        Set<OWLOntologyID> ontologyIds = projectOntologiesIndex.getOntologyIds().collect(Collectors.toSet());
        for(OWLOntologyID ontId : ontologyIds) {
            Set<OWLObjectSomeValuesFrom> superClsesToSet = new HashSet<>();
            for (OBORelationship relationship : relationships.getRelationships()) {
                OWLObjectSomeValuesFrom someValuesFrom = relationshipConverter.toSomeValuesFrom(relationship);
                superClsesToSet.add(someValuesFrom);
            }


            changes = new ArrayList<>();

            cls = dataFactory.getOWLClass(lastEntity.getIRI());
            Set<OWLObjectSomeValuesFrom> existingSuperClsesToReplace = new HashSet<>();
            subClassAxiomsIndex.getSubClassOfAxiomsForSubClass(cls, ontId)
                    .map(OWLSubClassOfAxiom::getSuperClass)
                    .filter(OWLObjectSomeValuesFrom.class::isInstance)
                    .map(OWLObjectSomeValuesFrom.class::cast)
                    .forEach(existingSuperClsesToReplace::add);

            // What's changed?

            description = new StringBuilder();
            for (OWLObjectSomeValuesFrom toReplace : existingSuperClsesToReplace) {
                if (!superClsesToSet.contains(toReplace)) {
                    // Was there but not any longer
                    changes.add(RemoveAxiomChange.of(ontId, dataFactory.getOWLSubClassOfAxiom(cls, toReplace)));
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
                    changes.add(AddAxiomChange.of(ontId, dataFactory.getOWLSubClassOfAxiom(cls, toSet)));
                    description.append("Added ");
                    description.append(renderingManager.getBrowserText(toSet.getProperty()));
                    description.append(" relationship to ");
                    description.append(renderingManager.getBrowserText(toSet.getFiller()));
                    description.append("    ");
                }
            }
        }


        if (!changes.isEmpty()) {
            changeManager.applyChanges(userId,
                                       new FixedChangeListGenerator<>(changes, cls, "Edited relationship values: " + description.toString()));
        }
    }
}
