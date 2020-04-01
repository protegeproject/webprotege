package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.client.frame.DataPropertyFrameEditor;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.*;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class DataPropertyFrameEditorManager implements EditorManager<OWLEntityContext, DataPropertyFrame, GetDataPropertyFrameAction, GetDataPropertyFrameResult> {

    private final DataPropertyFrameEditor editor;

    @Inject
    public DataPropertyFrameEditorManager(DataPropertyFrameEditor editor) {
        this.editor = editor;
    }

    @Override
    public EditorView<DataPropertyFrame> getView(OWLEntityContext editorContext) {
        return editor;
    }

    @Override
    public GetDataPropertyFrameAction createAction(OWLEntityContext editorContext) {
        return new GetDataPropertyFrameAction(editorContext.getProjectId(), editorContext.getEntity().asOWLDataProperty());
    }

    @Override
    public DataPropertyFrame extractObject(GetDataPropertyFrameResult result) {
        return result.getFrame();
    }

    @Override
    public UpdateFrameAction createUpdateObjectAction(DataPropertyFrame pristineObject, DataPropertyFrame editedObject, OWLEntityContext editorContext) {
        return new UpdateDataPropertyFrameAction(editorContext.getProjectId(),
                                                 pristineObject.toPlainFrame(),
                                                 editedObject.toPlainFrame());
    }

    @Override
    public String getDescription(OWLEntityContext editorContext) {
        return "Data property description";
    }
}
