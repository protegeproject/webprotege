package edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.frame.*;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class ClassFrameEditorFactory implements EditorConfigurationFactory<LabelledFrame<ClassFrame>> {

    @Override
    public ValueEditor<LabelledFrame<ClassFrame>> getEditor(ProjectId projectId) {
        PropertyValueListEditor annotationsEditor = new PropertyValueListEditor(projectId);
        PropertyValueListEditor propertiesEditor = new PropertyValueListEditor(projectId);
        return new ClassFrameEditor(projectId, annotationsEditor, propertiesEditor);
    }

    @Override
    public EditorUpdateStrategy<LabelledFrame<ClassFrame>> getUpdateStrategy(ProjectId projectId) {
        return new EditorUpdateStrategy<LabelledFrame<ClassFrame>>() {
            @Override
            public boolean shouldUpdateEditor(EditorContext<LabelledFrame<ClassFrame>> editorContext) {
                return editorContext.getSelectedEntityOfType(EntityType.CLASS).isPresent();
            }

            @Override
            public String getEditorTitle(EditorContext<LabelledFrame<ClassFrame>> editorContext) {
                final Optional<OWLEntityData> selectedEntityOfType = editorContext.getSelectedEntityOfType(EntityType.CLASS);
                if(selectedEntityOfType.isPresent()) {
                    return "Class description for " + editorContext.getSelectedEntity().get().getBrowserText();
                }
                else {
                    return "No class selected";
                }
            }
        };
    }



}
