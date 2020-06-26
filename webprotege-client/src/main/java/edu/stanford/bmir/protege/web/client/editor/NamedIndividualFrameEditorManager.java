package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetNamedIndividualFrameResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.UpdateNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.client.frame.NamedIndividualFrameEditor;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class NamedIndividualFrameEditorManager implements EditorManager<OWLEntityContext, NamedIndividualFrame, GetNamedIndividualFrameAction, GetNamedIndividualFrameResult> {

    private final NamedIndividualFrameEditor editor;

    @Inject
    public NamedIndividualFrameEditorManager(NamedIndividualFrameEditor editor) {
        this.editor = editor;
    }

    @Override
    public EditorView<NamedIndividualFrame> getView(OWLEntityContext context) {
        return editor;
    }

    @Override
    public GetNamedIndividualFrameAction createAction(OWLEntityContext editorContext) {
        return new GetNamedIndividualFrameAction(editorContext.getProjectId(), editorContext.getEntity().asOWLNamedIndividual());
    }

    @Override
    public NamedIndividualFrame extractObject(GetNamedIndividualFrameResult result) {
        return result.getFrame();
    }

    @Override
    public UpdateNamedIndividualFrameAction createUpdateObjectAction(NamedIndividualFrame pristineObject, NamedIndividualFrame editedObject, OWLEntityContext editorContext) {
        return new UpdateNamedIndividualFrameAction(editorContext.getProjectId(),
                                                    pristineObject.toPlainFrame(),
                                                    editedObject.toPlainFrame());
    }

    @Override
    public String getDescription(OWLEntityContext editorContext) {
        return "Named individual description";
    }
}
