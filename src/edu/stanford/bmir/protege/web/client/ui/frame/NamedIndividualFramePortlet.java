package edu.stanford.bmir.protege.web.client.ui.frame;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor.AbstractObjectEditorPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor.EditorContext;
import edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor.NamedIndividualFrameEditorFactory;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/12/2012
 */
public class NamedIndividualFramePortlet extends AbstractObjectEditorPortlet<LabelledFrame<NamedIndividualFrame>> {

    public NamedIndividualFramePortlet(Project project) {
        super(project, new NamedIndividualFrameEditorFactory());
    }

    @Override
    protected UpdateObjectAction<LabelledFrame<NamedIndividualFrame>> createUpdateAction(EditorContext<LabelledFrame<NamedIndividualFrame>> editorContext, LabelledFrame<NamedIndividualFrame> from, LabelledFrame<NamedIndividualFrame> to) {
        return new UpdateNamedIndividualFrameAction(editorContext.getProjectId(), from, to);
    }

    @Override
    protected GetObjectAction<LabelledFrame<NamedIndividualFrame>> createGetObjectAction(EditorContext<LabelledFrame<NamedIndividualFrame>> editorContext) {
        OWLNamedIndividual selIndividual = (OWLNamedIndividual) editorContext.getSelectedEntity().get().getEntity();
        return new GetNamedIndividualFrameAction(selIndividual, editorContext.getProjectId());
    }
}
