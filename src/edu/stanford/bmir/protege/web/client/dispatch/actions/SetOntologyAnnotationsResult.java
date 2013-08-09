package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2013
 */
public class SetOntologyAnnotationsResult implements Result, HasEventList<ProjectEvent<?>> {

    private Set<OWLAnnotation> ontologyAnnotations;

    private EventList<ProjectEvent<?>> eventList;

    private SetOntologyAnnotationsResult() {
    }

    public SetOntologyAnnotationsResult(Set<OWLAnnotation> ontologyAnnotations, EventList<ProjectEvent<?>> eventList) {
        this.ontologyAnnotations = new HashSet<OWLAnnotation>(ontologyAnnotations);
        this.eventList = eventList;
    }

    public Set<OWLAnnotation> getOntologyAnnotations() {
        return new HashSet<OWLAnnotation>(ontologyAnnotations);
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }
}
