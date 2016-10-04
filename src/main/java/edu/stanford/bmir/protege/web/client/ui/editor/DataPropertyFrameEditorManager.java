package edu.stanford.bmir.protege.web.client.ui.editor;

import javax.inject.Inject;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.frame.DataPropertyFrameEditor;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.DataPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.UpdateDataPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class DataPropertyFrameEditorManager implements EditorManager<OWLEntityContext, LabelledFrame<DataPropertyFrame>, GetDataPropertyFrameAction, GetDataPropertyFrameResult> {

    private final DataPropertyFrameEditor editor;

    @Inject
    public DataPropertyFrameEditorManager(DataPropertyFrameEditor editor) {
        this.editor = editor;
    }

    @Override
    public EditorView<LabelledFrame<DataPropertyFrame>> getView(OWLEntityContext editorContext) {
        return editor;
    }

    @Override
    public GetDataPropertyFrameAction createAction(OWLEntityContext editorContext) {
        return new GetDataPropertyFrameAction(editorContext.getProjectId(), editorContext.getEntity().asOWLDataProperty());
    }

    @Override
    public LabelledFrame<DataPropertyFrame> extractObject(GetDataPropertyFrameResult result) {
        return result.getFrame();
    }

    @Override
    public UpdateObjectAction<LabelledFrame<DataPropertyFrame>> createUpdateObjectAction(LabelledFrame<DataPropertyFrame> pristineObject, LabelledFrame<DataPropertyFrame> editedObject, OWLEntityContext editorContext) {
        return new UpdateDataPropertyFrameAction(editorContext.getProjectId(), pristineObject, editedObject);
    }

    @Override
    public String getDescription(OWLEntityContext editorContext) {
        return "Data property description";
    }
}
