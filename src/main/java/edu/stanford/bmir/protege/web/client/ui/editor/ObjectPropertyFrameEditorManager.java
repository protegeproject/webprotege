package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.client.ui.frame.ObjectPropertyFrameEditor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class ObjectPropertyFrameEditorManager implements EditorManager<OWLEntityDataContext, LabelledFrame<ObjectPropertyFrame>> {

    private ObjectPropertyFrameEditor editor;

    private ProjectId projectId;

    private final DispatchServiceManager dispatchServiceManager;

    public ObjectPropertyFrameEditorManager(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public EditorView<LabelledFrame<ObjectPropertyFrame>> getView(OWLEntityDataContext editorContext) {
        if (editor == null || !projectId.equals(editorContext.getProjectId())) {
            projectId = editorContext.getProjectId();
            editor = new ObjectPropertyFrameEditor(editorContext.getProjectId(), dispatchServiceManager);
        }
        return editor;
    }

    @Override
    public GetObjectAction<LabelledFrame<ObjectPropertyFrame>> createGetObjectAction(OWLEntityDataContext editorContext) {
        return new GetObjectPropertyFrameAction(editorContext.getProjectId(), editorContext.getEntityData().getEntity().asOWLObjectProperty());
    }

    @Override
    public UpdateObjectAction<LabelledFrame<ObjectPropertyFrame>> createUpdateObjectAction(LabelledFrame<ObjectPropertyFrame> pristineObject, LabelledFrame<ObjectPropertyFrame> editedObject, OWLEntityDataContext editorContext) {
        return new UpdateObjectPropertyFrameAction(editorContext.getProjectId(), pristineObject, editedObject);
    }

    @Override
    public String getDescription(OWLEntityDataContext editorContext) {
        return "Object property description for " + editorContext.getEntityData().getBrowserText();
    }
}
