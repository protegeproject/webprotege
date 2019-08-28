package edu.stanford.bmir.protege.web.server.bulkop;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.EntitiesInOntologySignatureIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class SetAnnotationValueActionChangeListGenerator implements ChangeListGenerator<Set<OWLEntity>> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final ImmutableSet<OWLEntity> entities;

    @Nonnull
    private final OWLAnnotationProperty property;

    @Nonnull
    private final OWLAnnotationValue value;

    @Nonnull
    private final String commitMessage;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final EntitiesInOntologySignatureIndex entitiesInSignature;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionBySubject;

    @AutoFactory
    public SetAnnotationValueActionChangeListGenerator(@Provided @Nonnull OWLDataFactory dataFactory,
                                                       @Nonnull ImmutableSet<OWLEntity> entities,
                                                       @Nonnull OWLAnnotationProperty property,
                                                       @Nonnull OWLAnnotationValue value,
                                                       @Nonnull String commitMessage,
                                                       @Provided @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                       @Provided @Nonnull EntitiesInOntologySignatureIndex entitiesInSignature,
                                                       @Provided @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionBySubject) {
        this.dataFactory = checkNotNull(dataFactory);
        this.entities = checkNotNull(entities);
        this.property = checkNotNull(property);
        this.value = checkNotNull(value);
        this.commitMessage = checkNotNull(commitMessage);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.entitiesInSignature = checkNotNull(entitiesInSignature);
        this.annotationAssertionBySubject = checkNotNull(annotationAssertionBySubject);
    }

    @Override
    public OntologyChangeList<Set<OWLEntity>> generateChanges(ChangeGenerationContext context) {
        var builder = OntologyChangeList.<Set<OWLEntity>>builder();
        projectOntologiesIndex.getOntologyIds()
                              .forEach(ontId -> generateSetAnnotationValueChangesForOntology(builder, ontId));
        return builder.build(entities);
    }

    private void generateSetAnnotationValueChangesForOntology(OntologyChangeList.Builder<Set<OWLEntity>> builder,
                                                              OWLOntologyID ontId) {
        entities.stream()
                .filter(entity -> entitiesInSignature.containsEntityInSignature(entity, ontId))
                .map(OWLEntity::getIRI)
                .flatMap(subject -> annotationAssertionBySubject.getAxiomsForSubject(subject, ontId))
                .filter(ax -> ax.getProperty()
                                .equals(property))
                .forEach(ax -> generateSetAnnotationValueChangesForAxiom(builder, ontId, ax));
    }

    private void generateSetAnnotationValueChangesForAxiom(OntologyChangeList.Builder<Set<OWLEntity>> builder,
                                                           OWLOntologyID ontId,
                                                           OWLAnnotationAssertionAxiom ax) {
        // Replacement annotations that set the value go into the ontology where the value was originally located
        var removeAxiom = RemoveAxiomChange.of(ontId, ax);
        builder.add(removeAxiom);
        // Copy over axiom annotations
        var subject = ax.getSubject();
        var preservedAxiomAnnotations = ax.getAnnotations();
        var replacementAx = dataFactory.getOWLAnnotationAssertionAxiom(property,
                                                                       subject,
                                                                       value,
                                                                       preservedAxiomAnnotations);
        var replacementAxiom = AddAxiomChange.of(ontId, replacementAx);
        builder.add(replacementAxiom);
    }

    @Override
    public Set<OWLEntity> getRenamedResult(Set<OWLEntity> result, RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Set<OWLEntity>> result) {
        return commitMessage;
    }
}
