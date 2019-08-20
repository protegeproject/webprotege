package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.HasGetRevisionSummary;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureIndex;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public class HighLevelEventGenerator implements EventTranslator {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final HasGetRevisionSummary hasGetRevisionSummary;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesByIri;

    @Nonnull
    private final EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex;

    @Inject
    public HighLevelEventGenerator(@Nonnull ProjectId projectId,
                                   @Nonnull RenderingManager renderingManager,
                                   @Nonnull EntitiesInProjectSignatureByIriIndex entitiesByIri,
                                   @Nonnull HasGetRevisionSummary hasGetRevisionSummary,
                                   @Nonnull EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex) {
        this.projectId = checkNotNull(projectId);
        this.renderingManager = checkNotNull(renderingManager);
        this.entitiesByIri = checkNotNull(entitiesByIri);
        this.hasGetRevisionSummary = checkNotNull(hasGetRevisionSummary);
        this.entitiesInProjectSignatureIndex = checkNotNull(entitiesInProjectSignatureIndex);
    }

    @Override
    public void prepareForOntologyChanges(List<OWLOntologyChange> submittedChanges) {
    }

    @Override
    public void translateOntologyChanges(Revision revision,
                                         ChangeApplicationResult<?> changes,
                                         final List<ProjectEvent<?>> projectEventList) {
        // TODO: NEED TIDYING AND SPLITTING UP
        final Set<OWLEntity> changedEntities = new HashSet<>();
        for(OWLOntologyChange change : changes.getChangeList()) {
            change.accept(new OWLOntologyChangeVisitor() {
                @Override
                public void visit(@Nonnull AddAxiom change) {
                    final OWLAxiom axiom = change.getAxiom();
                    handleAxiomChange(axiom);
                }

                @Override
                public void visit(@Nonnull RemoveAxiom change) {
                    handleAxiomChange(change.getAxiom());
                }

                @Override
                public void visit(@Nonnull SetOntologyID change) {
                    projectEventList.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), projectId));
                }

                @Override
                public void visit(@Nonnull AddImport change) {
                    projectEventList.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), projectId));
                }

                @Override
                public void visit(@Nonnull RemoveImport change) {
                    projectEventList.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), projectId));
                }

                @Override
                public void visit(@Nonnull AddOntologyAnnotation change) {
                    projectEventList.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), projectId));
                }

                @Override
                public void visit(@Nonnull RemoveOntologyAnnotation change) {
                    projectEventList.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), projectId));
                }

                private void handleAxiomChange(OWLAxiom axiom) {
                    var axiomSubjectProvider = new AxiomSubjectProvider();
                    var subject = axiomSubjectProvider.getSubject(axiom);
                    var entities = getEntitiesForSubject(subject);
                    for (OWLEntity e : entities) {
                        if (!changedEntities.contains(e)) {
                            changedEntities.add(e);
                            ProjectEvent<?> event = e.accept(new OWLEntityVisitorEx<ProjectEvent<?>>() {
                                @Nonnull
                                @Override
                                public ProjectEvent<?> visit(@Nonnull OWLClass cls) {
                                    return new ClassFrameChangedEvent(cls, projectId, revision.getUserId());
                                }

                                @Nonnull
                                @Override
                                public ProjectEvent<?> visit(@Nonnull OWLObjectProperty property) {
                                    return new ObjectPropertyFrameChangedEvent(property, projectId, revision.getUserId());
                                }

                                @Nonnull
                                @Override
                                public ProjectEvent<?> visit(@Nonnull OWLDataProperty property) {
                                    return new DataPropertyFrameChangedEvent(property, projectId, revision.getUserId());
                                }

                                @Nonnull
                                @Override
                                public ProjectEvent<?> visit(@Nonnull OWLNamedIndividual individual) {
                                    return new NamedIndividualFrameChangedEvent(individual, projectId, revision.getUserId());
                                }

                                @Nonnull
                                @Override
                                public ProjectEvent<?> visit(@Nonnull OWLDatatype datatype) {
                                    return new DatatypeFrameChangedEvent(datatype, projectId, revision.getUserId());
                                }

                                @Nonnull
                                @Override
                                public ProjectEvent<?> visit(@Nonnull OWLAnnotationProperty property) {
                                    return new AnnotationPropertyFrameChangedEvent(property, projectId, revision.getUserId());
                                }
                            });
                            projectEventList.add(event);
                        }
                    }
                }

                private Set<OWLEntity> getEntitiesForSubject(OWLObject subject) {
                    Set<OWLEntity> entities;
                    if(subject instanceof IRI) {
                        entities = entitiesByIri.getEntityInSignature((IRI) subject).collect(Collectors.toSet());
                    }
                    else if (subject instanceof OWLEntity) {
                        entities = Collections.singleton((OWLEntity) subject);
                    }
                    else {
                        entities = Collections.emptySet();
                    }
                    return entities;
                }
            });
        }


        var changedEntitiesData = new HashSet<OWLEntityData>();
        var subject = changes.getSubject();
        if(subject instanceof OWLEntity) {
            var entity = (OWLEntity) subject;
            if (entitiesInProjectSignatureIndex.containsEntityInSignature(entity)) {
                changedEntitiesData.add(renderingManager.getRendering(entity));
            }
        }
        else if(subject instanceof OWLEntityData) {
            var entityData = (OWLEntityData) subject;
            if (entitiesInProjectSignatureIndex.containsEntityInSignature(entityData.getEntity())) {
                changedEntitiesData.add(entityData);
            }
        }
        else if(subject instanceof Collection) {
            var collection = (Collection<?>) subject;
            collection.stream()
                      .filter(element -> element instanceof OWLEntity)
                      .map(element -> (OWLEntity) element)
                      .filter(entitiesInProjectSignatureIndex::containsEntityInSignature)
                      .forEach(entity -> changedEntitiesData.add(renderingManager.getRendering(entity)));

        }
        var revisionSummary = hasGetRevisionSummary.getRevisionSummary(revision.getRevisionNumber());
        if (revisionSummary.isPresent()) {
            var event = new ProjectChangedEvent(projectId, revisionSummary.get(), changedEntitiesData);
            projectEventList.add(event);
        }
    }
}
