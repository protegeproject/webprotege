package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.client.frame.AnnotationPropertyFrameEditor;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.UpdateAnnotationPropertyFrameAction;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class AnnotationPropertyFrameEditorManager implements EditorManager<OWLEntityContext, AnnotationPropertyFrame, GetAnnotationPropertyFrameAction, GetAnnotationPropertyFrameResult> {

    private final AnnotationPropertyFrameEditor editor;

    @Inject
    public AnnotationPropertyFrameEditorManager(AnnotationPropertyFrameEditor editor) {
        this.editor = editor;
    }

    @Override
    public String getDescription(OWLEntityContext editorContext) {
        return "Annotation property description";
    }

    @Override
    public EditorView<AnnotationPropertyFrame> getView(OWLEntityContext editorContext) {
        return editor;
    }

    @Override
    public GetAnnotationPropertyFrameAction createAction(OWLEntityContext editorContext) {
        return new GetAnnotationPropertyFrameAction(editorContext.getEntity().asOWLAnnotationProperty(), editorContext.getProjectId());
    }

    @Override
    public AnnotationPropertyFrame extractObject(GetAnnotationPropertyFrameResult result) {
        return result.getFrame();
    }

    @Override
    public UpdateAnnotationPropertyFrameAction createUpdateObjectAction(AnnotationPropertyFrame pristineObject, AnnotationPropertyFrame editedObject, OWLEntityContext editorContext) {
        return new UpdateAnnotationPropertyFrameAction(editorContext.getProjectId(),
                                                       pristineObject.toPlainFrame(),
                                                       editedObject.toPlainFrame());
    }
}
