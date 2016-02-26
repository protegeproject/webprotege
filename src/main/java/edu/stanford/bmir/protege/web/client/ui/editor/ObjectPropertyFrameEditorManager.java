package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.client.ui.frame.ObjectPropertyFrameEditor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateObjectPropertyFrameAction;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class ObjectPropertyFrameEditorManager implements EditorManager<OWLEntityContext, LabelledFrame<ObjectPropertyFrame>> {

    private final ObjectPropertyFrameEditor editor;

    @Inject
    public ObjectPropertyFrameEditorManager(ObjectPropertyFrameEditor editor) {
        this.editor = editor;
    }

    @Override
    public EditorView<LabelledFrame<ObjectPropertyFrame>> getView(OWLEntityContext editorContext) {
        return editor;
    }

    @Override
    public GetObjectAction<LabelledFrame<ObjectPropertyFrame>> createGetObjectAction(OWLEntityContext editorContext) {
        return new GetObjectPropertyFrameAction(editorContext.getProjectId(), editorContext.getEntity().asOWLObjectProperty());
    }

    @Override
    public UpdateObjectAction<LabelledFrame<ObjectPropertyFrame>> createUpdateObjectAction(LabelledFrame<ObjectPropertyFrame> pristineObject, LabelledFrame<ObjectPropertyFrame> editedObject, OWLEntityContext editorContext) {
        return new UpdateObjectPropertyFrameAction(editorContext.getProjectId(), pristineObject, editedObject);
    }

    @Override
    public String getDescription(OWLEntityContext editorContext) {
        return "Object property description";
    }
}
