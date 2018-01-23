package edu.stanford.bmir.protege.web.client.change;

import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
public interface RevertRevisionHandler {

    void handleRevertRevision(RevisionNumber revisionNumber);
}
