package edu.stanford.bmir.protege.web.client.perspective;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */

import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/05/2014
 */
public interface PerspectiveLinkManager {


    public static interface Callback {
        void handlePerspectives(List<PerspectiveId> perspectiveIds);
    }

    void getLinkedPerspectives(Callback callback);

    void removeLinkedPerspective(PerspectiveId perspectiveId, Callback callback);

    void addLinkedPerspective(PerspectiveId perspectiveId, Callback callback);
}

