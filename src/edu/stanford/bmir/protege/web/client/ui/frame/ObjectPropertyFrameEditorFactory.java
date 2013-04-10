package edu.stanford.bmir.protege.web.client.ui.frame;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.ui.library.common.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor.*;
import org.semanticweb.owlapi.model.EntityType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class ObjectPropertyFrameEditorFactory implements EditorConfigurationFactory<LabelledObjectPropertyFrame> {

    @Override
    public ValueEditor<LabelledObjectPropertyFrame> getEditor(ProjectId projectId) {
        return new ObjectPropertyFrameEditor(projectId);
    }

    @Override
    public EditorUpdateStrategy<LabelledObjectPropertyFrame> getUpdateStrategy(ProjectId projectId) {
        return new EditorUpdateStrategy<LabelledObjectPropertyFrame>() {
            @Override
            public boolean shouldUpdateEditor(EditorContext<LabelledObjectPropertyFrame> editorContext) {
                return editorContext.getSelectedEntityOfType(EntityType.OBJECT_PROPERTY).isPresent();
            }

            @Override
            public String getEditorTitle(EditorContext<LabelledObjectPropertyFrame> editorContext) {
                if(editorContext.getSelectedEntityOfType(EntityType.OBJECT_PROPERTY).isPresent()) {
                    return "Description for " + editorContext.getSelectedEntityOfType(EntityType.OBJECT_PROPERTY).get().getBrowserText();
                }
                else {
                    return "No object property selected";
                }
            }
        };
    }

//    @Override
//    public UpdateObjectRequestFactory<LabelledObjectPropertyFrame> getUpdateObjectRequestFactory(final ProjectId projectId) {
//        return new UpdateObjectRequestFactory<LabelledObjectPropertyFrame>() {
//            @Override
//            public UpdateObjectRequest<LabelledObjectPropertyFrame> createUpdateObjectRequest(EditorContext<LabelledObjectPropertyFrame> editorContext, LabelledObjectPropertyFrame from, LabelledObjectPropertyFrame to) {
//                return new UpdateObjectPropertyFrameRequest(editorContext.getProjectId(), from, to);
//            }
//        };
//    }
//
//    @Override
//    public GetObjectRequestFactory<LabelledObjectPropertyFrame> getGetObjectRequestFactory(ProjectId projectId) {
//        return new GetObjectRequestFactory<LabelledObjectPropertyFrame>() {
//            @Override
//            public GetObjectRequest<LabelledObjectPropertyFrame> createGetObjectRequest(EditorContext editorContext) {
//                final Optional<OWLObjectPropertyData> selectedEntityOfType = editorContext.getSelectedEntityOfType(EntityType.OBJECT_PROPERTY);
//                return new GetObjectPropertyFrameRequest(editorContext.getProjectId(), selectedEntityOfType.get().getEntity());
//            }
//        };
//    }
}
