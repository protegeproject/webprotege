package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetClassFrameAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateClassFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.ClassFrameEditor;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class ClassFrameEditorManager implements EditorManager<OWLEntityContext, LabelledFrame<ClassFrame>> {

    private ClassFrameEditor editor;

    @Inject
    public ClassFrameEditorManager(ClassFrameEditor editor) {
        this.editor = editor;
    }

    @Override
    public EditorView<LabelledFrame<ClassFrame>> getView(OWLEntityContext context) {
        return editor;
    }

    @Override
    public GetObjectAction<LabelledFrame<ClassFrame>> createGetObjectAction(OWLEntityContext editorContext) {
        return new GetClassFrameAction(editorContext.getEntity().asOWLClass(), editorContext.getProjectId());
    }

    @Override
    public UpdateObjectAction<LabelledFrame<ClassFrame>> createUpdateObjectAction(LabelledFrame<ClassFrame> pristineObject, LabelledFrame<ClassFrame> editedObject, OWLEntityContext editorContext) {
        return new UpdateClassFrameAction(editorContext.getProjectId(), pristineObject, editedObject);
    }

    @Override
    public String getDescription(OWLEntityContext editorContext) {
        return "Class description";
    }
}
