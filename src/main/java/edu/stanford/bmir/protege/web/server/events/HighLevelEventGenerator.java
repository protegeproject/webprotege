package edu.stanford.bmir.protege.web.server.events;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.change.HasGetRevisionSummary;
import edu.stanford.bmir.protege.web.server.owlapi.change.Revision;
import edu.stanford.bmir.protege.web.shared.BrowserTextProvider;
import edu.stanford.bmir.protege.web.shared.HasGetEntitiesWithIRI;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;

import javax.inject.Inject;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public class HighLevelEventGenerator implements EventTranslator {

    private final ProjectId projectId;

    private final BrowserTextProvider browserTextProvider;

    private final HasGetRevisionSummary hasGetRevisionSummary;

    private final HasGetEntitiesWithIRI hasGetEntitiesWithIRI;

    @Inject
    public HighLevelEventGenerator(ProjectId projectId, BrowserTextProvider browserTextProvider, HasGetEntitiesWithIRI hasGetEntitiesWithIRI, HasGetRevisionSummary hasGetRevisionSummary) {
        this.projectId = projectId;
        this.browserTextProvider = browserTextProvider;
        this.hasGetEntitiesWithIRI = hasGetEntitiesWithIRI;
        this.hasGetRevisionSummary = hasGetRevisionSummary;
    }

    @Override
    public void prepareForOntologyChanges(List<OWLOntologyChange> submittedChanges) {

    }

    @Override
    public void translateOntologyChanges(Revision revision, List<OWLOntologyChange> appliedChanges, final List<ProjectEvent<?>> projectEventList) {
        // TODO: NEED TIDYING AND SPLITTING UP
        final Set<OWLEntity> changedEntities = new HashSet<>();

        for(OWLOntologyChange change : appliedChanges) {
            change.accept(new OWLOntologyChangeVisitor() {
                @Override
                public void visit(AddAxiom change) {
                    final OWLAxiom axiom = change.getAxiom();
                    handleAxiomChange(axiom);
                }

                private void handleAxiomChange(OWLAxiom axiom) {
                    AxiomSubjectProvider p = new AxiomSubjectProvider();
                    OWLObject subject = p.getSubject(axiom);
                    Set<OWLEntity> entities;
                    if(subject instanceof IRI) {
                        entities = hasGetEntitiesWithIRI.getEntitiesWithIRI((IRI) subject);
                    }
                    else if (subject instanceof OWLEntity) {
                        entities = Collections.singleton((OWLEntity) subject);
                    }
                    else {
                        entities = Collections.emptySet();
                    }
                    for (OWLEntity e : entities) {
                        if (!changedEntities.contains(e)) {
                            changedEntities.add(e);
                            ProjectEvent<?> event = e.accept(new OWLEntityVisitorEx<ProjectEvent<?>>() {
                                @Override
                                public ProjectEvent<?> visit(OWLClass cls) {
                                    return new ClassFrameChangedEvent(cls, projectId);
                                }

                                @Override
                                public ProjectEvent<?> visit(OWLObjectProperty property) {
                                    return new ObjectPropertyFrameChangedEvent(property, projectId);
                                }

                                @Override
                                public ProjectEvent<?> visit(OWLDataProperty property) {
                                    return new DataPropertyFrameChangedEvent(property, projectId);
                                }

                                @Override
                                public ProjectEvent<?> visit(OWLNamedIndividual individual) {
                                    return new NamedIndividualFrameChangedEvent(individual, projectId);
                                }

                                @Override
                                public ProjectEvent<?> visit(OWLDatatype datatype) {
                                    return new DatatypeFrameChangedEvent(datatype, projectId);
                                }

                                @Override
                                public ProjectEvent<?> visit(OWLAnnotationProperty property) {
                                    return new AnnotationPropertyFrameChangedEvent(property, projectId);
                                }
                            });
                            projectEventList.add(event);
                        }
                    }
                }

                @Override
                public void visit(RemoveAxiom change) {
                    handleAxiomChange(change.getAxiom());
                }

                @Override
                public void visit(SetOntologyID change) {
                    projectEventList.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), projectId));
                }

                @Override
                public void visit(AddImport change) {
                    projectEventList.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), projectId));
                }

                @Override
                public void visit(RemoveImport change) {
                    projectEventList.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), projectId));
                }

                @Override
                public void visit(AddOntologyAnnotation change) {
                    projectEventList.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), projectId));
                }

                @Override
                public void visit(RemoveOntologyAnnotation change) {
                    projectEventList.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), projectId));
                }
            });
        }


        Set<OWLEntityData> changedEntitiesData = new HashSet<>();
        for (OWLEntity entity : changedEntities) {
            Optional<String> browserText = browserTextProvider.getOWLEntityBrowserText(entity);
            changedEntitiesData.add(DataFactory.getOWLEntityData(entity, browserText.or("")));
        }
        Optional<RevisionSummary> revisionSummary = hasGetRevisionSummary.getRevisionSummary(revision.getRevisionNumber());
        if (revisionSummary.isPresent()) {
            ProjectEvent<?> event = new ProjectChangedEvent(projectId, revisionSummary.get(), changedEntitiesData);
            projectEventList.add(event);
        }
    }
}
