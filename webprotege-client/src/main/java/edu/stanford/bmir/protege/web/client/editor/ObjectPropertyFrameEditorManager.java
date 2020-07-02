package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.client.frame.ObjectPropertyFrameEditor;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateObjectPropertyFrameAction;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class ObjectPropertyFrameEditorManager implements EditorManager<OWLEntityContext, ObjectPropertyFrame, GetObjectPropertyFrameAction, GetObjectPropertyFrameResult> {

    private final ObjectPropertyFrameEditor editor;

    @Inject
    public ObjectPropertyFrameEditorManager(ObjectPropertyFrameEditor editor) {
        this.editor = editor;
    }

    @Override
    public EditorView<ObjectPropertyFrame> getView(OWLEntityContext editorContext) {
        return editor;
    }

    @Override
    public GetObjectPropertyFrameAction createAction(OWLEntityContext editorContext) {
        return new GetObjectPropertyFrameAction(editorContext.getProjectId(), editorContext.getEntity().asOWLObjectProperty());
    }

    @Override
    public ObjectPropertyFrame extractObject(GetObjectPropertyFrameResult result) {
        return result.getFrame();
    }

    @Override
    public UpdateObjectPropertyFrameAction createUpdateObjectAction(ObjectPropertyFrame pristineObject, ObjectPropertyFrame editedObject, OWLEntityContext editorContext) {
        return new UpdateObjectPropertyFrameAction(editorContext.getProjectId(),
                                                   pristineObject.toPlainFrame(),
                                                   editedObject.toPlainFrame());
    }

    @Override
    public String getDescription(OWLEntityContext editorContext) {
        return "Object property description";
    }
}
