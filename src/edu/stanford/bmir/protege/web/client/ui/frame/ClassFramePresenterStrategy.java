package edu.stanford.bmir.protege.web.client.ui.frame;

import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/01/2013
 */
public class ClassFramePresenterStrategy implements FramePresenterStrategy<LabelledFrame<ClassFrame>, ClassFrame, OWLClass> {

    @Override
    public ValueEditor<LabelledFrame<ClassFrame>> getEditor(ProjectId projectId) {
        PropertyValueListEditor annotationsEditor = new PropertyValueListEditor(projectId);
        PropertyValueListEditor propertiesEditor = new PropertyValueListEditor(projectId);
        return new ClassFrameEditor(projectId, annotationsEditor, propertiesEditor);
    }

}
