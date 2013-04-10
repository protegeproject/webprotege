package edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/01/2013
 */
public interface UpdateObjectRequestFactory<T extends Serializable> {

    UpdateObjectRequest<T> createUpdateObjectRequest(EditorContext<T> editorContext, T from, T to);
}
