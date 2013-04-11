package edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor;

import edu.stanford.bmir.protege.web.client.ui.library.common.ValueEditor;
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
public interface EditorConfigurationFactory<T extends Serializable> {

    public ValueEditor<T> getEditor(ProjectId projectId);

    public EditorUpdateStrategy<T> getUpdateStrategy(ProjectId projectId);

}
