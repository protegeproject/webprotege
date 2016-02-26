package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
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
public class DataPropertyFrameEditorManager implements EditorManager<OWLEntityContext, LabelledFrame<DataPropertyFrame>> {

    private final DataPropertyFrameEditor editor;

    private final ProjectId projectId;

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public DataPropertyFrameEditorManager(DataPropertyFrameEditor editor, ProjectId projectId, DispatchServiceManager dispatchServiceManager) {
        this.editor = editor;
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public EditorView<LabelledFrame<DataPropertyFrame>> getView(OWLEntityContext editorContext) {
        return editor;
    }

    @Override
    public GetObjectAction<LabelledFrame<DataPropertyFrame>> createGetObjectAction(OWLEntityContext editorContext) {
        return new GetDataPropertyFrameAction(editorContext.getProjectId(), editorContext.getEntity().asOWLDataProperty());
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
