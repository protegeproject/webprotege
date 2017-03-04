package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/05/15
 */
public class EventTranslatorManager {

    private Collection<EventTranslator> eventTranslators;

    @Inject
    public EventTranslatorManager(Set<EventTranslator> eventTranslators) {
        this.eventTranslators = eventTranslators;
    }

    public void prepareForOntologyChanges(List<OWLOntologyChange> submittedChanges) {
        for(EventTranslator eventTranslator : eventTranslators) {
            eventTranslator.prepareForOntologyChanges(submittedChanges);
        }
    }

    public void translateOntologyChanges(Revision revision, List<OWLOntologyChange> appliedChanges, List<ProjectEvent<?>> projectEventList) {
        for(EventTranslator eventTranslator : eventTranslators) {
            eventTranslator.translateOntologyChanges(revision, appliedChanges, projectEventList);
        }
    }
}
