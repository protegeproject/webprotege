package edu.stanford.bmir.protege.web.client.perspective;

import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/16
 */
public interface PerspectiveFactory {

    /**
     * Create a perspective for the specified perspectiveId.
     * @param perspectiveId The perspectiveId.  Not {@code null}.
     * @return The perspective.
     */
    Perspective createPerspective(PerspectiveId perspectiveId);
}
