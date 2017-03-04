package edu.stanford.bmir.protege.web.client.projectmanager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 * <p>
 *     An interface that can handle load requests for projects.
 * </p>
 */
public interface HasLoadProjectRequestHandler {

    /**
     * Sets the handler that will be called by this view when a load project request is received.
     * @param handler The handler.  Not {@code null}.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    void setLoadProjectRequestHandler(LoadProjectRequestHandler handler);
}
