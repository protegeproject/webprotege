package edu.stanford.bmir.protege.web.client.perspective;

import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/07/2014
 */
public interface CreateFreshPerspectiveRequestHandler {

    void createFreshPerspective(Callback callback);

    interface Callback {
        void createNewPerspective(PerspectiveDescriptor perspectiveDescriptor);
    }
}

