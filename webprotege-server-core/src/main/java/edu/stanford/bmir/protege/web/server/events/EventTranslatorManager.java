package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

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

    public void prepareForOntologyChanges(List<OntologyChange> submittedChanges) {
        for(EventTranslator eventTranslator : eventTranslators) {
            eventTranslator.prepareForOntologyChanges(submittedChanges);
        }
    }

    public void translateOntologyChanges(Revision revision, ChangeApplicationResult<?> appliedChanges, List<ProjectEvent<?>> projectEventList) {
        for(EventTranslator eventTranslator : eventTranslators) {
            eventTranslator.translateOntologyChanges(revision, appliedChanges, projectEventList);
        }
    }
}
