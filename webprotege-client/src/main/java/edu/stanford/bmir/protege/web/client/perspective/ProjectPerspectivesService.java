package edu.stanford.bmir.protege.web.client.perspective;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */

import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/05/2014
 */
public interface ProjectPerspectivesService {


    interface PerspectiveServiceCallback {
        void handlePerspectives(List<PerspectiveDescriptor> perspectiveIds, Set<PerspectiveId> resettable);
    }

    /**
     * Gets all of the perspectives for the current project and currently logged in user
     */
    void getPerspectives(PerspectiveServiceCallback callback);

    void setPerspectives(List<PerspectiveDescriptor> perspectives,
                         PerspectiveServiceCallback callback);
}

