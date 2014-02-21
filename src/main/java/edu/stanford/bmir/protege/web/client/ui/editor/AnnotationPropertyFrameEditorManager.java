package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.client.ui.frame.AnnotationPropertyFrameEditor;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.UpdateAnnotationPropertyFrameAction;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class AnnotationPropertyFrameEditorManager implements EditorManager<OWLEntityDataContext, LabelledFrame<AnnotationPropertyFrame>> {

    private AnnotationPropertyFrameEditor editor;

    @Override
    public String getDescription(OWLEntityDataContext editorContext) {
        return "Annotation property description for " + editorContext.getEntityData().getBrowserText();
    }

    @Override
    public EditorView<LabelledFrame<AnnotationPropertyFrame>> getView(OWLEntityDataContext editorContext) {
        if (editor == null) {
            editor = new AnnotationPropertyFrameEditor(editorContext.getProjectId());
        }
        return editor;
    }

    @Override
    public GetObjectAction<LabelledFrame<AnnotationPropertyFrame>> createGetObjectAction(OWLEntityDataContext editorContext) {
        return new GetAnnotationPropertyFrameAction(editorContext.getEntityData().getEntity().asOWLAnnotationProperty(), editorContext.getProjectId());
    }

    @Override
    public UpdateObjectAction<LabelledFrame<AnnotationPropertyFrame>> createUpdateObjectAction(LabelledFrame<AnnotationPropertyFrame> pristineObject, LabelledFrame<AnnotationPropertyFrame> editedObject, OWLEntityDataContext editorContext) {
        return new UpdateAnnotationPropertyFrameAction(editorContext.getProjectId(), pristineObject, editedObject);
    }
}
