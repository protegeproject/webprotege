package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/05/15
 */
public interface HasGetRevisionSummary {

    RevisionSummary getRevisionSummary(RevisionNumber revisionNumber);
}
