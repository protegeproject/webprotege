package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetClassFrameAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.UpdateClassFrameAction;
import edu.stanford.bmir.protege.web.client.frame.ClassFrameEditor;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetClassFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.PlainClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateFrameAction;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class ClassFrameEditorManager implements EditorManager<OWLEntityContext, ClassFrame, GetClassFrameAction, GetClassFrameResult> {

    private ClassFrameEditor editor;

    @Inject
    public ClassFrameEditorManager(ClassFrameEditor editor) {
        this.editor = editor;
    }

    @Override
    public EditorView<ClassFrame> getView(OWLEntityContext context) {
        return editor;
    }

    @Override
    public GetClassFrameAction createAction(OWLEntityContext editorContext) {
        return new GetClassFrameAction(editorContext.getEntity().asOWLClass(), editorContext.getProjectId());
    }

    @Override
    public ClassFrame extractObject(GetClassFrameResult result) {
        return result.getFrame();
    }

    @Override
    public UpdateFrameAction createUpdateObjectAction(ClassFrame pristineObject, ClassFrame editedObject, OWLEntityContext editorContext) {
        return new UpdateClassFrameAction(editorContext.getProjectId(),
                                          pristineObject.toPlainFrame(),
                                          editedObject.toPlainFrame());
    }

    @Override
    public String getDescription(OWLEntityContext editorContext) {
        return "Class description";
    }
}
