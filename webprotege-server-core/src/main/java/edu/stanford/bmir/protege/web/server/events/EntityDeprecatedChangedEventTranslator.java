package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.shared.event.EntityDeprecatedChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.inject.Inject;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/05/15
 */
public class EntityDeprecatedChangedEventTranslator implements EventTranslator {


    private ProjectId projectId;

    private DeprecatedEntityChecker deprecatedEntityChecker;

    private EntitiesInProjectSignatureByIriIndex entitiesByIri;

    @Inject
    public EntityDeprecatedChangedEventTranslator(ProjectId projectId, DeprecatedEntityChecker deprecatedEntityChecker, EntitiesInProjectSignatureByIriIndex entitiesByIri) {
        this.projectId = projectId;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.entitiesByIri = entitiesByIri;
    }


    @Override
    public void prepareForOntologyChanges(List<OWLOntologyChange> submittedChanges) {

    }

    @Override
    public void translateOntologyChanges(Revision revision, ChangeApplicationResult<?> changes, List<ProjectEvent<?>> projectEventList) {
        for(OWLOntologyChange change : changes.getChangeList()) {
            if (change.isAxiomChange()) {
                if (change.getAxiom() instanceof OWLAnnotationAssertionAxiom) {
                    OWLAnnotationAssertionAxiom axiom = (OWLAnnotationAssertionAxiom) change.getAxiom();
                    if (axiom.getProperty().isDeprecated()) {
                        if (axiom.getSubject() instanceof IRI) {
                            IRI subject = (IRI) axiom.getSubject();
                            entitiesByIri.getEntityInSignature(subject)
                                    .map(entity -> {
                                        var deprecated = deprecatedEntityChecker.isDeprecated(entity);
                                        return new EntityDeprecatedChangedEvent(projectId, entity, deprecated);
                                    })
                                    .forEach(projectEventList::add);
                        }
                    }
                }
            }
        }
    }
}
