package edu.stanford.bmir.protege.web.server.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.issues.EntityDiscussionThreadRepository;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.shared.entity.MergedEntityTreatment;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.entity.MergedEntityTreatment.DEPRECATE_MERGED_ENTITY;
import static org.semanticweb.owlapi.vocab.SKOSVocabulary.ALTLABEL;
import static org.semanticweb.owlapi.vocab.SKOSVocabulary.PREFLABEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2018
 *
 * Performs a simple merge of one entity into another entity.  For a source entity, S, and a
 * target entity, T, S will be merged into T.  This involves the following:
 * 1) All usages of S will be replaced by T.
 * 2) Annotation assertion axioms that annotate S with an rdfs:label or a skos:prefLabel will
 *    be removed and replaced with annotation assertions that annotate T with the value of the
 *    annotations on S but with the property switched to skos:altLabel.  For example,
 *    AnnotationAssertion(rdfs:label S "Blah"@en) will be replaced with
 *    AnnotationAssertion(skos:altLabel S "Blah"@en).
 * 3) If the treatement is deprecation, then an annotation assertion is added to deprecate S
 *    and original annotations on S are preserved.  If the treatment is deletion then any
 *    discussion threads on S will be copied over to T.
 */
public class MergeEntitiesChangeListGenerator implements ChangeListGenerator<OWLEntity> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final ImmutableSet<OWLEntity> sourceEntities;

    @Nonnull
    private final OWLEntity targetEntity;

    @Nonnull
    private final MergedEntityTreatment treatment;

    @Nonnull
    private final EntityDiscussionThreadRepository discussionThreadRepository;

    @Nonnull
    private final String commitMessage;

    @Nonnull
    private final EntityRenamer entityRenamer;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologies;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertions;

    @AutoFactory
    @Inject
    public MergeEntitiesChangeListGenerator(@Nonnull ImmutableSet<OWLEntity> sourceEntities,
                                            @Nonnull OWLEntity targetEntity,
                                            @Nonnull MergedEntityTreatment treatment,
                                            @Nonnull String commitMessage,
                                            @Provided @Nonnull ProjectId projectId,
                                            @Provided @Nonnull OWLDataFactory dataFactory,
                                            @Provided @Nonnull EntityDiscussionThreadRepository discussionThreadRepository,
                                            @Provided @Nonnull EntityRenamer entityRenamer,
                                            @Provided @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                            @Provided @Nonnull ProjectOntologiesIndex projectOntologies,
                                            @Provided @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertions) {
        this.projectId = checkNotNull(projectId);
        this.dataFactory = checkNotNull(dataFactory);
        this.sourceEntities = checkNotNull(sourceEntities);
        this.targetEntity = checkNotNull(targetEntity);
        this.treatment = checkNotNull(treatment);
        this.discussionThreadRepository = checkNotNull(discussionThreadRepository);
        this.commitMessage = checkNotNull(commitMessage);
        this.entityRenamer = checkNotNull(entityRenamer);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
        this.projectOntologies = checkNotNull(projectOntologies);
        this.annotationAssertions = checkNotNull(annotationAssertions);
    }

    @Override
    public OntologyChangeList<OWLEntity> generateChanges(ChangeGenerationContext context) {

        // Generate changes to perform a merge.  The order of the generation of these changes
        // is important.  Usage changes must be generated first.
        OntologyChangeList.Builder<OWLEntity> builder = OntologyChangeList.builder();

        // Generate changes to replace usage of the entity.  This will essentially merge the
        // entity into the target entity
        replaceUsage(builder);

        // Avoid conflicts with labels.  The merged term must not duplicate preferred labels for
        // a given language.
        replaceLabels(builder);

        // Deprecated, if necessary
        if (treatment == DEPRECATE_MERGED_ENTITY) {
            deprecateSourceEntities(builder);
        }
        else {
            // Copy over discussion threads if the entity is being deleted so that these
            // will still be accessible.
            sourceEntities.forEach(sourceEntity -> discussionThreadRepository.replaceEntity(projectId, sourceEntity, targetEntity));

            // TODO:  Name Map Old IRI to new IRI - we don't have this functionality yet
        }

        return builder.build(targetEntity);

    }

    private void deprecateSourceEntities(OntologyChangeList.Builder<OWLEntity> builder) {
        sourceEntities.forEach(sourceEntity -> {
            // Add an annotation assertion to deprecate the source entity
            var sourceEntityIRI = sourceEntity.getIRI();
            var deprecatedAx = dataFactory.getDeprecatedOWLAnnotationAssertionAxiom(sourceEntityIRI);
            var ontologyId = defaultOntologyIdManager.getDefaultOntologyId();
            var addDeprecatedAxiom = AddAxiomChange.of(ontologyId, deprecatedAx);
            builder.add(addDeprecatedAxiom);

            // Preserve labels and other annotations on the source entity
            projectOntologies.getOntologyIds().forEach(ontId -> {
                annotationAssertions.getAxiomsForSubject(sourceEntityIRI, ontId)
                                    .map(ax -> AddAxiomChange.of(ontId, ax))
                                    .forEach(builder::add);
            });
        });
    }

    private void replaceUsage(OntologyChangeList.Builder<OWLEntity> builder) {
        sourceEntities.forEach(sourceEntity -> {
            var renameChanges = entityRenamer.generateChanges(ImmutableMap.of(sourceEntity, targetEntity.getIRI()));
            builder.addAll(renameChanges);
        });
    }

    private void replaceLabels(@Nonnull OntologyChangeList.Builder<OWLEntity> builder) {
        // Replace rdfs:label with skos:altLabel.
        // Replace skos:prefLabel with skos:altLabel.
        // In both cases, language tags are preserved.
        projectOntologies.getOntologyIds().forEach(ontId -> {
            sourceEntities.forEach(sourceEntity -> {
                var sourceEntityIRI = sourceEntity.getIRI();
                // Get the annotation assertions that were originally on the source entity
                annotationAssertions.getAxiomsForSubject(sourceEntityIRI, ontId)
                                    // Just deal with explicit rdfs:label and skos:prefLabel annotations
                                    .filter(ax -> isRdfsLabelAnnotation(ax) || isSkosPrefLabelAnnotation(ax))
                                    // Replace on the target entity with skos:altLabel as the property
                                    .forEach(ax -> replaceWithSkosAltLabel(ax, ontId, builder));
            });
        });
    }

    /**
     * Replaces the specified annotation assertion on the target entity with an annotation assertion whose
     * property is skos:altLabel.
     *
     * @param ax        The annotation assertion under consideration.  This is the original annotation
     *                  assertion on the source entity (not the target entity).
     * @param ontId        The ontology to make the changes in.
     * @param builder    The builder for adding changes to.
     */
    private void replaceWithSkosAltLabel(@Nonnull OWLAnnotationAssertionAxiom ax,
                                         @Nonnull OWLOntologyID ontId,
                                         @Nonnull OntologyChangeList.Builder<OWLEntity> builder) {
        // Remove the original one (that will be on the target entity by now)
        var origAx = dataFactory.getOWLAnnotationAssertionAxiom(
                targetEntity.getIRI(),
                ax.getAnnotation(),
                ax.getAnnotations());
        builder.add(RemoveAxiomChange.of(ontId, origAx));

        // Generate a new annotation with a property of skos:altLabel.
        // Preserve any annotations on the annotation.
        OWLAnnotation replAnno = dataFactory.getOWLAnnotation(getSkosAltLabel(),
                                                              ax.getAnnotation().getValue(),
                                                              ax.getAnnotation().getAnnotations());
        // Generate a new annotation assertion to replace the original one.
        // Preserve any annotations on the axiom.
        OWLAxiom replAx = dataFactory.getOWLAnnotationAssertionAxiom(
                targetEntity.getIRI(),
                replAnno,
                ax.getAnnotations());
        builder.add(AddAxiomChange.of(ontId, replAx));
    }

    /**
     * Determines if the given annotation assertion axiom provides an rdfs:label
     */
    private boolean isRdfsLabelAnnotation(@Nonnull OWLAnnotationAssertionAxiom ax) {
        return ax.getProperty().isLabel();
    }

    /**
     * Determines if the given annotation assertion axiom provides a skos:prefLabel
     */
    private boolean isSkosPrefLabelAnnotation(@Nonnull OWLAnnotationAssertionAxiom ax) {
        return PREFLABEL.getIRI().equals(ax.getProperty().getIRI());
    }

    @Nonnull
    private OWLAnnotationProperty getSkosAltLabel() {
        return dataFactory.getOWLAnnotationProperty(ALTLABEL.getIRI());
    }

    @Override
    public OWLEntity getRenamedResult(OWLEntity result, RenameMap renameMap) {
        return targetEntity;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<OWLEntity> result) {
        return commitMessage;
    }
}
