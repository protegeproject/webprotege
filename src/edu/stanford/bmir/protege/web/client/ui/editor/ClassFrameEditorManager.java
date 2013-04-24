package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetClassFrameAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateClassFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.*;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class ClassFrameEditorManager implements EditorManager<OWLEntityDataContext, LabelledFrame<ClassFrame>> {

    private ClassFrameEditor editor;

    private ProjectId projectId;

    public ClassFrameEditorManager() {

    }

    @Override
    public EditorView<LabelledFrame<ClassFrame>> getView(OWLEntityDataContext context) {
        if (editor == null || !projectId.equals(context.getProjectId())) {
            projectId = context.getProjectId();
            PropertyValueListEditor annotationsEditor = new PropertyValueListEditor(projectId, PropertyValueGridGrammar.getAnnotationsGrammar());
            PropertyValueListEditor propertiesEditor = new PropertyValueListEditor(projectId, PropertyValueGridGrammar.getLogicalPropertiesGrammar());
            editor = new ClassFrameEditor(projectId, annotationsEditor, propertiesEditor);
        }
        GWT.log("Returning class frame editor: " + editor);
        return editor;
    }

    @Override
    public GetObjectAction<LabelledFrame<ClassFrame>> createGetObjectAction(OWLEntityDataContext editorContext) {
        return new GetClassFrameAction(editorContext.getEntityData().getEntity().asOWLClass(), editorContext.getProjectId());
    }

    @Override
    public UpdateObjectAction<LabelledFrame<ClassFrame>> createUpdateObjectAction(LabelledFrame<ClassFrame> pristineObject, LabelledFrame<ClassFrame> editedObject, OWLEntityDataContext editorContext) {
        return new UpdateClassFrameAction(editorContext.getProjectId(), pristineObject, editedObject);
    }

    @Override
    public String getDescription(OWLEntityDataContext editorContext) {
        return "Class description for " + editorContext.getEntityData().getBrowserText();
    }
}
