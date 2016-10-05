package edu.stanford.bmir.protege.web.client.perspective;

import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public class PerspectiveLinkFactory {

    @Inject
    public PerspectiveLinkFactory() {
    }

    PerspectiveLink createPerspectiveLink(PerspectiveId perspectiveId) {
        return new PerspectiveLinkImpl(perspectiveId);
    }
}
