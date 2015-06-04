package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/06/15
 */
public interface HasApplyChangeRecords {

    void applyChangeRecords(UserId userId, List<OWLOntologyChangeRecord> changeRecords, String description);
}
