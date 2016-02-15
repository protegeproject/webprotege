package edu.stanford.bmir.protege.web.client.ui.tab;

import edu.stanford.bmir.protege.web.client.perspective.Perspective;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/16
 */
public interface PerspectiveFactory {

    Perspective createPerspective(PerspectiveId perspectiveId);
}
