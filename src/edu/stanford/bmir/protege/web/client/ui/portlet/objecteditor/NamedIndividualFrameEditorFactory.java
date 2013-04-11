package edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.client.ui.frame.NamedIndividualFrameEditor;
import edu.stanford.bmir.protege.web.client.ui.library.common.ValueEditor;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class NamedIndividualFrameEditorFactory implements EditorConfigurationFactory<LabelledFrame<NamedIndividualFrame>> {

    @Override
    public ValueEditor<LabelledFrame<NamedIndividualFrame>> getEditor(ProjectId projectId) {
        return new NamedIndividualFrameEditor(projectId);
    }

    @Override
    public EditorUpdateStrategy<LabelledFrame<NamedIndividualFrame>> getUpdateStrategy(ProjectId projectId) {
        return new EditorUpdateStrategy<LabelledFrame<NamedIndividualFrame>>() {
            @Override
            public boolean shouldUpdateEditor(EditorContext<LabelledFrame<NamedIndividualFrame>> editorContext) {
                return editorContext.getSelectedEntityOfType(EntityType.NAMED_INDIVIDUAL).isPresent();
            }

            @Override
            public String getEditorTitle(EditorContext<LabelledFrame<NamedIndividualFrame>> editorContext) {
                final Optional<OWLEntityData> selEntity = editorContext.getSelectedEntityOfType(EntityType.NAMED_INDIVIDUAL);
                if(selEntity.isPresent()) {
                    return "Types and properties for " + selEntity.get().getBrowserText();
                }
                else {
                    return "No individual selected";
                }
            }
        };
    }

}
