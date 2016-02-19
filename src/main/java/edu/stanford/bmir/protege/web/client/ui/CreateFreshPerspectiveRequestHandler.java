package edu.stanford.bmir.protege.web.client.ui;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/07/2014
 */
public interface CreateFreshPerspectiveRequestHandler {

    void createFreshPerspective(Callback callback);

    public static interface Callback {
        void createNewPerspective(PerspectiveId perspectiveId);
    }
}

