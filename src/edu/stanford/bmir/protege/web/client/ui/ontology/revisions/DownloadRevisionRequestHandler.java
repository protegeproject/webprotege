package edu.stanford.bmir.protege.web.client.ui.ontology.revisions;

import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/04/2013
 */
public interface DownloadRevisionRequestHandler {

    void handleDownloadRevisionRequest(RevisionNumber revisionNumber);
}
