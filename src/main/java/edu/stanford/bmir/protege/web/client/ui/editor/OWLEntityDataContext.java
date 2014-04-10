package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class OWLEntityDataContext extends EditorCtx {

    private OWLEntityData entityData;

    public OWLEntityDataContext(ProjectId projectId, OWLEntityData entityData) {
        super(projectId);
        this.entityData = entityData;
    }

    public OWLEntityData getEntityData() {
        return entityData;
    }
}
