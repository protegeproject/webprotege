package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/05/15
 */
public interface EventTranslator {

    /**
     * Prepare this translator for the following ontology changes.
     * @param submittedChanges The list of ontology changes to be submitted.  The actual changes that will be applied
     *                         will either be this list or a subset of this list.
     */
    void prepareForOntologyChanges(List<OWLOntologyChange> submittedChanges);

    /**
     * Translate the ontology changes that were applied to high level project events.
     * @param revision The revision
     * @param appliedChanges The applied changes.
     * @param projectEventList A list to be filled with high level project events that were generated from the changes.
     */
    void translateOntologyChanges(Revision revision, List<OWLOntologyChange> appliedChanges, List<ProjectEvent<?>> projectEventList);
}

