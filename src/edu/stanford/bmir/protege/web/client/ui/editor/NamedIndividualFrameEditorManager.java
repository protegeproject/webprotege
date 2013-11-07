package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.client.ui.frame.NamedIndividualFrameEditor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class NamedIndividualFrameEditorManager implements EditorManager<OWLEntityDataContext, LabelledFrame<NamedIndividualFrame>> {

    private NamedIndividualFrameEditor editor;

    @Override
    public EditorView<LabelledFrame<NamedIndividualFrame>> getView(OWLEntityDataContext context) {
        if (editor == null) {
            ProjectId projectId = context.getProjectId();
            editor = new NamedIndividualFrameEditor(projectId);
        }
        return editor;
    }

    @Override
    public GetObjectAction<LabelledFrame<NamedIndividualFrame>> createGetObjectAction(OWLEntityDataContext editorContext) {
        return new GetNamedIndividualFrameAction(editorContext.getEntityData().getEntity().asOWLNamedIndividual(), editorContext.getProjectId());
    }

    @Override
    public UpdateObjectAction<LabelledFrame<NamedIndividualFrame>> createUpdateObjectAction(LabelledFrame<NamedIndividualFrame> pristineObject, LabelledFrame<NamedIndividualFrame> editedObject, OWLEntityDataContext editorContext) {
        return new UpdateNamedIndividualFrameAction(editorContext.getProjectId(), pristineObject, editedObject);
    }

    @Override
    public String getDescription(OWLEntityDataContext editorContext) {
        return "Named individual description for " + editorContext.getEntityData().getBrowserText();
    }
}
