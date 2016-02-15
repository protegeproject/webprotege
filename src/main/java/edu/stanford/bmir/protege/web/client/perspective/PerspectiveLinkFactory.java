package edu.stanford.bmir.protege.web.client.perspective;

import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public interface PerspectiveLinkFactory {

    PerspectiveLink createPerspectiveLink(PerspectiveId perspectiveId);
}
