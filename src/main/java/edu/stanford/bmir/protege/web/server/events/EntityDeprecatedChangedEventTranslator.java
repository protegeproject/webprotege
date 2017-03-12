package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.shared.HasGetEntitiesWithIRI;
import edu.stanford.bmir.protege.web.shared.event.EntityDeprecatedChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
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

    private HasGetEntitiesWithIRI hasGetEntitiesWithIRI;

    @Inject
    public EntityDeprecatedChangedEventTranslator(ProjectId projectId, DeprecatedEntityChecker deprecatedEntityChecker, HasGetEntitiesWithIRI hasGetEntitiesWithIRI) {
        this.projectId = projectId;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.hasGetEntitiesWithIRI = hasGetEntitiesWithIRI;
    }


    @Override
    public void prepareForOntologyChanges(List<OWLOntologyChange> submittedChanges) {

    }

    @Override
    public void translateOntologyChanges(Revision revision, List<OWLOntologyChange> appliedChanges, List<ProjectEvent<?>> projectEventList) {
        for(OWLOntologyChange change : appliedChanges) {
            if (change.isAxiomChange()) {
                if (change.getAxiom() instanceof OWLAnnotationAssertionAxiom) {
                    OWLAnnotationAssertionAxiom axiom = (OWLAnnotationAssertionAxiom) change.getAxiom();
                    if (axiom.getProperty().isDeprecated()) {
                        if (axiom.getSubject() instanceof IRI) {
                            IRI subject = (IRI) axiom.getSubject();
                            for (OWLEntity entity : hasGetEntitiesWithIRI.getEntitiesWithIRI(subject)) {
                                boolean deprecated = deprecatedEntityChecker.isDeprecated(entity);
                                projectEventList.add(new EntityDeprecatedChangedEvent(projectId, entity, deprecated));
                            }
                        }
                    }
                }
            }
        }
    }
}
