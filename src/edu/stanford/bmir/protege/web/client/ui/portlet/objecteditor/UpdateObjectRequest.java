package edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor;

import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/01/2013
 */
public interface UpdateObjectRequest<T extends Serializable> extends HasProjectAction {
//    ProjectId getProjectId();

    T getFrom();

    T getTo();
}
