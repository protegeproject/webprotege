package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionSummary;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLEntityBrowserTextChangeSet;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public class HighLevelEventGenerator {

    private OWLAPIProject project;

    public HighLevelEventGenerator(OWLAPIProject project, UserId userId, RevisionNumber revisionNumber) {
        this.project = project;
    }

    public List<ProjectEvent<?>> getHighLevelEvents(List<OWLOntologyChange> ontologyChanges, RevisionNumber revisionNumber) {
        // A HORRIBLE HORRIBLE stopgap
        final List<ProjectEvent<?>> result = new ArrayList<ProjectEvent<?>>();
        final Set<OWLEntity> changedEntities = new HashSet<OWLEntity>();
        List<OWLOntologyChangeRecord> changeRecords = new ArrayList<OWLOntologyChangeRecord>();
        final Set<IRI> deprecationStatusChangedEntites = new HashSet<IRI>();

        for(OWLOntologyChange change : ontologyChanges) {
            changeRecords.add(OWLOntologyChangeRecord.createFromOWLOntologyChange(change));
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
                    if (subject instanceof IRI) {
                        entities = project.getRootOntology().getEntitiesInSignature((IRI) subject);
                        if(axiom instanceof OWLAnnotationAssertionAxiom) {
                            if(((OWLAnnotationAssertionAxiom) axiom).isDeprecatedIRIAssertion()) {
                                deprecationStatusChangedEntites.add((IRI) ((OWLAnnotationAssertionAxiom) axiom).getSubject());
                            }
                        }
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
                                    return new ClassFrameChangedEvent(cls, project.getProjectId());
                                }

                                @Override
                                public ProjectEvent<?> visit(OWLObjectProperty property) {
                                    return new ObjectPropertyFrameChangedEvent(property, project.getProjectId());
                                }

                                @Override
                                public ProjectEvent<?> visit(OWLDataProperty property) {
                                    return new DataPropertyFrameChangedEvent(property, project.getProjectId());
                                }

                                @Override
                                public ProjectEvent<?> visit(OWLNamedIndividual individual) {
                                    return new NamedIndividualFrameChangedEvent(individual, project.getProjectId());
                                }

                                @Override
                                public ProjectEvent<?> visit(OWLDatatype datatype) {
                                    return new DatatypeFrameChangedEvent(datatype, project.getProjectId());
                                }

                                @Override
                                public ProjectEvent<?> visit(OWLAnnotationProperty property) {
                                    return new AnnotationPropertyFrameChangedEvent(property, project.getProjectId());
                                }
                            });
                            result.add(event);
                        }
                    }
                }

                @Override
                public void visit(RemoveAxiom change) {
                    handleAxiomChange(change.getAxiom());
                }

                @Override
                public void visit(SetOntologyID change) {
                    result.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), project.getProjectId()));
                }

                @Override
                public void visit(AddImport change) {
                    result.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), project.getProjectId()));
                }

                @Override
                public void visit(RemoveImport change) {
                    result.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), project.getProjectId()));
                }

                @Override
                public void visit(AddOntologyAnnotation change) {
                    result.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), project.getProjectId()));
                }

                @Override
                public void visit(RemoveOntologyAnnotation change) {
                    result.add(new OntologyFrameChangedEvent(change.getOntology().getOntologyID(), project.getProjectId()));
                }
            });
        }

        for(IRI entityIRI : deprecationStatusChangedEntites) {
            for (OWLEntity entity : project.getRootOntology().getEntitiesInSignature(entityIRI, true)) {
                boolean deprecated = project.isDeprecated(entity);
                result.add(new EntityDeprecatedChangedEvent(project.getProjectId(), entity, deprecated));
            }
        }

        Set<OWLEntity> changedBrowserTexts = new HashSet<OWLEntity>();
        List<OWLEntityBrowserTextChangeSet> browserTextChangeSets = project.getOWLEntityEditorKit().getChangedEntities(changeRecords);
        for(int i = browserTextChangeSets.size() - 1; i > -1; i--) {
            final OWLEntityBrowserTextChangeSet o = browserTextChangeSets.get(i);
            if(!changedBrowserTexts.contains(o.getEntity())) {
                changedBrowserTexts.add(o.getEntity());
                result.add(new BrowserTextChangedEvent(o.getEntity(), o.getNewBrowserText(), project.getProjectId()));
            }
        }

        Set<OWLEntityData> changedEntitiesData = new HashSet<OWLEntityData>();
        for (OWLEntity entity : changedEntities) {
            String browserText = project.getRenderingManager().getBrowserText(entity);
            changedEntitiesData.add(DataFactory.getOWLEntityData(entity, browserText));
        }
        RevisionSummary revisionSummary = project.getChangeManager().getRevisionSummary(revisionNumber);
        ProjectEvent<?> event = new ProjectChangedEvent(project.getProjectId(), revisionSummary, changedEntitiesData);
        result.add(event);
        return result;
    }










}
