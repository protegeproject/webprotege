package edu.stanford.bmir.protege.web.client.portlet.objecteditor;

import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 * <p>
 *     A factory object for an editor that edits objects of type {@code T}.
 * </p>
 */
public interface EditorConfigurationFactory<O extends Serializable> {

    public ValueEditor<O> getEditor(ProjectId projectId);

    public EditorUpdateStrategy<O> getUpdateStrategy(ProjectId projectId);

}
