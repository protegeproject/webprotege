package edu.stanford.bmir.protege.web.client.ui.frame;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor.AbstractObjectEditorPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor.EditorContext;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class ObjectPropertyFramePortlet extends AbstractObjectEditorPortlet<LabelledObjectPropertyFrame> {

    public ObjectPropertyFramePortlet(Project project) {
        super(project, new ObjectPropertyFrameEditorFactory());
    }

    @Override
    protected UpdateObjectAction<LabelledObjectPropertyFrame> createUpdateAction(EditorContext<LabelledObjectPropertyFrame> editorContext, LabelledObjectPropertyFrame from, LabelledObjectPropertyFrame to) {
        return null;
    }

    @Override
    protected GetObjectAction<LabelledObjectPropertyFrame> createGetObjectAction(EditorContext<LabelledObjectPropertyFrame> editorContext) {
        return null;
    }
}
