package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetClassFrameAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateClassFrameAction;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor.AbstractObjectEditorPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor.ClassFrameEditorFactory;
import edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor.EditorContext;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public class ClassFramePortlet extends AbstractObjectEditorPortlet<LabelledFrame<ClassFrame>> {

    public ClassFramePortlet(Project project) {
        super(project, new ClassFrameEditorFactory());
    }

    @Override
    protected GetObjectAction<LabelledFrame<ClassFrame>> createGetObjectAction(EditorContext<LabelledFrame<ClassFrame>> editorContext) {
        Optional<OWLEntityData> sel =  editorContext.getSelectedEntity();
        final OWLClass selClass = (OWLClass) sel.get().getEntity();
        return new GetClassFrameAction(selClass, editorContext.getProjectId());
    }

    @Override
    protected UpdateObjectAction<LabelledFrame<ClassFrame>> createUpdateAction(EditorContext<LabelledFrame<ClassFrame>> editorContext, LabelledFrame<ClassFrame> from, LabelledFrame<ClassFrame> to) {
        return new UpdateClassFrameAction(editorContext.getProjectId(), from, to);
    }
}
