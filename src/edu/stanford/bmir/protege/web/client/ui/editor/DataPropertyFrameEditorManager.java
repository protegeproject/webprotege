package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.client.ui.frame.DataPropertyFrameEditor;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.DataPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.UpdateDataPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class DataPropertyFrameEditorManager implements EditorManager<OWLEntityDataContext, LabelledFrame<DataPropertyFrame>> {

    private DataPropertyFrameEditor editor;

    private ProjectId projectId;

    @Override
    public EditorView<LabelledFrame<DataPropertyFrame>> getView(OWLEntityDataContext editorContext) {
        if (editor == null || !projectId.equals(editorContext.getProjectId())) {
            projectId = editorContext.getProjectId();
            editor = new DataPropertyFrameEditor(editorContext.getProjectId());
        }
        return editor;
    }

    @Override
    public GetObjectAction<LabelledFrame<DataPropertyFrame>> createGetObjectAction(OWLEntityDataContext editorContext) {
        return new GetDataPropertyFrameAction(editorContext.getProjectId(), editorContext.getEntityData().getEntity().asOWLDataProperty());
    }

    @Override
    public UpdateObjectAction<LabelledFrame<DataPropertyFrame>> createUpdateObjectAction(LabelledFrame<DataPropertyFrame> pristineObject, LabelledFrame<DataPropertyFrame> editedObject, OWLEntityDataContext editorContext) {
        return new UpdateDataPropertyFrameAction(editorContext.getProjectId(), pristineObject, editedObject);
    }

    @Override
    public String getDescription(OWLEntityDataContext editorContext) {
        return "Data property description for " + editorContext.getEntityData().getBrowserText();
    }
}
