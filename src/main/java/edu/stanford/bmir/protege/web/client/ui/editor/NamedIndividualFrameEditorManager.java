package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.client.ui.frame.NamedIndividualFrameEditor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class NamedIndividualFrameEditorManager implements EditorManager<OWLEntityDataContext, LabelledFrame<NamedIndividualFrame>> {

    private final NamedIndividualFrameEditor editor;

    @Inject
    public NamedIndividualFrameEditorManager(NamedIndividualFrameEditor editor) {
        this.editor = editor;
    }

    @Override
    public EditorView<LabelledFrame<NamedIndividualFrame>> getView(OWLEntityDataContext context) {
        return editor;
    }

    @Override
    public GetObjectAction<LabelledFrame<NamedIndividualFrame>> createGetObjectAction(OWLEntityDataContext editorContext) {
        return new GetNamedIndividualFrameAction(editorContext.getEntity().asOWLNamedIndividual(), editorContext.getProjectId());
    }

    @Override
    public UpdateObjectAction<LabelledFrame<NamedIndividualFrame>> createUpdateObjectAction(LabelledFrame<NamedIndividualFrame> pristineObject, LabelledFrame<NamedIndividualFrame> editedObject, OWLEntityDataContext editorContext) {
        return new UpdateNamedIndividualFrameAction(editorContext.getProjectId(), pristineObject, editedObject);
    }

    @Override
    public String getDescription(OWLEntityDataContext editorContext) {
        return "Named individual description";
    }
}
