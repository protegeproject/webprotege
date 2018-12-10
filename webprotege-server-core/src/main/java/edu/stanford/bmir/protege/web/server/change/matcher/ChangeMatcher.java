package edu.stanford.bmir.protege.web.server.change.matcher;

import org.semanticweb.owlapi.change.OWLOntologyChangeData;

import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public interface ChangeMatcher {

    Optional<ChangeSummary> getDescription(List<OWLOntologyChangeData> changeData);
}
